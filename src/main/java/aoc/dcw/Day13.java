package aoc.dcw;

import aoc.dcw.util.Point;
import aoc.dcw.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day13 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<Machine> machines;
    int costA = 3;
    int costB = 1;

    public Day13(String resource) {
        List<String> lines = Utilities.getLines(resource);
        logger.debug("lines: {}",lines.size());
        machines = new ArrayList<>();
        for(Iterator<String> it = lines.iterator();it.hasNext();) {
            machines.add(new Machine(it.next(),it.next(),it.next()));
            if(it.hasNext())it.next();
        }
        logger.info("{} machines",machines.size());
    }
    class Machine {
        Point buttonA;
        Point buttonB;
        Point prize;
        public Machine(String a, String b,String p){
            String[] xy = a.split(":")[1].split(",");
            buttonA = new Point(Integer.parseInt(xy[0].split("\\+")[1]),Integer.parseInt(xy[1].split("\\+")[1].trim()));
            xy = b.split(":")[1].split(",");
            buttonB = new Point(Integer.parseInt(xy[0].split("\\+")[1]),Integer.parseInt(xy[1].split("\\+")[1].trim()));
            xy = p.split(":")[1].split(",");
            prize = new Point(Integer.parseInt(xy[0].split("=")[1]),Integer.parseInt(xy[1].split("=")[1].trim()));
        }
    }
    int max = 100;

    public  long solvePart1() {
        long totalTokens = 0 ;
        for (Machine machine : machines) {
            Point butA = machine.buttonA;
            Point butB = machine.buttonB;
            totalTokens += solveV3(new Point(machine.prize), butA, butB, 100);
        }
        return totalTokens;
    }

    public long solvePart2() {
        long totalTokens = 0 ;
        Point add = new Point(10000000000000L,10000000000000L);
        for (Machine machine : machines) {
            // first check distances
            Point butA = machine.buttonA;
            Point butB = machine.buttonB;
            Point end = new Point(machine.prize);
            end.add(add);
            long newTotal = totalTokens + solveV3(end, butA, butB, -1);
            if(newTotal < totalTokens) throw new IllegalStateException();
            totalTokens = newTotal;
            logger.debug("total tokens: {}",totalTokens);
        }
        return totalTokens;
    }

    public long solveV3(Point endPoint, Point butA, Point butB, long max) {

        logger.debug("solving from {} for A: {} B: {}",endPoint,butA,butB);

        Point start = new Point(0,0);
        Point current = new Point(0,0);
        //while(current.x < endPoint.x && current.y < endPoint.y) {


        long bTimes = (butA.x * endPoint.y - butA.y * endPoint.x) / (butA.x * butB.y - butA.y * butB.x);

        Point bEnd = new Point(butB);
        bEnd.mul(bTimes);
        logger.info("press B {} times to {}",  bTimes, bEnd);

        Point sub = new Point(endPoint.x - bTimes*butB.x, endPoint.y - bTimes*butB.y);
        logger.info("remaining1: {}",  sub);


        Point end = new Point(endPoint);
        end.subtract(bEnd);
        logger.info("remaining2: {}",  sub);

        long modX = end.x % butA.x;
        long aTimesFromBx = end.x / butA.x;
        logger.info("aTimesFromBx: {} modX: {}",  aTimesFromBx,modX);

        long modY = end.y % butA.y;
        long aTimesFromBy = end.y / butA.y;
        logger.info("aTimesFromBy: {} modY: {}",  aTimesFromBy,modY);

        long aTimes = (butB.x * endPoint.y - butB.y * endPoint.x) / (butB.x * butA.y - butB.y * butA.x);

        Point aEnd = new Point(butA);
        aEnd.mul(aTimes);
        logger.info("press A {} times to {}",  aTimes, aEnd);

        if(bTimes >= 0  && aTimes >= 0 && modX == 0 && modY == 0 && (max == -1 ||  (bTimes <= max && aTimes <= max))){


            Point a = new Point(butA);
            Point b = new Point(butB);
            a.mul(aTimes);
            logger.debug("amul: {}",a);
            b.mul(bTimes);
            logger.debug("bmul: {}",b);
            a.add(b);
            logger.debug("a+b: {}",a);

            long tokens = aTimes * costA + bTimes * costB;
            logger.debug("tokens: {}",tokens);

            if(!a.equals(endPoint)) {
                logger.error("breaking because result was {}",a);
                throw new IllegalStateException();
            }
            return tokens;
        }

        //}
        return 0;

    }

}
