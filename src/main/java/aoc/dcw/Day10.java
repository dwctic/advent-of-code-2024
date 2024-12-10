package aoc.dcw;

import aoc.dcw.util.CharacterMap;
import aoc.dcw.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static int TRAILHEAD = 0;
    public static int TRAILEND = 9;
    CharacterMap map;
    //int[] dirs = new int[]{-1,0,1};
    int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    public Day10(String file) {
        map = new CharacterMap(file);
    }

    public int rateTrails() {
        return walkTrails()[1];
    }

    public int scoreTrails() {
        return walkTrails()[0];
    }

    public int[] walkTrails() {
        List<Point> trailheads = map.findInt(TRAILHEAD);
        int score = 0;
        int rating = 0;
        for (Point p : trailheads) {
            logger.debug("TRAILHEAD {}",p);
            Set<Point> allPoints = new HashSet<>();
            Set<Point> reachable = new HashSet<>();
            Trail start = new Trail();
            Set<Trail> distinct = walk(reachable,allPoints, start,-1, p);
            logger.debug("TRAILHEAD {} can reach {}",p,reachable.size());
            logger.debug("TRAILHEAD {} distinct {}",p,distinct.size());
            rating += distinct.size();
            score += reachable.size();
        }
        return new int[]{score,rating};
    }

    public Set<Trail> walk(Set<Point> reachable,Set<Point> walkedPoints, Trail current, int fromHeight, Point check) {

        //if(walkedPoints.contains(check)) return Collections.emptySet();
        int height = map.getInt(check);
        if (height == fromHeight + 1) {
            walkedPoints.add(check);
            current.add(check);
            logger.debug("walking: {} height: {} -> {}",check,fromHeight,height);
            if (height == TRAILEND) {
                logger.debug("found trailend at: {}",check);
                reachable.add(check);
                return Set.of(current);
            }
            Set<Trail> nextTrails = new HashSet<>();
            for (Point next : check.cardinalDirections()) {
                Trail nextTrail = new Trail(current);
                nextTrail.add(next);
                nextTrails.addAll(walk(reachable,walkedPoints, nextTrail,height, next));
            }
            return nextTrails;
        }
        else return Collections.emptySet();
    }

    public static class Trail extends ArrayList<Point> {
        public Trail(Trail start) {
            super(start);
        }

        public Trail() {

        }
    }

}
