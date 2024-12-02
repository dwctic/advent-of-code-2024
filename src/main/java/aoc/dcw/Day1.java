package aoc.dcw;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Day1 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        List<Integer> leftNums = new ArrayList<>();
        List<Integer> rightNums = new ArrayList<>();
        Files.lines(Path.of(Day1.class.getClassLoader().getResource("day1.txt").toURI()))
                .map(StringUtils::split)
                .forEach(parts -> {
            leftNums.add(Integer.valueOf(parts[0]));
            rightNums.add(Integer.valueOf(parts[1]));
        });
        Collections.sort(leftNums);
        Collections.sort(rightNums);
        int totalDistance = 0;
        for(int i=0;i<leftNums.size();i++){
            Integer left = leftNums.get(i);
            Integer right = rightNums.get(i);
            int distance = Math.abs(left-right);
            totalDistance += distance;
        }

        System.out.println("total distance: "+totalDistance);

        long totalSimilarity = 0;
        for (Integer left : leftNums) {
            long count = rightNums.stream().filter(Predicate.isEqual(left)).count();
            totalSimilarity += left * count;
        }

        System.out.println("totalSimilarity: "+totalSimilarity);
    }



}
