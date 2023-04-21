    public static boolean trueHitAlpha2(IPlain2D home, IPlain2D dest, Mask mhome, Mask mdest, float alpha, float beta) {
        class Point extends Point2D {

            private static final long serialVersionUID = 4155987867699953900L;

            public Point(float x, float y) {
                super(x, y);
                U = S = B = D = false;
                segments = new Segment[2];
            }

            public void rotate(double cosalpha, double sinalpha) {
                double nx, ny;
                nx = getX() * cosalpha - getY() * sinalpha;
                ny = getX() * sinalpha + getY() * cosalpha;
                set((float) nx, (float) ny);
            }

            public boolean U, S, B, D;

            public Segment segments[];

            public void USBD(Point b, Point c, Point d) {
                Vector<Point> v = new Vector<Point>(4);
                v.add(this);
                v.add(b);
                v.add(c);
                v.add(d);
                Point temp;
                {
                    Point max = v.get(0);
                    Point max2 = null;
                    float maxval = v.get(0).getY();
                    for (int i = 0; i < 4; i++) {
                        temp = v.get(i);
                        if (maxval < temp.getY()) {
                            max = temp;
                            maxval = temp.getY();
                        }
                        if (maxval == temp.getY()) {
                            max2 = temp;
                        }
                    }
                    max.U = true;
                    if (max2 != null) max2.U = true;
                    max = v.get(0);
                    max2 = null;
                    maxval = v.get(0).getX();
                    for (int i = 0; i < 4; i++) {
                        temp = v.get(i);
                        if (maxval < temp.getX()) {
                            max = temp;
                            maxval = temp.getX();
                        }
                        if (maxval == temp.getX()) {
                            max2 = temp;
                        }
                    }
                    max.D = true;
                    if (max2 != null) max2.D = true;
                }
                {
                    Point min = v.get(0);
                    Point min2 = null;
                    float minval = v.get(0).getY();
                    for (int i = 0; i < 4; i++) {
                        temp = v.get(i);
                        if (minval > temp.getY()) {
                            min = temp;
                            minval = temp.getY();
                        }
                        if (minval == temp.getY()) {
                            min2 = temp;
                        }
                    }
                    min.U = true;
                    if (min2 != null) min2.U = true;
                    min = v.get(0);
                    min2 = null;
                    minval = v.get(0).getX();
                    for (int i = 0; i < 4; i++) {
                        temp = v.get(i);
                        if (minval > temp.getX()) {
                            min = temp;
                            minval = temp.getX();
                        }
                        if (minval == temp.getX()) {
                            min2 = temp;
                        }
                    }
                    min.D = true;
                    if (min2 != null) min2.D = true;
                }
            }

            public void createSegments(Point b, Point c, Point d) {
                Segment temp;
                Segment rette[] = new Segment[4];
                Point points[][] = new Point[4][2];
                temp = new Segment(this, b);
                this.segments[0] = temp;
                points[0][0] = this;
                b.segments[0] = temp;
                points[0][1] = b;
                rette[0] = temp;
                temp = new Segment(this, c);
                this.segments[1] = temp;
                points[1][0] = this;
                c.segments[0] = temp;
                points[1][1] = c;
                rette[1] = temp;
                temp = new Segment(b, d);
                b.segments[1] = temp;
                points[2][0] = b;
                d.segments[0] = temp;
                points[2][1] = d;
                rette[2] = temp;
                temp = new Segment(c, d);
                c.segments[1] = temp;
                points[3][0] = c;
                d.segments[1] = temp;
                points[3][1] = d;
                rette[3] = temp;
                Point A, B;
                for (int i = 0; i < 4; i++) {
                    A = points[i][0];
                    B = points[i][1];
                    if ((rette[i].U = (A.U && B.U)) || (rette[i].S = (A.S && B.S)) || (rette[i].B = (A.B && B.B)) || (rette[i].D = (A.D && B.D))) {
                        continue;
                    }
                    rette[i].U = A.U || B.U;
                    rette[i].S = A.S || B.S;
                    rette[i].B = A.B || B.B;
                    rette[i].D = A.D || B.D;
                }
            }
        }
        alpha = -alpha;
        beta = -beta;
        alpha *= rad;
        beta *= rad;
        final float xh = home.getX();
        final float yh = home.getY();
        final float xd = dest.getX();
        final float yd = dest.getY();
        final float widthh = home.getWidth();
        final float heighth = home.getHeight();
        final float widthd = dest.getWidth();
        final float heightd = dest.getHeight();
        final float centerxh = (xh + widthh) / 2;
        final float centeryh = (yh + heighth) / 2;
        final float centerxd = (xd + widthd) / 2;
        final float centeryd = (yd + heightd) / 2;
        final double cosalpha = Math.cos(alpha);
        final double sinalpha = Math.sin(alpha);
        final double cosbeta = Math.cos(beta);
        final double sinbeta = Math.sin(beta);
        Point hp1 = new Point(xh - centerxh, xh - centeryh);
        Point hp2 = new Point(xh + widthh - centerxh, yh - centeryh);
        Point hp3 = new Point(xh - centerxh, yh + heighth - centeryh);
        Point hp4 = new Point(xh + widthh - centerxh, yh + heighth - centeryh);
        Point dp1 = new Point(xd - centerxd, yd - centeryd);
        Point dp2 = new Point(xd + widthd - centerxd, yd - centeryd);
        Point dp3 = new Point(xd - centerxd, yd + heightd - centeryd);
        Point dp4 = new Point(xd + widthd - centerxd, yd + heightd - centeryd);
        hp1.rotate(cosalpha, sinalpha);
        hp2.rotate(cosalpha, sinalpha);
        hp3.rotate(cosalpha, sinalpha);
        hp4.rotate(cosalpha, sinalpha);
        dp1.rotate(cosbeta, sinbeta);
        dp2.rotate(cosbeta, sinbeta);
        dp3.rotate(cosbeta, sinbeta);
        dp4.rotate(cosbeta, sinbeta);
        hp1.USBD(hp2, hp3, hp4);
        dp1.USBD(dp2, dp3, dp4);
        hp1.createSegments(hp2, hp3, hp4);
        dp1.createSegments(dp2, dp3, dp4);
        return false;
    }
