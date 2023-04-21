    public synchronized void docompute(final ZirkelCanvas zc) {
        V = new Vector();
        for (int i = 0; i < PN; i++) {
            VO[i] = new Vector();
        }
        VPM = new Vector();
        if (O instanceof PrimitiveCircleObject) {
            zc.getConstruction().shouldSwitch(false);
            final PrimitiveCircleObject c = (PrimitiveCircleObject) O;
            final double x = c.getX(), y = c.getY(), r = c.getR();
            PM.project(c);
            double amin = 0, amax = 0, astart = 0, anull = 0;
            final double dmax = 0.5;
            boolean range = false;
            if (c.hasRange()) {
                range = true;
                final double a1 = c.getA1();
                final double a2 = c.getA2();
                double d = a2 - a1;
                while (d < 0) {
                    d += 2 * Math.PI;
                }
                while (d >= 2 * Math.PI) {
                    d -= 2 * Math.PI;
                }
                amin = astart = -d / 2;
                amax = d / 2;
                anull = (a1 + a2) / 2;
            } else {
                amin = astart = -Math.PI * 0.9999;
                amax = Math.PI * 0.9999;
            }
            double a = astart;
            PM.move(x + r * Math.cos(anull + a), y + r * Math.sin(anull + a));
            PM.project(c);
            zc.getConstruction().validate(P, PM);
            zc.resetSum();
            double x1 = 0, y1 = 0;
            boolean started = false;
            if (P.valid()) {
                zc.getConstruction().shouldSwitch(true);
                if (P instanceof PointObject) {
                    final PointObject p = (PointObject) P;
                    x1 = p.getX();
                    y1 = p.getY();
                    V.addElement(new Coordinates(x1, y1));
                    VPM.addElement(new Coordinates(PM.getX(), PM.getY()));
                    started = true;
                } else if (P instanceof PrimitiveLineObject) {
                    final PrimitiveLineObject L = (PrimitiveLineObject) P;
                    X = L.getX();
                    Y = L.getY();
                    DX = L.getDX();
                    DY = L.getDY();
                    started = true;
                }
            }
            final boolean startedO[] = new boolean[PMax];
            for (int i = 0; i < PN; i++) {
                startedO[i] = false;
            }
            final long time = System.currentTimeMillis();
            addSecondary(startedO, zc);
            final double dmin = 0.001;
            if (da < 1e-10 || da > zc.dx(1)) {
                da = zc.dx(1) / 10;
            }
            double aold = a;
            double x2 = 0, y2 = 0;
            while (true) {
                a = a + da;
                boolean Break = false;
                if ((!started || range) && a >= amax) {
                    a = amax;
                    Break = true;
                } else if ((!started || range) && a <= amin) {
                    a = amin;
                    Break = true;
                } else if (started && da > 0) {
                    if ((mod(aold - astart) < 0 && mod(a - astart) >= 0) && !zc.getConstruction().haveSwitched()) {
                        Break = true;
                        a = astart;
                    }
                }
                aold = a;
                PM.move(x + r * Math.cos(anull + a), y + r * Math.sin(anull + a));
                PM.project(c);
                zc.getConstruction().validate(P, PM);
                if (P.valid()) {
                    if (!started) {
                        zc.getConstruction().shouldSwitch(true);
                        astart = a;
                    }
                    boolean valid = false;
                    if (P instanceof PointObject) {
                        final PointObject p = (PointObject) P;
                        x2 = p.getX();
                        y2 = p.getY();
                        valid = true;
                    } else if (P instanceof PrimitiveLineObject) {
                        final PrimitiveLineObject L = (PrimitiveLineObject) P;
                        if (!started) {
                            X = L.getX();
                            Y = L.getY();
                            DX = L.getDX();
                            DY = L.getDY();
                        } else {
                            double xx, yy, dx, dy;
                            xx = L.getX();
                            yy = L.getY();
                            dx = L.getDX();
                            dy = L.getDY();
                            final double det = dx * DY - dy * DX;
                            if (Math.sqrt(Math.abs(det)) > 1e-9) {
                                final double h = (-(X - xx) * DY + DX * (Y - yy)) / (-det);
                                x2 = xx + h * dx;
                                y2 = yy + h * dy;
                                valid = true;
                            }
                            X = xx;
                            Y = yy;
                            DX = dx;
                            DY = dy;
                        }
                    }
                    final double dist = zc.dCenter(x2, y2);
                    final boolean different = ((int) zc.col(x1) != (int) zc.col(x2) || (int) zc.row(y1) != (int) zc.row(y2));
                    if (valid && different) {
                        V.addElement(new Coordinates(x2, y2));
                        VPM.addElement(new Coordinates(PM.getX(), PM.getY()));
                    }
                    final double dp = Math.abs(x2 - x1) + Math.abs(y2 - y1);
                    da = updateDA(da, valid, dist, dp, dmin, dmax, zc);
                    x1 = x2;
                    y1 = y2;
                    started = true;
                } else if (started) {
                    V.addElement(new Coordinates(x2, y2));
                    VPM.addElement(new Coordinates(PM.getX(), PM.getY()));
                    da = -da;
                }
                addSecondary(startedO, zc);
                if (Break || System.currentTimeMillis() - time > 5000) {
                    break;
                }
            }
        } else if (O instanceof PrimitiveLineObject) {
            zc.getConstruction().shouldSwitch(false);
            final PrimitiveLineObject l = (PrimitiveLineObject) O;
            PM.project(l);
            final double lx = l.getX(), ly = l.getY(), ldx = l.getDX(), ldy = l.getDY();
            double amin = 0, amax = 0, astart = 0;
            double dmax = 0.5;
            boolean range = false;
            if (l instanceof RayObject) {
                amin = astart = 0;
                amax = Math.PI * 0.9999;
                range = true;
            } else if (l instanceof SegmentObject) {
                amin = astart = 0;
                final double r = ((SegmentObject) l).getLength();
                System.out.println(r);
                dmax = r / 20;
                amax = Math.atan(r) * 2;
                range = true;
            } else {
                amin = astart = -Math.PI * 0.99999;
                amax = Math.PI * 0.9999;
            }
            double a = astart;
            double hd = Math.tan(mod(a) / 2);
            PM.move(lx + hd * ldx, ly + hd * ldy);
            PM.project(l);
            zc.getConstruction().validate(P, PM);
            zc.resetSum();
            double x1 = 0, y1 = 0;
            boolean started = false;
            if (P.valid()) {
                zc.getConstruction().shouldSwitch(true);
                if (P instanceof PointObject) {
                    final PointObject p = (PointObject) P;
                    x1 = p.getX();
                    y1 = p.getY();
                    V.addElement(new Coordinates(x1, y1));
                    VPM.addElement(new Coordinates(PM.getX(), PM.getY()));
                    started = true;
                } else if (P instanceof PrimitiveLineObject) {
                    final PrimitiveLineObject L = (PrimitiveLineObject) P;
                    X = L.getX();
                    Y = L.getY();
                    DX = L.getDX();
                    DY = L.getDY();
                    started = true;
                }
            }
            final boolean startedO[] = new boolean[PMax];
            for (int i = 0; i < PN; i++) {
                startedO[i] = false;
            }
            final long time = System.currentTimeMillis();
            addSecondary(startedO, zc);
            final double dmin = 0.001;
            if (da < 1e-10 || da > zc.dx(1)) {
                da = zc.dx(1) / 10;
            }
            double aold = a;
            double x2 = 0, y2 = 0;
            while (true) {
                a = a + da;
                boolean Break = false;
                if ((!started || range) && a >= amax) {
                    a = amax;
                    Break = true;
                } else if ((!started || range) && a <= amin) {
                    a = amin;
                    Break = true;
                } else if (started && da > 0) {
                    if ((mod(aold - astart) < 0 && mod(a - astart) >= 0) && !zc.getConstruction().haveSwitched()) {
                        Break = true;
                        a = astart;
                    }
                }
                aold = a;
                hd = Math.tan(mod(a) / 2);
                PM.move(lx + hd * ldx, ly + hd * ldy);
                PM.project(l);
                zc.getConstruction().validate(P, PM);
                if (P.valid()) {
                    if (!started) {
                        zc.getConstruction().shouldSwitch(true);
                        astart = a;
                    }
                    boolean valid = false;
                    if (P instanceof PointObject) {
                        final PointObject p = (PointObject) P;
                        x2 = p.getX();
                        y2 = p.getY();
                        valid = true;
                    } else if (P instanceof PrimitiveLineObject) {
                        final PrimitiveLineObject L = (PrimitiveLineObject) P;
                        if (!started) {
                            X = L.getX();
                            Y = L.getY();
                            DX = L.getDX();
                            DY = L.getDY();
                        } else {
                            double xx, yy, dx, dy;
                            xx = L.getX();
                            yy = L.getY();
                            dx = L.getDX();
                            dy = L.getDY();
                            final double det = dx * DY - dy * DX;
                            if (Math.sqrt(Math.abs(det)) > 1e-9) {
                                final double h = (-(X - xx) * DY + DX * (Y - yy)) / (-det);
                                x2 = xx + h * dx;
                                y2 = yy + h * dy;
                                valid = true;
                            }
                            X = xx;
                            Y = yy;
                            DX = dx;
                            DY = dy;
                        }
                    }
                    final double dist = zc.dCenter(x2, y2);
                    final boolean different = ((int) zc.col(x1) != (int) zc.col(x2) || (int) zc.row(y1) != (int) zc.row(y2));
                    if (valid && different) {
                        V.addElement(new Coordinates(x2, y2));
                        VPM.addElement(new Coordinates(PM.getX(), PM.getY()));
                    }
                    final double dp = Math.abs(x2 - x1) + Math.abs(y2 - y1);
                    da = updateDA(da, valid, dist, dp, dmin, dmax, zc);
                    x1 = x2;
                    y1 = y2;
                    started = true;
                } else if (started) {
                    V.addElement(new Coordinates(x2, y2));
                    VPM.addElement(new Coordinates(PM.getX(), PM.getY()));
                    da = -da;
                }
                addSecondary(startedO, zc);
                if (Break || System.currentTimeMillis() - time > 5000) {
                    break;
                }
            }
        } else if (O instanceof ExpressionObject) {
            zc.getConstruction().shouldSwitch(false);
            final ExpressionObject eo = (ExpressionObject) O;
            if (!eo.isSlider()) {
                return;
            }
            double amin = 0, amax = 0, astart = 0;
            double dmax = 0.5;
            boolean range = false;
            amin = astart = 0;
            final double r = 1;
            dmax = r / 20;
            amax = Math.atan(r) * 2;
            range = true;
            double a = astart;
            double hd = Math.tan(mod(a) / 2);
            eo.setSliderPosition(0);
            zc.getConstruction().validate(P, null);
            zc.resetSum();
            double x1 = 0, y1 = 0;
            boolean started = false;
            if (P.valid()) {
                zc.getConstruction().shouldSwitch(true);
                if (P instanceof PointObject) {
                    final PointObject p = (PointObject) P;
                    x1 = p.getX();
                    y1 = p.getY();
                    V.addElement(new Coordinates(x1, y1));
                    VPM.addElement(new Coordinates(eo.getSliderPosition(), 0));
                    started = true;
                } else if (P instanceof PrimitiveLineObject) {
                    final PrimitiveLineObject L = (PrimitiveLineObject) P;
                    X = L.getX();
                    Y = L.getY();
                    DX = L.getDX();
                    DY = L.getDY();
                    started = true;
                }
            }
            final boolean startedO[] = new boolean[PMax];
            for (int i = 0; i < PN; i++) {
                startedO[i] = false;
            }
            final long time = System.currentTimeMillis();
            addSecondary(startedO, zc);
            final double dmin = 0.001;
            if (da < 1e-10 || da > zc.dx(1)) {
                da = zc.dx(1) / 10;
            }
            double aold = a;
            double x2 = 0, y2 = 0;
            while (true) {
                a = a + da;
                boolean Break = false;
                if ((!started || range) && a >= amax) {
                    a = amax;
                    Break = true;
                } else if ((!started || range) && a <= amin) {
                    a = amin;
                    Break = true;
                } else if (started && da > 0) {
                    if ((mod(aold - astart) < 0 && mod(a - astart) >= 0) && !zc.getConstruction().haveSwitched()) {
                        Break = true;
                        a = astart;
                    }
                }
                aold = a;
                hd = Math.tan(mod(a) / 2);
                eo.setSliderPosition(hd);
                zc.getConstruction().validate(P, null);
                if (P.valid()) {
                    if (!started) {
                        zc.getConstruction().shouldSwitch(true);
                        astart = a;
                    }
                    boolean valid = false;
                    if (P instanceof PointObject) {
                        final PointObject p = (PointObject) P;
                        x2 = p.getX();
                        y2 = p.getY();
                        valid = true;
                    } else if (P instanceof PrimitiveLineObject) {
                        final PrimitiveLineObject L = (PrimitiveLineObject) P;
                        if (!started) {
                            X = L.getX();
                            Y = L.getY();
                            DX = L.getDX();
                            DY = L.getDY();
                        } else {
                            double xx, yy, dx, dy;
                            xx = L.getX();
                            yy = L.getY();
                            dx = L.getDX();
                            dy = L.getDY();
                            final double det = dx * DY - dy * DX;
                            if (Math.sqrt(Math.abs(det)) > 1e-9) {
                                final double h = (-(X - xx) * DY + DX * (Y - yy)) / (-det);
                                x2 = xx + h * dx;
                                y2 = yy + h * dy;
                                valid = true;
                            }
                            X = xx;
                            Y = yy;
                            DX = dx;
                            DY = dy;
                        }
                    }
                    final double dist = zc.dCenter(x2, y2);
                    final boolean different = ((int) zc.col(x1) != (int) zc.col(x2) || (int) zc.row(y1) != (int) zc.row(y2));
                    if (valid && different) {
                        V.addElement(new Coordinates(x2, y2));
                        VPM.addElement(new Coordinates(eo.getSliderPosition(), 0));
                    }
                    final double dp = Math.abs(x2 - x1) + Math.abs(y2 - y1);
                    da = updateDA(da, valid, dist, dp, dmin, dmax, zc);
                    x1 = x2;
                    y1 = y2;
                    started = true;
                } else if (started) {
                    V.addElement(new Coordinates(x2, y2));
                    VPM.addElement(new Coordinates(eo.getSliderPosition(), 0));
                    da = -da;
                }
                addSecondary(startedO, zc);
                if (Break || System.currentTimeMillis() - time > 5000) {
                    break;
                }
            }
        }
    }
