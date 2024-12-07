package aoc.dcw;

import aoc.AoC;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws IOException {
        new Day7().run();
    }

    public void run() throws IOException {
        //run("day7control.txt");
        run("day7.txt");
    }

    public void run(String file) throws IOException {
        List<String> lines = List.of(IOUtils.toString(AoC.class.getClassLoader().getResourceAsStream(file), Charset.defaultCharset()).split("\n"));
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
