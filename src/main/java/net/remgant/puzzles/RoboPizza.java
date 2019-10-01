package net.remgant.puzzles;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.Random;

public class RoboPizza {

    public static void main(String args[]) {
        System.out.println(new RoboPizza().expectedNumberOfSlices(1000));
    }

    private double expectedNumberOfSlices(int trials) {
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
                m[j] = (p[idx[j][0]].getY() - p[idx[j][1]].getY()) / (p[idx[j][0]].getX() - p[idx[j][1]].getX());
                b[j] = p[idx[j][0]].getY() - m[j] * p[idx[j][0]].getX();
            }

            // Find out how many intercepts there are between the lines
            int intercepts = 0;
            Optional<Point2D> ip = calculateIntersectionPoint(m[0], b[0], m[1], m[1]);
            if (ip.isPresent())
                if (Math.sqrt(ip.get().getX() * ip.get().getX() + ip.get().getY() * ip.get().getY()) <= 1.0)
                    intercepts++;
            ip = calculateIntersectionPoint(m[0], b[0], m[2], m[2]);
            if (ip.isPresent())
                if (Math.sqrt(ip.get().getX() * ip.get().getX() + ip.get().getY() * ip.get().getY()) <= 1.0)
                    intercepts++;
            ip = calculateIntersectionPoint(m[1], b[1], m[2], m[2]);
            if (ip.isPresent())
                if (Math.sqrt(ip.get().getX() * ip.get().getX() + ip.get().getY() * ip.get().getY()) <= 1.0)
                    intercepts++;

            // Count how many slices result from the number of intercepts
            switch (intercepts) {
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
