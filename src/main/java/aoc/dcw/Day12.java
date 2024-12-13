package aoc.dcw;

import aoc.dcw.util.Map2D;
import aoc.dcw.util.Point;
import org.apache.commons.geometry.euclidean.twod.ConvexArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Map2D map;

    public Day12(String resource) {
        map = new Map2D(resource);
    }

   /* Map<Point, Region> pointRegionMap = new HashMap<>();

    public int findRegions() {
        Set<Point> allPoints = map.getAllPoints();
        do {
            Point point = allPoints.iterator().next();
            //logger.debug("checking point {}",point);
            char value = map.getChar(point);
            logger.debug("checking point {} value :{}", point, value);
            Region region = pointRegionMap.computeIfAbsent(point, Region::new);
            Map2D.TouchingStats touching = map.findAllTouching(point, c -> c == value, true, false);
            region.touching = touching;
            logger.debug("touching: {}", touching.allTouching.size());
            touching.pointTouchingCardinal.forEach((p, s) -> perimeterMap.put(p, 4 - s.size()));
            region.addAll(touching.allTouching);
            allPoints.removeAll(region);
            //check all directions

        } while (allPoints.size() > 0);

        Collection<Region> regions = pointRegionMap.values();
        logger.info("found {} regions", regions.size());
        for (Region r : regions) {
            logger.info("region {} perimeter {}", map.getChar(r.iterator().next()), r.getPerimeter());
            logger.info("region: {}",r);
        }

        return regions.size();
    }


    public void getPrice2Scan() {




        for (Region region : pointRegionMap.values()) {
            logger.debug("REGION: {}", region);
            //Point start = region.stream().filter(p -> perimeterMap.get(p) > 0).findFirst().orElseThrow();
            // int topEdge = 0;
            // int sideEdge = 0;

            int currentY = -1;
            int sides = 0;
            List<Edge> edges = new ArrayList<>();
            for (int x = region.min.x; x <= region.max.x; x++) {
                logger.debug("COLUMN: {}", x);
                Edge currentEdge = null;
                boolean atEdge = false;
                boolean[] onEdge = new boolean[region.dimension.height];
                for(int j=0;j<=region.dimension.height;j++){

                    int y = j+region.min.y;
                    Point p = new Point(x,y);
                    boolean inside = region.contains(p);
                    if(inside) {
                        onEdge[j] = true;
                        if(currentEdge == null) {
                            logger.debug("starting edge at {}",p);
                            currentEdge = new Edge();
                        }
                        currentEdge.add(p);
                    }
                    else {
                        logger.debug("ending edge at {}",p);
                        edges.add(currentEdge);
                        currentEdge = null;
                    }
                }
                logger.debug("edges: {}",edges);
            }
        }

    }

    public static class Edge extends ArrayList<Point> {

    }

    public void getPrice2Trace() {

        for (Region region : pointRegionMap.values()) {
            logger.debug("region: {}",region);
            //Point start = region.stream().filter(p -> perimeterMap.get(p) > 0).findFirst().orElseThrow();
           // int topEdge = 0;
           // int sideEdge = 0;
            int currentY = -1;
            int sides = 0;
            for(int x =region.min.x;x<=region.max.x;x++){
                //topEdge++;
                logger.debug("scanning x: {} current y: {}",x,currentY);
                boolean currentInside = false;
                for(int y=region.min.y;y<=region.max.y;y++){
                    logger.debug("scanning y: {}",y);
                    Point p = new Point(x,y);
                    boolean inside = region.contains(p);
                    if(currentInside != inside) {
                        if(inside) {
                            if(y != currentY) {
                                logger.debug("new edge at y: {}",y);
                                sides++;
                                currentY = y;
                            }
                        }
                        else {
                            if(y != currentY) {
                                logger.debug("left shape at y: {}",y);
                                currentY = y;
                            }
                        }
                        currentInside = inside;
                    }

                }
            }
        }

    }

    public void trace(Region region, Point start){
        //
        // South
        if(region.contains(start.south())) {

        }

    }

    public void getPrice2Area() {

        JFrame frame = new JFrame();
        frame.setSize(600,600);

        List<Polygon> polygons = new ArrayList<>();
        List<Area> areas = new ArrayList<>();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g.create();

                AffineTransform tx = new AffineTransform(); //
                tx.concatenate( g2.getTransform() );
                tx.translate(100,100);
                tx.scale(20,20);

                g2.setTransform(tx);

                polygons.forEach(a -> {
                    g2.setColor(a.color);
                    g2.fill(a);
                });


                g2.dispose();

            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
        };
        frame.add(panel);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //createAndShowGUI();
                frame.setVisible(true);
            }
        });

        Color[] colors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.CYAN,Color.MAGENTA,Color.BLACK,Color.GRAY};
        int color = 0;
        for (Region region : pointRegionMap.values()) {

            logger.debug("region: {}", region);

            Area a = new Area();
            for (Point p : region) {
                Rectangle r = new Rectangle(p.x, p.y, 1, 1);
                a.add(new Area(r));
                logger.debug("added: {} poly: {} rect: {}", p, a.isPolygonal(), a.isRectangular());
            }


           // poly.color = colors[color++%colors.length];
            a.color = colors[color++%colors.length];
            Polygon poly = areaToPoly(a);
            poly.color = a.color;
            //logger.debug("region: {}",a);
            logger.debug("region: {} poly: {} rect: {}", region, a.isPolygonal(), a.isRectangular());
            logger.debug("area bounds: {}", a.getBounds());
            //logger.debug("poly: {}", poly);
            logger.debug("poly: {}", poly.npoints);

            //logger.debug("poly: {}", poly.npoints);
            float[] res = new float[6];
            PathIterator it = a.getPathIterator(null);
//            it.next();
            while (!it.isDone()) {
                it.currentSegment(res);
                //logger.debug("path: {}",res);
                it.next();
            }


           polygons.add(poly);
            areas.add(a);
        }

    }

public static class Polygon extends java.awt.Polygon {
        public Color color;
}

    public static class Area extends java.awt.geom.Area {
        public Color color;
        public Area(Rectangle r) {
            super(r);
        }

        public Area() {

        }
    }

    public static Polygon areaToPoly(Area a) {


   // Area a = new Area(new Rectangle(1, 1, 5, 5));
    PathIterator iterator = a.getPathIterator(null);
    float[] floats = new float[6];
    Polygon polygon = new Polygon();
    while(!iterator.isDone())

    {
        int type = iterator.currentSegment(floats);
        int x = (int) floats[0];
        int y = (int) floats[1];
        if (type != PathIterator.SEG_CLOSE) {
            polygon.addPoint(x, y);
            //System.out.println("adding x = " + x + ", y = " + y);
        }
        iterator.next();
    }
    return polygon;
}

    public void getPrice2() {

        logger.debug("getPrice2");

        for(Region region : pointRegionMap.values()) {
            logger.debug("region {}", region.value);
            Set<Point> outers = region.stream().filter(p -> perimeterMap.get(p) > 0).collect(Collectors.toCollection(HashSet::new));

            logger.debug("region {} size {} shell {}", region.value, region.size(), outers.size());

            // expand each outer point
            Set<Point> perimeterPoints = new HashSet<>();
            for (Point outer : outers) {
                perimeterPoints.addAll(outer.cardinalDirections().stream().filter(p -> !region.contains(p)).toList());
                perimeterPoints.addAll(outer.intercardinalDirections().stream().filter(p -> !region.contains(p)).toList());
            }

            logger.debug("perimeter points: {}", perimeterPoints.size());

            Point start = perimeterPoints.iterator().next();

            {
            //find first corner {
            //HashSet<Point> walked = new HashSet<>();
                HashSet<Point> walked = new HashSet<>();
                walked.add(start);
                 start = walkToCorner(walked,perimeterPoints,null,start);
            }

            logger.debug("starting at corner: {}",start);
        HashSet<Point> walked = new HashSet<>();
        walked.add(start);
            //if(region.value == 'V') {
                int turns = walk(walked,perimeterPoints,null,start) + 1;
                logger.info("region {} turns: {}",region ,turns);
            //}

        }

    }


    public int walk(Set<Point> walked,Set<Point> perimeter, Point from,Point at) {
        //find non-from point to walk to
        //logger.debug("walk at {}",at);
        int turns = 0;
        Point nextWalk = null;
        Point alreadyWalked = null;
        //logger.debug("testing {}",perimeter.size());
        int[] priorDirection = from != null ? new int[]{at.x - from.x, at.y - from.y} : null;
        //logger.debug("priorDirection: {}",priorDirection);
        for(Point p : perimeter) {
            //logger.debug("testing {}",p);
            if(p != from && at.touchingCardinal(p)){
                if(walked.contains(p)) {
                    alreadyWalked = p;
                }
                else {
                    logger.debug("walk from {} to {}",at,p);
                    nextWalk = p;
                    int[] nextDir =  new int[]{nextWalk.x - at.x, nextWalk.y - at.y};
                    //logger.debug("nextDirection: {}",nextDir);
                    if(priorDirection != null && !Arrays.equals(priorDirection,nextDir)) {
                        logger.debug("turning to {}",nextDir);
                        turns++;
                    }
                    break;
                }
            }
        }
        if(nextWalk == null) {
            if(alreadyWalked == null) {
                logger.debug("walk from {} BACK to FROM {}", at, from);
                nextWalk = from;
            }
            else {
                logger.debug("walking from {} BACK to {}", at, alreadyWalked);
                nextWalk = alreadyWalked;
                int[] lastDir =  new int[]{nextWalk.x - at.x, nextWalk.y - at.y};
                if(!Arrays.equals(priorDirection,lastDir)) {
                    //logger.debug("last to {}",nextDir);
                    turns++;
                }
                return turns;
            }
        }
        walked.add(nextWalk);
        turns+=walk(walked,perimeter,at,nextWalk);
        return turns;
    }

    public Point walkToCorner(Set<Point> walked,Set<Point> perimeter, Point from,Point at) {
        //find non-from point to walk to
        //logger.debug("walk at {}",at);
        //int turns = 0;
        Point nextWalk = null;
        Point alreadyWalked = null;
        //logger.debug("testing {}",perimeter.size());
        int[] priorDirection = from != null ? new int[]{at.x - from.x, at.y - from.y} : null;
        //logger.debug("priorDirection: {}",priorDirection);
        for(Point p : perimeter) {
            //logger.debug("testing {}",p);
            if(p != from && at.touchingCardinal(p)){
                if(walked.contains(p)) {
                    alreadyWalked = p;
                }
                else {
                    logger.debug("walk from {} to {}",at,p);
                    nextWalk = p;
                    int[] nextDir =  new int[]{nextWalk.x - at.x, nextWalk.y - at.y};
                    //logger.debug("nextDirection: {}",nextDir);
                    if(priorDirection != null && !Arrays.equals(priorDirection,nextDir)) {
                        logger.debug("turning to {}",nextDir);
                        return at;
                        //turns++;
                    }
                    break;
                }
            }
        }
        if(nextWalk == null) {
            if(alreadyWalked == null) {
                logger.debug("walk from {} BACK to FROM {}", at, from);
                nextWalk = from;
            }
            else {
                logger.debug("walking from {} BACK to {}", at, alreadyWalked);
                nextWalk = alreadyWalked;
                int[] lastDir =  new int[]{nextWalk.x - at.x, nextWalk.y - at.y};
                if(!Arrays.equals(priorDirection,lastDir)) {
                    //logger.debug("last to {}",nextDir);
                    //turns++;
                }
               // return turns;
            }
        }
        walked.add(nextWalk);
        return walkToCorner(walked,perimeter,at,nextWalk);
        //return turns;
    }



    public int walkPerimeter(Region region, Set<Point> remaining, Point from) {
        //find the next one to walk to
        Point to = null;
        for(Point p : remaining) {
            if(region.perimeterTouching.get(from).contains(p)){
             to = p;
             break;
            }
        }
        logger.debug("walking from {} to {}",from,to);
        remaining.remove(from);
        walkPerimeter(region,remaining,to);
        //see if both dimensions change
        // test if the walk is a turn
        //region.touching.pointTouching.get(point);

        //first find all touching in all directions, then remove interior cardinal points

        return -1;
    }

    Map<Point,Integer> perimeterMap = new HashMap<>();

    public int getPrice() {
        int price = 0;
        for(Region region : pointRegionMap.values()){
            price += region.getPrice();
        }
        return price;
    }

  *//*  public Set<Point> findTouching(Set<Point> touching, Point check, Predicate<> predicate) {

    }*//*

    public class Region extends HashSet<Point> {
        Point min = new Point(Integer.MAX_VALUE,Integer.MAX_VALUE);
        Point max = new Point(Integer.MIN_VALUE,Integer.MIN_VALUE);
        Map<Point,Set<Point>> perimeterTouching;
        Map2D.TouchingStats touching;
        int perimeter = -1;
        char value;
        Dimension dimension;
        public Region(Point p){
            this.add(p);
            this.value = map.getChar(p);
        }

        @Override
        public boolean add(Point point) {
            perimeter = -1;
            if(point.x < min.x) min.x = point.x;
            if(point.y < min.y) min.y = point.y;
            if(point.x > max.x) max.x = point.x;
            if(point.y > max.y) max.y = point.y;
            dimension = new Dimension(max.x-min.x+1,max.y-min.y+1);
            return super.add(point);
        }

        public int getPerimeter() {
            if(perimeter == -1) {
                perimeter = 0;
                for(Point p : this){
                    int per = perimeterMap.get(p);
                    logger.debug("point {} has perimeter {}",p,per);
                    perimeter += per;
                }
            }
            return perimeter;
        }

        public int getPrice() {
            return getPerimeter() * size();
        }
        public String toString(){
            return ""+value+" "+min+" -> "+max+" "+dimension.width+" x "+dimension.height;

        }

    }*/
}
