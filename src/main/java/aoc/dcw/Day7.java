package aoc.dcw;

import aoc.dcw.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

public class Day7 extends AoCDay {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        new Day7().run();
    }

    public void run()  {
        //run("day7control.txt");
        run("day7.txt");
    }

    public void run(String file) {
        List<String> lines = Utilities.getLines(file);
        long sum = lines.stream().map(this::testLine).mapToLong(i -> i).sum();
        logger.info("sum: {}",sum);
    }
    public long testLine(String line) {
        String[] parts = line.split(":");
        long total = Long.parseLong(parts[0]);
        List<Long> values = Arrays.stream(parts[1].split("\\s")).filter(s -> !s.isEmpty()).map(Long::valueOf).toList();
        return findOperators(total,0,values,true) ? total : 0;
    }

    public boolean findOperators(long expected, long current, List<Long> list,boolean allowCat) {
        if(list.isEmpty()){
            return expected == current;
        }
        else {
            List<Long> nextList = list.subList(1, list.size());
            long next = list.get(0);
            boolean add = findOperators(expected,current+next,nextList,allowCat);
            boolean div = findOperators(expected,current*next,nextList,allowCat);
            boolean cat = allowCat && findOperators(expected,Long.parseLong(current+""+next),nextList,allowCat);
            return add | div | cat;
        }
    }
}
