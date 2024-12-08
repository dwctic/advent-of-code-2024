package aoc.dcw;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 extends AoCDay {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) throws IOException, URISyntaxException {

        List<List<Integer>> reports = new ArrayList<>();
        Files.lines(Path.of(Day1.class.getClassLoader().getResource("day2.txt").toURI()))
                .map(StringUtils::split)
                .map(Arrays::asList)
                .map(l -> l.stream().map(Integer::valueOf).toList())
                .forEach(reports::add);

        int totalSafe = 0;
        for (List<Integer> report : reports) {

            boolean increasing = false;
            Integer last = null;
            boolean safe = true;

            for (int i = 0; i < report.size(); i++) {
                Integer value = report.get(i);
                if (i > 0) {
                    int diff = value - last;
                    int abs = Math.abs(diff);

                    if (abs < 1 || abs > 3) {
                        safe = false;
                        break;
                    }

                    boolean currentIncrease = diff > 0;

                    if (i == 1) increasing = currentIncrease;
                    else {
                        if (increasing != currentIncrease) {
                            safe = false;
                            break;
                        }
                    }
                }
                last = value;
            }
            if (safe) {
                logger.info("safe: {}",report);
                totalSafe++;
            }
        }

        logger.info("total safe: {}",totalSafe);

        part2(reports);

    }

    public static void part2(List<List<Integer>> reports) throws IOException {

        int totalSafe = 0;
        for (List<Integer> report : reports) {
            if (checkReport(report)) totalSafe++;
            else {
                for (int i = 0; i < report.size(); i++) {
                    List<Integer> mod = new ArrayList<>(report);
                    mod.remove(i);
                    if (checkReport(mod)) {
                        logger.info("safe after removing index {}: {}",i,report);
                        totalSafe++;
                        break;
                    }
                }
            }

        }

        logger.info("total safe: {}",totalSafe);
    }


    public static boolean checkReport(List<Integer> report) throws IOException {
        boolean increasing = false;
        Integer last = null;
        boolean safe = true;

        for (int i = 0; i < report.size(); i++) {
            Integer value = report.get(i);
            if (i > 0) {
                int diff = value - last;
                int abs = Math.abs(diff);

                if (abs < 1 || abs > 3) {
                    safe = false;
                    break;
                }

                boolean currentIncrease = diff > 0;
                if (i == 1) increasing = currentIncrease;
                else {
                    if (increasing != currentIncrease) {
                        safe = false;
                        break;
                    }
                }
            }
            last = value;
        }
        return safe;
    }


}
