package aoc.dcw;

import aoc.dcw.util.Utilities;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day11 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<String> stoneStrings;

    public Day11(String resource) {
        stoneStrings = Arrays.asList(Utilities.getString(resource).split("\\s"));
    }

    public List<String> blinkSlow(int num) {
        logger.info("blinkFast: {}",num);
        long start = System.currentTimeMillis();
        System.out.println("start: "+start);
        List<String> stones = new ArrayList<>(this.stoneStrings);
        for(int i=0;i<num;i++) {
            this.blinkFast(stones);
            long dur = System.currentTimeMillis() - start;
           logger.info("@ {} duration: {} stones: {}",i,dur,stones.size());
            if(stones.size() <= 31) logger.info("stones: {}",stones);
        }
        return stones;
    }

    public long blinkExpand(int num) {
        logger.info("blink: {}",num);
        long start = System.currentTimeMillis();
        System.out.println("start: "+start);
        List<Stream<String>> streams = new ArrayList<>();
       // Stream<String> s = Stream.empty();
        long total = 0;
        for(String stone : stoneStrings) {
            logger.debug("expanding {}",stone);
            total += expand(stone,num);
        }
        return total;
    }


    public void blinkFast(List<String> stones) {
        for(int i=0;i<stones.size();i++) {
            String stone = stones.get(i);
            if(stone.equals("0"))  {
                stones.set(i,"1");
            }
          else if(stone.length()%2 == 0) {

              String left = ""+Long.parseLong(stone.substring(0,stone.length()/2));
              String right = ""+Long.parseLong(stone.substring(stone.length()/2));
              stones.set(i,right);
              stones.add(i,left);
              i++;
          }
          else {
              String next = (Long.parseLong(stone)*2024)+"";
              stones.set(i,next);
          }
        }
    }

    static final char ZERO_C = '0';
    static final String ZERO = "0";
    static final String ONE = "1";
    Map<Pair<String,Integer>,Long> countCache = new HashMap<>();

    public long expand(String s, int to){
        logger.debug("expanded level {}",to);
        Pair<String,Integer> p = new ImmutablePair<>(s,to);
        if(countCache.containsKey(p)) return countCache.get(p);
        long result;
        if(to == 0) {
            result = 1;
        }
        else if(s.equals(ZERO)) {
            logger.debug("expanding 0 to 1");
            result = expand(ONE,to-1);
        }
        else {
            int length = s.length();
            if(length%2 == 0) {
                int mid = length / 2;

                String left = s.substring(0,mid);
                int r = -1;
                for(int i=mid;i<s.length();i++){
                    if(s.charAt(i) != ZERO_C) {
                        r = i;
                        break;
                    }
                }
                String right = r == -1 ? ZERO : s.substring(r);
                logger.debug("splitting {} into {} and {}",s,left,right);
                result = expand(left,to-1) + expand(right,to-1);
            }
            else {
                String n = ""+Long.parseLong(s)*2024;
                logger.debug("expanding {} to {}",s,n);
                result = expand(n,to-1);
            }
        }
        countCache.put(p,result);
        logger.debug("caching {} as {}",p,result);
        return result;
    }
}
