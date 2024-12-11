package aoc.dcw.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a 2D map of chars
 */
public class Map2D {

    public static char EOF = (char) -1;
    public final char[][] map;

    public Map2D(String resource) {
        this(Utilities.getLines(resource));
    }
    public Map2D(List<String> lines) {
        int y = 0;
        char[][] mapAr = new char[lines.size()][];
        for (char[] row : lines.stream().map(String::trim).map(String::toCharArray).toList()) {
            mapAr[y++] = row;
        }
        this.map = mapAr;
    }

    public char getChar(Point p) {
        return getChar(p.x,p.y);
    }

    public char getChar(int x, int y) {
        return y > -1 && x > -1 && y < map.length && x < map[y].length ? map[y][x] : EOF;
    }
    public void setChar(Point p, char c) {
        map[p.y][p.x] = c;
    }

    public int getInt(int x, int y) {
        char c = getChar(x,y);
        if(c == EOF) return EOF;
        else return Character.digit(c,10);
    }

    public int getInt(Point p) {
        return getInt(p.x,p.y);
    }

    public List<Point> findChar(char c) {
        List<Point> points = new ArrayList<>();
        for(int y=0;y<map.length;y++){
            for(int x=0;x<map[y].length;x++) {
                if(map[y][x] == c) points.add(new Point(x,y));
            }
        }
        return points;
    }

    public List<Point> findInt(int i) {
        char c = Character.forDigit(i,10);
        return findChar(c);
    }
}
