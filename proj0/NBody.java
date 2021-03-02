public class NBody {
    public static double readRadius(String s) {
        In in = new In(s);
        in.readInt();  // skip number
        return in.readDouble();
    }

    public static Planet[] readPlanets(String s) {
        In in = new In(s);
        int N = in.readInt();
        in.readDouble(); // skip radius
        Planet[] allPlanets = new Planet[N];
        for (int i = 0; i < N; i++) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            allPlanets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }
        return allPlanets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] allPlanets = readPlanets(filename);

        /* Drawing the background. */
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        StdDraw.show();

        /* Drawing planets. */
        for (Planet p : allPlanets) {
            p.draw();
        }

        /* Creating an animation. */
        StdDraw.enableDoubleBuffering();

        for (double time = 0; time <= T; time = time + dt) {
            double[] xForces = new double[allPlanets.length];
            double[] yForces = new double[allPlanets.length];

            /* Calculate net x and y forces for each planet,
            *  storing these in xForces and yForces respectively. */
            for (int i = 0; i < allPlanets.length; i++) {
                xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
                yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
            }

            /* Update each planet's position, velocity, and acceleration. */
            for (int i = 0; i < allPlanets.length; i++) {
                allPlanets[i].update(dt, xForces[i], yForces[i]);
            }

            /* Draw the background image and all of the planets. */
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet p : allPlanets) {
                p.draw();
            }

            /* Show the offscreen buffer. */
            StdDraw.show();
            StdDraw.pause(10);
        }

        /* Printing the universe. */
        StdOut.printf("%d\n", allPlanets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < allPlanets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    allPlanets[i].xxPos, allPlanets[i].yyPos, allPlanets[i].xxVel,
                    allPlanets[i].yyVel, allPlanets[i].mass, allPlanets[i].imgFileName);
        }
    }
}
