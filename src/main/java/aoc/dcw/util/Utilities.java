package aoc.dcw.util;

import aoc.dcw.AoCDay;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Utilities {

    public static List<String> getLines(String resource) {
        return (List.of(getString(resource).split("\n")));
    }

    public static String getString(String resource) {
        try {
            return IOUtils.toString(AoCDay.class.getClassLoader().getResourceAsStream(resource), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long[] fill(int size, long value) {
        long[] values = new long[size];
        Arrays.fill(values,value);
        return values;
    }

    public static Stream<Long> fillStream(int size, long value) {
        return Arrays.stream(Utilities.fill(size, value)).boxed();
    }
}
