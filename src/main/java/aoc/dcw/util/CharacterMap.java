package aoc.dcw.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a 2D map of chars
 */
public class CharacterMap {

    public static char EOF = (char) -1;
    public final char[][] map;

    public CharacterMap(String resource) {
        this(Utilities.getLines(resource));
    }
    public CharacterMap(List<String> lines) {
        int y = 0;
        char[][] mapAr = new char[lines.size()][];
        for (char[] row : lines.stream().map(String::trim).map(String::toCharArray).toList()) {
            mapAr[y++] = row;
        }
        this.map = mapAr;
    }
    public char get(Point p) {
        return p.y > -1 && p.x > -1 && p.y < map.length && p.x < map[p.y].length ? map[p.y][p.x] : EOF;
    }
    public void set(Point p, char c) {
        map[p.y][p.x] = c;
    }
    public List<Point> find(char c) {
        List<Point> points = new ArrayList<>();
        for(int y=0;y<map.length;y++){
            for(int x=0;x<map[y].length;x++) {
                if(map[y][x] == c) points.add(new Point(x,y));
            }
        }
        return points;
    }
}
