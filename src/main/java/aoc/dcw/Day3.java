package aoc.dcw;

import aoc.AoC;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3 {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void run() throws URISyntaxException, IOException {

        //mul(8,5)
        String file = IOUtils.toString(AoC.class.getClassLoader().getResourceAsStream("day3.txt"), Charset.defaultCharset());

        Pattern pattern = Pattern.compile("mul\\(\\d{1,3}?,\\d{1,3}?\\)");

        Integer sum1 = pattern
                .matcher(file)
                .results()
                .map(MatchResult::group)
                .map(Day3::operate)
                .reduce(Integer::sum)
                .orElseThrow();

        logger.info("part 1 sum: {}",sum1);


        Integer sum2 = Stream.of(file.split("do\\(\\)"))
                .map(s -> {
                    int dont = s.indexOf("don't()");
                    return dont == -1 ? s : s.substring(0,dont);
                })
                .map(pattern::matcher)
                .flatMap(Matcher::results)
                .map(MatchResult::group)
                .map(Day3::operate)
                .reduce(Integer::sum)
                .orElseThrow();

        logger.info("part 2 sum: {}",sum2);

    }

    public static int operate(String s){
        String[] nums = s.substring(s.indexOf("(")+1,s.indexOf(")")).split(",");
        return Integer.valueOf(nums[0]) * Integer.valueOf(nums[1]);
    }
}
