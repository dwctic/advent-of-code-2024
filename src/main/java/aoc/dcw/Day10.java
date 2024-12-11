package aoc.dcw;

import aoc.dcw.util.Map2D;
import aoc.dcw.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Day10 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static int TRAIL_HEAD = 0;
    public static int TRAIL_END = 9;
    public Map2D map;

    public Day10(String file) {
        map = new Map2D(file);
    }

    public int rateTrails() {
        return walkTrails()[1];
    }

    public int scoreTrails() {
        return walkTrails()[0];
    }

    public int[] walkTrails() {
        int score = 0;
        int rating = 0;
        for (Point p : map.findInt(TRAIL_HEAD)) {
            Set<Trail> distinct = walk(new Trail(),-1, p);
            Set<Point> reachable = distinct.stream().map(t -> t.get(t.size()-1)).collect(Collectors.toSet());
            rating += distinct.size();
            score += reachable.size();
        }
        return new int[]{score,rating};
    }

    // Recurse from each point and track the distinct trails
    public Set<Trail> walk(Trail current, int fromHeight, Point check) {
        int height = map.getInt(check);
        if (height == fromHeight + 1) {
            current.add(check);
            logger.debug("walking: {} height: {} -> {}",check,fromHeight,height);
            if (height == TRAIL_END) return Set.of(current);
            else {
                Set<Trail> nextTrails = new HashSet<>();
                for (Point next : check.cardinalDirections()) {
                    Trail nextTrail = new Trail(current);
                    nextTrail.add(next);
                    nextTrails.addAll(walk(nextTrail, height, next));
                }
                return nextTrails;
            }
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
