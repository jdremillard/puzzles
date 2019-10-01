package net.remgant.puzzles;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class PickyEater {

    public static void main(String args[]) {
        System.out.println(new PickyEater().compute(args.length > 0 ? Integer.parseInt(args[0]) : 250));
    }

    private double compute(int slices) {
        Line2D[] lines = new Line2D[]{
                new Line2D.Double(-0.5, 0.5, 0.5, 0.5),
                new Line2D.Double(0.5, 0.5, 0.5, -0.5),
                new Line2D.Double(0.5, -0.5, -0.5, -0.5),
                new Line2D.Double(-0.5, -0.5, -0.5, 0.5)
        };
        int eatable = 0;
        for (int i = 0; i < slices; i++) {
            for (int j = 0; j < slices; j++) {
                double x = (double) i / (double) slices - 0.5;
                double y = (double) j / (double) slices - 0.5;
                Point2D p = new Point2D.Double(x, y);
                double fromLine = Double.MAX_VALUE;
                for (int k = 0; k < 4; k++) {
                    if (lines[k].ptLineDist(p) < fromLine)
                        fromLine = lines[k].ptLineDist(p);
                }
                double fromCenter = Math.sqrt(x * x + y * y);
                if (fromCenter < fromLine)
                    eatable++;
            }
        }
        return (double) eatable / (double) (slices * slices);
    }
}
