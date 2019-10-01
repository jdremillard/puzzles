package net.remgant.puzzles;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PickyEater {

    public static void main(String args[]) {
        for (int i = 3; i <= 32; i++)
            System.out.printf("%d %f%n", i, PickyEater.compute(i, 0.0025));
    }

    private static double compute(int sides, @SuppressWarnings("SameParameterValue") double delta) {
        Line2D[] lines = new Line2D[sides];
        Path2D path = new Path2D.Double();
        path.moveTo(1.0, 0.0);
        for (int i = 0; i < sides; i++) {
            double angle = (double) i / (double) sides * 2.0 * Math.PI;
            Point2D p1 = new Point2D.Double(Math.cos(angle), Math.sin(angle));
            angle = (double) (i + 1) / (double) sides * 2.0 * Math.PI;
            Point2D p2 = new Point2D.Double(Math.cos(angle), Math.sin(angle));
            lines[i] = new Line2D.Double(p1, p2);
            path.lineTo(p2.getX(), p2.getY());
        }
        int eatable = 0;
        int nonEatable = 0;
        Rectangle2D bounds = path.getBounds2D();
        for (double x = bounds.getX(); x <= bounds.getX() + bounds.getWidth(); x += delta) {
            for (double y = bounds.getY(); y <= bounds.getY() + bounds.getHeight(); y += delta) {
                Point2D p = new Point2D.Double(x, y);
                if (path.contains(p)) {
                    double fromLine = Double.MAX_VALUE;
                    for (int k = 0; k < sides; k++) {
                        if (lines[k].ptLineDist(p) < fromLine)
                            fromLine = lines[k].ptLineDist(p);
                    }
                    double fromCenter = Math.sqrt(x * x + y * y);
                    if (fromCenter < fromLine)
                        eatable++;
                    else
                        nonEatable++;
                }
            }
        }
        return (double) eatable / (double) (eatable + nonEatable);
    }
}
