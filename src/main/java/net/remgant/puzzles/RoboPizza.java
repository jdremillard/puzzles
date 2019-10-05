package net.remgant.puzzles;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/*
   Monte Carlo simulation to calculate expected number of slices from the RoboPizza slicer
   described at https://fivethirtyeight.com/features/what-if-robots-cut-your-pizza/
 */
public class RoboPizza {

    public static void main(String args[]) {
        int trials = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        boolean draw = trials <= 25;
        System.out.println(new RoboPizza().expectedNumberOfSlices(trials, draw));
    }

    private double expectedNumberOfSlices(int trials, boolean draw) {
        Random random = new Random();
        int slices = 0;
        for (int i = 0; i < trials; i++) {
            // Create 6 random points around a unit circle
            Point2D p[] = new Point2D[6];
            for (int j = 0; j < 6; j++) {
                double angle = random.nextDouble() * (2.0 * Math.PI);
                p[j] = new Point2D.Double(Math.cos(angle), Math.sin(angle));
            }

            // Calculate slope and y-intercept for the cuts
            double m[] = new double[3];
            double b[] = new double[3];

            // Three cuts: point 0 to point 1, 0 to 2, and 1 to 2
            int idx[][] = {{0, 1}, {0, 2}, {1, 2}};
            for (int j = 0; j < 3; j++) {
                m[j] = (p[2 * j].getY() - p[2 * j + 1].getY()) / (p[2 * j].getX() - p[2 * j + 1].getX());
                b[j] = p[2 * j].getY() - m[j] * p[2 * j].getX();
            }

            // Find out how many intercepts there are between the lines
            AtomicInteger intercepts = new AtomicInteger(0);
            for (int j = 0; j < 3; j++) {
                calculateIntersectionPoint(m[idx[j][0]], b[idx[j][0]], m[idx[j][1]], m[idx[j][1]]).ifPresent(ip -> {
                    // If there's an intercept point and it's inside the circle, count it.
                    if (Math.sqrt(ip.getX() * ip.getX() + ip.getY() * ip.getY()) <= 1.0)
                        intercepts.incrementAndGet();
                });
            }

            // Count how many slices result from the number of intercepts
            switch (intercepts.get()) {
                case 0:
                    slices += 4;
                    break;
                case 1:
                    slices += 5;
                    break;
                case 2:
                    slices += 6;
                    break;
                case 3:
                    slices += 7;
                    break;
            }
            if (draw)
                Pizza.draw(p, String.format("pizza-%04d.png",i));
        }
        return (double) slices / (double) trials;
    }

    private Optional<Point2D> calculateIntersectionPoint(double m1, double b1, double m2, double b2) {

        if (m1 == m2) {
            return Optional.empty();
        }

        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        return Optional.of(new Point2D.Double(x, y));
    }
}
