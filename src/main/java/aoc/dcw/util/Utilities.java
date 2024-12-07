package aoc.dcw.util;

import aoc.dcw.AoCDay;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

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
}
