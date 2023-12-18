package main.kooozel.kotlin;

import java.awt.geom.Point2D;

public class Point extends Point2D {
    public long x;
    public long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = (long) x;
        this.y = (long) y;
    }

    public static Long calculateManhattanDistance(Point point1, Point point2) {
        return Math.abs(point2.x - point1.x) + Math.abs(point2.y - point1.y);
    }

    public Point plus(Direction direction, Long length) {
        long x = this.x;
        long y = this.y;
        switch (direction) {
        case DOWN -> {
            return new Point(x - length, y);
        }
        case RIGHT -> {
            return new Point(x, y + length);
        }
        case UP -> {
            return new Point(x + length, y);
        }
        case LEFT -> {
            return new Point(x, y - length);
        }

        }
        throw new IllegalArgumentException("Wrong Direction");
    }

    public Point plus(Direction direction) {
        return plus(direction, 1L);
    }
}

