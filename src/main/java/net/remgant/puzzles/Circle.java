package net.remgant.puzzles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Random;

public class Circle {
    public static void main(String args[]) {
        Circle circle = new Circle();
        for (int i = 0; i < 10; i++)
            circle.draw(String.format("circle-%04d.png", i));
    }

    private Random random = new Random(1);

    private void draw(String fileName) {
        BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fill(new Rectangle2D.Double(0.0, 0.0, 800.0, 800.0));

        Area a = new Area();
        a.add(new Area(new Ellipse2D.Double(-0.5, -0.5, 1.0, 1.0)));
        a.transform(AffineTransform.getTranslateInstance(0.5, 0.5));
        a.transform(AffineTransform.getScaleInstance(800.0, 800.0));
        graphics.setColor(Color.BLACK);
        graphics.draw(a);

        Color[] colors = new Color[]{Color.RED, Color.RED, Color.BLUE, Color.BLUE, Color.GREEN, Color.GREEN};
        Point2D[] points = new Point2D[6];
        for (int i = 0; i < 6; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            Point2D p = new Point2D.Double(Math.sin(angle), Math.cos(angle));
            points[i] = p;
            a.reset();
            a.add(new Area(new Ellipse2D.Double(p.getX() / 2.0, p.getY() / 2.0, 0.01, 0.01)));
            a.transform(AffineTransform.getTranslateInstance(0.5, 0.5));
            a.transform(AffineTransform.getScaleInstance(800.0, 800.0));
            graphics.setColor(colors[i]);
            graphics.fill(a);
        }

        for (int i = 0; i < 3; i++) {
            Point2D p1 = points[i * 2];
            Point2D p2 = points[i * 2 + 1];
            Path2D path = new Path2D.Double();
            path.moveTo(p1.getX() / 2.0 - 0.01, p1.getY() / 2.0);
            path.lineTo(p1.getX() / 2.0 + 0.01, p1.getY() / 2.0);
            path.lineTo(p2.getX() / 2.0 + 0.01, p2.getY() / 2.0);
            path.lineTo(p2.getX() / 2.0 - 0.01, p2.getY() / 2.0);
            path.lineTo(p1.getX() / 2.0 - 0.01, p1.getY() / 2.0);

            a.reset();
            a.add(new Area(path));
            a.transform(AffineTransform.getTranslateInstance(0.5, 0.5));
            a.transform(AffineTransform.getScaleInstance(800.0, 800.0));
            graphics.setColor(Color.GRAY);
            graphics.fill(a);
        }

        try {
            ImageIO.write(image, "PNG", new File(fileName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
