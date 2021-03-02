public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private double G = 6.67e-11;

    public Planet(double xP, double yP, double xV,
                double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx1 = xxPos;
        double dy1 = yyPos;
        double dx2 = p.xxPos;
        double dy2 = p.yyPos;
        return Math.sqrt(((dx2-dx1)*(dx2-dx1) + (dy2-dy1)*(dy2-dy1)));
    }

    public double calcForceExertedBy(Planet p) {
        double m1 = mass;
        double m2 = p.mass;
        double r = calcDistance(p);
        return G * m1 * m2 / (r * r);
    }

    public double calcForceExertedByX(Planet p) {
        double F = calcForceExertedBy(p);
        double dx = p.xxPos - xxPos;
        double r = calcDistance(p);
        return F * dx / r;
    }

    public double calcForceExertedByY(Planet p) {
        double F = calcForceExertedBy(p);
        double dy = p.yyPos - yyPos;
        double r = calcDistance(p);
        return F * dy / r;
    }

    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double NetForceX = 0;
        for (Planet p : allPlanets) {
            if (!this.equals(p)) {
                NetForceX = NetForceX + calcForceExertedByX(p);
            }
        }
        return NetForceX;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double NetForceY = 0;
        for (Planet p : allPlanets) {
            if (!this.equals(p)) {
                NetForceY = NetForceY + calcForceExertedByY(p);
            }
        }
        return NetForceY;
    }

    public void update(double dt, double fX, double fY) {
        double ax = fX / mass;
        double ay = fY / mass;
        xxVel = xxVel + dt * ax;
        yyVel = yyVel + dt * ay;
        xxPos = xxPos + dt * xxVel;
        yyPos = yyPos + dt * yyVel;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "./images/"+imgFileName);
    }
}
