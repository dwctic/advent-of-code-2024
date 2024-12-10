package aoc.dcw;

import aoc.dcw.util.CharacterMap;
import aoc.dcw.util.Point;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Day8 extends AoCDay {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    CharacterMap map;

    public static void main(String[] args) {
        new Day8("day8control1.txt");
    }

    public Day8(String file) {
        map = new CharacterMap(file);
    }

    public int part1() {
        //iterate through all possible antenna chars
        Set<Point> part1AntiNodes = new HashSet<>();
        //Set<Point> part2AntiNodes = new HashSet<>();
        for(char c=0;c<='z';c++){
          if(CharUtils.isAsciiAlphanumeric(c)) {
              Antenna ant = new Antenna(c,map.findChar(c));

              Set<Point> anti1 = findAntiNodes(ant, map, false);
              logger.debug("ant {} part1: {}", c, anti1.size());
              part1AntiNodes.addAll(anti1);
          }
        }
        logger.info("part 1: {}",part1AntiNodes.size());
        return part1AntiNodes.size();

    }

    public int part2() {
        //iterate through all possible antenna chars
        Set<Point> part2AntiNodes = new HashSet<>();
        for(char c=0;c<='z';c++){
            if(CharUtils.isAsciiAlphanumeric(c)) {
                Antenna ant = new Antenna(c,map.findChar(c));
                Set<Point> anti2 = findAntiNodes(ant, map, true);
                logger.debug("ant {} part2: {}", c, anti2.size());
                part2AntiNodes.addAll(anti2);

            }
        }
        logger.info("part 2: {}",part2AntiNodes.size());
        return part2AntiNodes.size();
    }

    public class Antenna {
        public final List<Point> nodes;
        final char c;
        public Antenna(char c,List<Point> points) {
            this.c = c;
            this.nodes = points;
        }
    }
    public static Set<Point> findAntiNodes(Antenna a, CharacterMap map, boolean scan) {
        logger.debug("antenna: {}",a.c);
        Set<Point> allAntiNodes = new HashSet<>();
        List<Point> checked = new ArrayList<>();
        List<Point> nodes = a.nodes;
        logger.debug("checking: {} nodes",nodes.size());
        for(Point point : nodes) {
            logger.debug("checking: {}",point);
            checked.add(point);
            Set<Point> antiNodes = new HashSet<>();
            // Check this point against all others
            nodes.stream().filter(Predicate.not(checked::contains)).forEach(p -> {
                int xd = p.x - point.x;
                int yd = p.y - point.y;
                Point an1 = new Point(p.x, p.y);
                Point an2 = new Point(point.x, point.y);

                boolean bothOob; // always break when both are OOB
                do {
                    bothOob = true;
                    an1.x += xd;
                    an1.y += yd;
                    an2.x -= xd;
                    an2.y -= yd;
                    //logger.info("checking an1: {}",an1);
                    if (map.getChar(an1) != CharacterMap.EOF) {
                        logger.debug("{} anti node: {} between {} and {}", a.c, an1, point, p);
                        antiNodes.add(new Point(an1));
                        bothOob = false;
                    }
                    //logger.info("checking an2: {}",an2);
                    if (map.getChar(an2) != CharacterMap.EOF) {
                        //logger.info("{} has anti node at: {}",c,an2);
                        logger.debug("{} anti node: {} between {} and {}", a.c, an2, point, p);
                        antiNodes.add(new Point(an2));
                        bothOob = false;
                    }
                }
                while(scan && !bothOob);
                logger.debug("adding antiNodes: {}",antiNodes.size());
                logger.debug("allAntiNodes: {}",allAntiNodes.size());
                allAntiNodes.addAll(antiNodes);
                logger.debug("allAntiNodes: {}",allAntiNodes.size());

            });
        }
        if(scan && nodes.size()>1) {
            allAntiNodes.addAll(nodes);
        }
        return allAntiNodes;
    }

}
