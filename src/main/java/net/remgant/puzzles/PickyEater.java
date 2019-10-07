package net.remgant.puzzles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.IntStream;

public class PickyEater {

    public static void main(String args[]) {
        for (int i = 3; i <= 32; i++)
            System.out.printf("%d %f%n", i, PickyEater.compute(i, 0.0025));
    }

    private static double compute(int sides, @SuppressWarnings("SameParameterValue") double delta) {
        if (sides < 3)
            throw new RuntimeException("Invalid size: must be 3 or greater");
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
        BufferedImage image = new BufferedImage((int)(1.0/delta)*2,(int)(1.0/delta)*2,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D r = new Rectangle2D.Double(0.0, 0.0, image.getWidth(), image.getHeight());
        graphics.setColor(Color.WHITE);
        graphics.fill(r);
        for (double x = bounds.getX(); x <= bounds.getX() + bounds.getWidth(); x += delta) {
            for (double y = bounds.getY(); y <= bounds.getY() + bounds.getHeight(); y += delta) {
                Point2D p = new Point2D.Double(x, y);
                if (path.contains(p)) {
                    //noinspection OptionalGetWithoutIsPresent
                    double fromLine = IntStream.range(0,sides)
                            .boxed()
                            .map(i -> lines[i].ptLineDist(p))
                            .min(Double::compareTo)
                            .get();
                    double fromCenter = Math.sqrt(x * x + y * y);
                    if (fromCenter < fromLine) {
                        eatable++;
                        graphics.setColor(new Color(64,64,64));
                    }
                    else {
                        nonEatable++;
                        graphics.setColor(new Color(192,192, 192));
                    }
                    r = new Rectangle2D.Double(x/delta + image.getWidth()/2.0,y/delta + image.getWidth()/2.0,1.0,1.0);
                    graphics.fill(r);
                }
            }
        }
        try {
            ImageIO.write(image,"PNG",new File(String.format("picky-%02d.png",sides)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return (double) eatable / (double) (eatable + nonEatable);
    }
}
