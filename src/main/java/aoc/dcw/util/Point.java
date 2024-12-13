package aoc.dcw.util;

import java.util.List;
import java.util.Objects;

public class Point {

    public long x;
    public long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public long getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void add(Point move){
        this.x += move.x;
        this.y += move.y;
    }

    public void mul(long mul){
        this.x *= mul;
        this.y *= mul;
    }

    public void subtract(Point move){
        this.x -= move.x;
        this.y -= move.y;
    }

    public List<Point> cardinalDirections() {
        return List.of(new Point(x+1,y),new Point(x-1,y),new Point(x,y+1), new Point(x,y-1));
    }

    public List<Point> intercardinalDirections() {
        return List.of(new Point(x+1,y+1),new Point(x-1,y-1),new Point(x-1,y+1), new Point(x+1,y-1));
    }

    public boolean touchingCardinal(Point p) {
        return cardinalDirections().contains(p);
    }

    public boolean touchingIntercardinal(Point p) {
        return intercardinalDirections().contains(p);
    }

    public boolean touching(Point p){
        return touchingIntercardinal(p) || touchingCardinal(p);
    }

    public Point south() {
        return new Point(x,y+1);
    }

    public Point north() {
        return new Point(x,y-1);
    }

    public Point east() {
        return new Point(x+1,y);
    }

    public Point west() {
        return new Point(x-1,y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + "x=" + x + ", y=" + y + ')';
    }

    public double distanceFromOrigin(){
        return Math.sqrt(x*x + y*y);
    }

    public double distanceFrom(Point other){
        return Math.sqrt((other.x-x)*(other.x-x) + (other.y-y)*(other.y-y));
    }
}
