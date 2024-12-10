package aoc.dcw;

import aoc.dcw.util.CharacterMap;
import aoc.dcw.util.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Day6 extends AoCDay {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static char EOF = (char) -1;
    static char OBS = '#';
    static char OLD = 'X';
    static char NEW = '.';

    CharacterMap map;
    PointAndDir guardStart;
    Guard guard;

    public static void main(String[] args) {
        new Day6().run();
    }

    public Day6() {
        map = new CharacterMap("day6.txt");
        int y = 0;
        for (char[] row : map.map) {
           // mapAr[y] = row;
            for (int x = 0; x < row.length; x++) {
                char c = row[x];
                if (c != NEW && c != OBS) {
                    guardStart = new PointAndDir(x, y, c);
                    break;
                }
            }
            y++;
        }
    }

    public void run() {
        part1();
        part2();
    }

    public int part1() {
        guard = new Guard(guardStart.x, guardStart.y, guardStart.dirChar);
        runGuard(guard);
        return guard.allPositions.size();
    }

    public int part2() {
        return runPart2(guard.allPositions);
    }

    public int runPart2(Set<Point> obstaclePositions) {
        obstaclePositions.remove(guardStart);
        Guard g;
        int loops = 0;
        for (Point position : obstaclePositions) {
            g = new Guard(guardStart.x, guardStart.y, guardStart.dirChar);
            State state = dropObstacleAndRun(position, g);
            if (state == State.LOOP) loops++;
        }
        logger.info("loops: {}", loops);
        return loops;
    }

    public State dropObstacleAndRun(Point newObs, Guard guard) {
        logger.info("Dropping obstacle at {}", newObs);
        map.setChar(newObs, OBS);
        State state = runGuard(guard);
        map.setChar(newObs, NEW);
        return state;
    }

    public State runGuard(Guard guard) {
        logger.info("Running Guard at: {}", guard.current);
        boolean inside;
        boolean loop = false;
        PointAndDir next;
        do {
            next = guard.getNextPosition();
            inside = next != null;
            if (next != null) {
                loop = guard.history.contains(next);
                guard.setPosition(next);
            }
        } while (inside && !loop);
        logger.info("Guard {} at position: {} with positions {}", loop ? "Looped" : "Left",guard.current, guard.allPositions.size());
        return loop ? State.LOOP : State.EXIT;
    }

    public enum State {EXIT, LOOP}

    // Class that understands how to move and turn the guard
    public class Guard {
        public Set<PointAndDir> history = new LinkedHashSet<>(); // Used to know if we hit a loop
        public Set<Point> allPositions = new LinkedHashSet<>(); // Used to calculate the total number of unique positions
        public PointAndDir current;
        public Guard(int x, int y, char c) {
            this.current = new PointAndDir(x, y, c);
            this.allPositions.add(current.toPoint());
            this.history.add(current);
        }
        public PointAndDir getNextPosition() {
            PointAndDir current = new PointAndDir(this.current);
            PointAndDir next = new PointAndDir(current);
            next.move();
            char nextSpot = map.getChar(next);
            while (nextSpot == OBS && nextSpot != EOF) {
                current.turnRight();
                next = new PointAndDir(current);
                next.move();
                nextSpot = map.getChar(next);
            }
            if (nextSpot == EOF) return null;
            return next;
        }
        public void setPosition(PointAndDir next) {
            this.current = next;
            allPositions.add(next.toPoint());
            history.add(next);
        }
    }

    // Class to capture location and direction in one swoop
    public static class PointAndDir extends Point {
        public char dirChar;
        public Point dir;
        public PointAndDir(PointAndDir pad) {
            this(pad.x, pad.y, pad.dirChar);
        }
        public PointAndDir(int x, int y, char dir) {
            super(x, y);
            this.dirChar = dir;
            this.dir = getDir(dirChar);
        }
        public void turnRight() {
            dirChar = switch (dirChar) {
                case '^' -> '>';
                case '>' -> 'v';
                case 'v' -> '<';
                case '<' -> '^';
                default -> ' ';
            };
            dir = getDir(dirChar);
        }
        public Point getDir(char c) {
            return new Point(c == '<' ? -1 : c == '>' ? 1 : 0, c == '^' ? -1 : c == 'v' ? 1 : 0);
        }
        public Point toPoint() {
            return new Point(x, y);
        }
        public void move() {
            this.x += dir.x;
            this.y += dir.y;
        }
        public String toString() {
            return "[" + x + "," + y + "," + dirChar + "]";
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            PointAndDir that = (PointAndDir) o;
            return dirChar == that.dirChar;
        }
        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), dirChar);
        }
    }
}