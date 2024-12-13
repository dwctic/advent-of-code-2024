package aoc.dcw.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Manages a 2D map of chars
 */
public class Map2D {

    public static char EOF = (char) -1;
    public final char[][] map;

    public Map2D(String resource) {
        this(Utilities.getLines(resource));
    }
    public Map2D(List<String> lines) {
        int y = 0;
        char[][] mapAr = new char[lines.size()][];
        for (char[] row : lines.stream().map(String::trim).map(String::toCharArray).toList()) {
            mapAr[y++] = row;
        }
        this.map = mapAr;
    }

    public char getChar(Point p) {
        return getChar((int) p.x, (int) p.y);
    }

    public char getChar(int x, int y) {
        return y > -1 && x > -1 && y < map.length && x < map[y].length ? map[y][x] : EOF;
    }
    public void setChar(Point p, char c) {
        map[(int) p.y][(int) p.x] = c;
    }

    public int getInt(int x, int y) {
        char c = getChar(x,y);
        if(c == EOF) return EOF;
        else return Character.digit(c,10);
    }

    public int getInt(Point p) {
        return getInt((int) p.x, (int) p.y);
    }

    public List<Point> findChar(char c) {
        List<Point> points = new ArrayList<>();
        for(int y=0;y<map.length;y++){
            for(int x=0;x<map[y].length;x++) {
                if(map[y][x] == c) points.add(new Point(x,y));
            }
        }
        return points;
    }

   /* public Set<Point> findTouching(Point p, Set<Point> toCheck,Predicate<Character> filter){
        Set<Point> matching = new HashSet<>();
        for(Point touching : p.cardinalDirections()) {
            char value = getChar(touching);
            if (filter.test(value)) {
                matching.add(touching);
            }
        }
        return matching;
    }*/

    public TouchingStats findAllTouching(Point check, Predicate<Character> predicate, boolean cardinal, boolean inter) {
        return findAllTouching(new TouchingStats(),check,predicate,cardinal,inter);
    }

    public TouchingStats findAllTouching(TouchingStats stats, Point check, Predicate<Character> predicate, boolean cardinal, boolean inter) {
        Set<Point> touchingAll = new HashSet<>();

        if(cardinal) {
            Set<Point> touchingCardinal = new HashSet<>();
            check.cardinalDirections().forEach(card -> {
                if (predicate.test(getChar(card))) touchingCardinal.add(card);
            });
            touchingAll.addAll(touchingCardinal);
            stats.pointTouchingCardinal.put(check,touchingCardinal);
        }
        if(inter) {
            Set<Point> touchingIntercardinal = new HashSet<>();
            check.intercardinalDirections().forEach(card -> {
                if (predicate.test(getChar(card))) touchingIntercardinal.add(card);
            });
            touchingAll.addAll(touchingIntercardinal);
            stats.pointTouchingIntercardinal.put(check,touchingIntercardinal);
        }

        stats.pointTouchingAll.put(check,touchingAll);
        //Set<Point> touching = findTouching(check,check.cardinalDirections().stream());


        touchingAll.forEach(t -> {
            stats.allTouching.add(t);
            if(!stats.pointTouchingCardinal.containsKey(t)) {
                findAllTouching(stats,t,predicate,cardinal,inter);
            }
            }
        );
        return stats;
    }

    public static class TouchingStats {
        public Map<Point,Set<Point>> pointTouchingCardinal = new HashMap<>();
        public Map<Point,Set<Point>> pointTouchingIntercardinal = new HashMap<>();
        public Map<Point,Set<Point>> pointTouchingAll = new HashMap<>();
        public Set<Point> allTouching = new HashSet<>();
    }

    public List<Point> findInt(int i) {
        char c = Character.forDigit(i,10);
        return findChar(c);
    }

    public Set<Point> getAllPoints() {
        Set<Point> points = new LinkedHashSet<>();
        for(int y=0;y<map.length;y++){
            for(int x=0;x<map[y].length;x++) {
                points.add(new Point(x,y));
            }
        }
        return points;
    }
}
