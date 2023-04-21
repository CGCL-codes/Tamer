    protected synchronized void drawScale(final double r, final Point2D cp, Graphics2D g2) {
        Point2D p0, p1, p2, p3, p4, p5, p6, p7, p8, pc;
        Polygon pol;
        Ellipse2D ell;
        Rectangle2D rect;
        Shape oldClip = g2.getClip();
        Font oldFont = g2.getFont();
        Point2D aicp = getPoint(cp, r * .325, 90);
        Point2D hsicp = new Point2D.Double(cp.getX(), cp.getY() + r * .45);
        double pf = 45;
        double pv = pitch / pf;
        p1 = new Point2D.Double(cp.getX() - r * 1.4, cp.getY() - r * 0.85);
        Rectangle2D rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * 2.8, r * 1.7);
        g2.setClip(rr);
        Angle a = new Angle(-1 * bank);
        p1 = getPoint(aicp, r * pv, a.getDegreesPlus(90));
        p1 = getPoint(p1, r * 1.8, a.getDegrees());
        p2 = getPoint(p1, r * 3.6, a.getDegreesPlus(180));
        p3 = getPoint(p2, r * 180 / pf, a.getDegreesPlus(90));
        p4 = getPoint(p1, r * 180 / pf, a.getDegreesPlus(90));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        g2.setColor(skyColorAI);
        g2.fill(pol);
        g2.setColor(new Color(181, 183, 189));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        g2.draw(new Line2D.Double(p1, p2));
        ell = new Ellipse2D.Double(aicp.getX() - r * .35, aicp.getY() - r * .35, r * .7, r * .7);
        g2.setClip(ell);
        g2.setColor(clrWhite);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double majorFontSize = Math.max(6.0, r * .05);
        Font font = oldFont.deriveFont(Font.PLAIN, (float) majorFontSize);
        g2.setFont(font);
        Angle currentAngle = new Angle(-1 * bank);
        TextLayout stl;
        Shape ts;
        AffineTransform tat = new AffineTransform();
        for (int i = -80; i <= 80; i += 10) {
            if (i == 0) continue;
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .15, a.getDegrees());
            p2 = getPoint(p1, r * .3, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
            String s = valueToString(i);
            currentAngle = new Angle(-1 * bank);
            stl = new TextLayout(s, new Font("Helvetica", 1, (int) Math.round(r * 0.05)), new FontRenderContext(null, true, false));
            tat = new AffineTransform();
            currentAngle = new Angle(360 - currentAngle.getDegrees());
            rect = stl.getBounds();
            tat.translate(aicp.getX() - rect.getWidth() / 2, aicp.getY() + stl.getAscent() / 2.5);
            tat.rotate(Math.toRadians(currentAngle.getDegrees()), rect.getCenterX(), rect.getCenterX());
            tat.translate(r * .2, -r * (pitch + i) / pf);
            ts = stl.getOutline(tat);
            g2.fill(ts);
            tat.translate(-r * .4, 0);
            ts = stl.getOutline(tat);
            g2.fill(ts);
        }
        for (int i = -35; i < 40; i += 10) {
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .09, a.getDegrees());
            p2 = getPoint(p1, r * .18, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
        }
        for (float i = -17.5f; i < 20; i += 5) {
            p1 = getPoint(aicp, r * (pitch + i) / pf, a.getDegreesPlus(90));
            p1 = getPoint(p1, r * .04, a.getDegrees());
            p2 = getPoint(p1, r * .08, a.getDegreesPlus(180));
            g2.draw(new Line2D.Double(p1, p2));
        }
        g2.setClip(rr);
        a = new Angle(0);
        p1 = getPoint(aicp, r * .41, a.getDegreesPlus(90));
        p2 = getPoint(p1, r * .055, a.getDegreesPlus(60));
        p3 = getPoint(p1, r * .055, a.getDegreesPlus(120));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setColor(clrWhite);
        g2.fill(pol);
        a = new Angle(-bank);
        p1 = getPoint(aicp, r * .39, a.getDegreesPlus(90));
        p2 = getPoint(p1, r * .055, a.getDegreesPlus(240));
        p3 = getPoint(p1, r * .055, a.getDegreesPlus(300));
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        g2.setColor(clrWhite);
        g2.fill(pol);
        double ball = (Double) ballVar.getValue();
        Angle angleCurrent = new Angle(-bank + 90);
        p1 = getPoint(aicp, r * .325, angleCurrent.getDegrees());
        p2 = getPoint(p1, -ball / 1024 * r, angleCurrent.plus(90));
        p3 = getPoint(p2, r * .03, angleCurrent.plus(90));
        p4 = getPoint(p2, r * .03, angleCurrent.plus(270));
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.01)));
        g2.draw(new Line2D.Double(p3, p4));
        g2.setColor(clrWhite);
        Point2D ap, np;
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double f = .75;
        for (int a1 = -60; a1 <= 60; a1 += 10) {
            if (a1 == 0 || Math.abs(a1) == 80 || Math.abs(a1) == 70 || Math.abs(a1) == 40 || Math.abs(a1) == 50) continue;
            f = Math.abs(a1) == 10 || Math.abs(a1) == 20 ? .44 : .46;
            angleCurrent = new Angle(a1 + 90);
            ap = getPoint(aicp, r * .41, angleCurrent.getDegrees());
            np = getPoint(aicp, r * f, angleCurrent.getDegrees());
            g2.draw(new Line2D.Double(ap, np));
        }
        g2.setClip(oldClip);
        ap = getPoint(aicp, r * .41, 135);
        np = getPoint(aicp, r * .44, 135);
        g2.draw(new Line2D.Double(ap, np));
        ap = getPoint(aicp, r * .41, 45);
        np = getPoint(aicp, r * .44, 45);
        g2.draw(new Line2D.Double(ap, np));
        Arc2D arc = new Arc2D.Double(new Rectangle2D.Double(aicp.getX() - r * .41, aicp.getY() - r * .41, r * .82, r * .82), 30, 120, Arc2D.OPEN);
        g2.draw(arc);
        rect = new Rectangle2D.Double(aicp.getX() - r * .015, aicp.getY() - r * .015, r * .03, r * .03);
        g2.setColor(clrBlack);
        g2.fill(rect);
        g2.setColor(clrWhite);
        g2.draw(rect);
        p1 = new Point2D.Double(aicp.getX() - r * .16, aicp.getY() - r * .015);
        p2 = new Point2D.Double(aicp.getX() - r * .36, aicp.getY() - r * .015);
        p3 = new Point2D.Double(aicp.getX() - r * .36, aicp.getY() + r * .01);
        p4 = new Point2D.Double(aicp.getX() - r * .19, aicp.getY() + r * .01);
        p5 = new Point2D.Double(aicp.getX() - r * .19, aicp.getY() + r * .030);
        p6 = new Point2D.Double(aicp.getX() - r * .16, aicp.getY() + r * .030);
        p7 = new Point2D.Double(aicp.getX() - r * .16, aicp.getY() - r * .015);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        g2.setColor(clrBlack);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        p1 = new Point2D.Double(aicp.getX() + r * .16, aicp.getY() - r * .015);
        p2 = new Point2D.Double(aicp.getX() + r * .36, aicp.getY() - r * .015);
        p3 = new Point2D.Double(aicp.getX() + r * .36, aicp.getY() + r * .01);
        p4 = new Point2D.Double(aicp.getX() + r * .19, aicp.getY() + r * .01);
        p5 = new Point2D.Double(aicp.getX() + r * .19, aicp.getY() + r * .030);
        p6 = new Point2D.Double(aicp.getX() + r * .16, aicp.getY() + r * .030);
        p7 = new Point2D.Double(aicp.getX() + r * .16, aicp.getY() - r * .015);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        g2.setColor(clrBlack);
        g2.fill(pol);
        g2.setColor(clrWhite);
        g2.draw(pol);
        double heightfactor = .9;
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        double width = r * .035;
        double dy = heightfactor / 120;
        double fps_kts = 0.592483801;
        double ys = aicp.getY() + r * ias * dy;
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(aicp.getX() - r * .65 - r * .25, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.setClip(rect);
        double h0 = (Double) vs0speed.getValue() * r * fps_kts * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .675, ys - h0);
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, h0);
        g2.setColor(new Color(165, 55, 55));
        g2.fill(rect);
        double h1 = (Double) vs1speed.getValue() * r * fps_kts * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .675, ys - (h0 + h1));
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, h1);
        g2.setColor(new Color(111, 111, 111));
        g2.fill(rect);
        double mach_kts = 666.74;
        double hg = (Double) maxmach.getValue() * mach_kts * r * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .675, ys - (h0 + h1 + hg));
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, hg);
        g2.setColor(new Color(88, 148, 88));
        g2.fill(rect);
        g2.setClip(oldClip);
        double hm = (Double) asbarberpole.getValue() * r * dy;
        p1 = new Point2D.Double(aicp.getX() - r * .695, ys - (h0 + h1 + hg + hm));
        rect = new Rectangle2D.Double(p1.getX(), p1.getY(), width, hm);
        g2.setColor(new Color(165, 55, 55));
        g2.fill(rect);
        g2.setClip(oldClip);
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(aicp.getX() - r * .65 - r * .25, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.draw(rect);
        g2.setClip(rect);
        p1 = new Point2D.Double(aicp.getX() - r * .65, aicp.getY() - r * .35);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * .9));
        for (int i = (int) Math.floor(ias / 10) * 10 - 70; i <= Math.floor(ias / 10) * 10 + 70; i += 10) {
            p1 = new Point2D.Double(aicp.getX() - r * .65, ys - r * dy * i);
            p2 = new Point2D.Double(aicp.getX() - r * (i % 20 == 0 ? .69 : .675), ys - r * dy * i);
            if (i < 0) continue;
            g2.draw(new Line2D.Double(p1, p2));
            if (i % 20 == 0) {
                p3 = new Point2D.Double(aicp.getX() - r * .7, aicp.getY() + r * .045 + r * ias * dy - r * dy * i);
                drawTextRight(r, p3, g2, .6, "" + i);
            }
        }
        g2.setClip(oldClip);
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.draw(rect);
        g2.setClip(rect);
        p1 = new Point2D.Double(cp.getX() + r * .88, cp.getY() - r * .75);
        drawTextRight(r, p1, g2, .6, "" + Math.round(salt));
        dy = heightfactor / 800;
        double das = salt - ialt;
        das = das < -400 ? -400 : das;
        das = das > 400 ? 400 : das;
        ys = cp.getY() - r * .3 - r * das * dy;
        p1 = new Point2D.Double(aicp.getX() + r * .66, ys);
        p2 = getPoint(p1, r * .03, 45);
        p3 = getPoint(p2, r * .02, 90);
        p4 = getPoint(p3, r * .05, 180);
        p5 = getPoint(p4, r * .082, 270);
        p6 = getPoint(p5, r * .05, 0);
        p7 = getPoint(p6, r * .02, 90);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        pol.addPoint((int) Math.round(p6.getX()), (int) Math.round(p6.getY()));
        pol.addPoint((int) Math.round(p7.getX()), (int) Math.round(p7.getY()));
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        g2.setColor(new Color(159, 186, 195));
        g2.fill(pol);
        g2.setClip(oldClip);
        pc = new Point2D.Double(cp.getX(), cp.getY() + r * .45);
        g2.setColor(new Color(65, 69, 55));
        ell = new Ellipse2D.Double((int) pc.getX() - r * .35, (int) pc.getY() - r * .35, (int) (r * .7), (int) (r * .7));
        g2.fill(ell);
        double r0 = proz(r, 30);
        double r1 = proz(r, 32);
        double majorTickSize = proz(r, 3);
        double minorTickSize = proz(r, 2);
        double majorTickR = r1 + 2.5;
        double minorTickR = majorTickR + (majorTickSize - minorTickSize) / 2.0;
        double minValue = 0;
        double maxValue = 359;
        double minorTickStep = 5;
        double majorTickStep = 30;
        double angleStart = 90;
        double angleExtend = 360;
        if (minorTickStep > 0) {
            g2.setColor(clrWhite);
            g2.setStroke(new BasicStroke(1.f));
            double stepAngle = angleExtend * minorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += minorTickStep) {
                currentAngle = new Angle(angleStart + angleExtend - stepAngle * (v - hi - minValue) / minorTickStep);
                p0 = getPoint(pc, minorTickR, currentAngle.getDegrees());
                p1 = getPoint(pc, minorTickR + minorTickSize, currentAngle.getDegrees());
                g2.draw(new Line2D.Double(p0, p1));
            }
        }
        if (majorTickStep > 0) {
            double l = valueToString(maxValue).length();
            if (l > 3 && decPlaceCount > 0) {
                l--;
            }
            f = l < 4 ? 1.0 : 1.0 - 0.1 * (l - 3);
            majorFontSize = Math.max(6.0, r * .05 * f);
            g2.setColor(clrWhite);
            g2.setStroke(new BasicStroke((int) (r * 0.0075)));
            double stepAngle = angleExtend * majorTickStep / (maxValue - minValue);
            for (double v = minValue; v <= maxValue; v += majorTickStep) {
                currentAngle = new Angle(90 - angleStart + angleExtend - stepAngle * (v - hi - minValue) / majorTickStep);
                p0 = getPoint(pc, majorTickR, currentAngle.getDegrees());
                p1 = getPoint(pc, majorTickR + majorTickSize, currentAngle.getDegrees());
                g2.draw(new Line2D.Double(p0, p1));
                String s = valueToString(v / 10);
                switch((int) (v / 10)) {
                    case 0:
                        s = "N";
                        break;
                    case 9:
                        s = "E";
                        break;
                    case 18:
                        s = "S";
                        break;
                    case 27:
                        s = "W";
                        break;
                    default:
                        break;
                }
                stl = new TextLayout(s, new Font("Helvetica", 1, (int) Math.round(r * 0.05)), new FontRenderContext(null, true, false));
                tat = new AffineTransform();
                currentAngle = new Angle(360 - currentAngle.getDegrees() - 135);
                rect = stl.getBounds();
                tat.translate(pc.getX() - rect.getWidth() / 2, pc.getY() - stl.getAscent() / 2);
                tat.rotate(currentAngle.getRadians(), rect.getCenterX(), rect.getCenterX());
                tat.translate(r * .17, r * .17);
                tat.rotate(Math.toRadians(135), rect.getCenterX(), rect.getCenterX());
                ts = stl.getOutline(tat);
                g2.fill(ts);
            }
        }
        g2.setClip(oldClip);
        g2.setStroke(new BasicStroke((int) (r * 0.005)));
        a = new Angle(0);
        p1 = getPoint(cp, r * 0.105, 270);
        p2 = getPoint(p1, r * .05, 60);
        p3 = getPoint(p1, r * .05, 120);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        g2.setColor(clrWhite);
        g2.fill(pol);
        double vsi = vs > 2000 ? 2000 : vs;
        vsi = vsi < -2000 ? -2000 : vsi;
        double vsf = (.225 / 1.5) * (vsi / 1000);
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY());
        if (vsi > 0) {
            rect = new Rectangle2D.Double(p1.getX(), p1.getY() - vsf * r, r * .02, vsf * r);
        } else {
            rect = new Rectangle2D.Double(p1.getX(), p1.getY() * r, r * .02, -vsf * r);
        }
        g2.setColor(new Color(142, 135, 151));
        g2.fill(rect);
        p1 = new Point2D.Double(aicp.getX() + r * .65, aicp.getY() - r * .4);
        rect = new Rectangle2D.Double((int) p1.getX(), (int) p1.getY(), (int) (r * .25), (int) (r * heightfactor));
        g2.setClip(rect);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke((int) Math.round(r * 0.005)));
        dy = .9 / 800;
        ys = aicp.getY() + r * ialt * dy;
        for (int i = (int) Math.floor(ialt / 100) * 100 - 400; i <= Math.floor(ialt / 100) * 100 + 400; i += 100) {
            p1 = new Point2D.Double(aicp.getX() + r * .65, ys - r * dy * i);
            p2 = new Point2D.Double(aicp.getX() + r * .69, ys - r * dy * i);
            if (i < -1000) continue;
            g2.draw(new Line2D.Double(p1, p2));
            if (i % 200 == 0) {
                p3 = new Point2D.Double(aicp.getX() + r * .77, aicp.getY() + r * .045 + r * ialt * dy - r * dy * i);
                drawText(r, p3, g2, .5, "" + i);
            }
        }
        g2.setClip(oldClip);
        g2.setColor(Color.LIGHT_GRAY);
        p1 = getPoint(aicp, r * 0.9, 0);
        p2 = getPoint(p1, r * .1, 25);
        p3 = getPoint(p1, r * .1, 335);
        drawLine(p1, p2, g2);
        drawLine(p1, p3, g2);
        p4 = getPoint(p2, r * .3, 90);
        p5 = getPoint(p3, r * .3, 270);
        drawLine(p2, p4, g2);
        drawLine(p3, p5, g2);
        p2 = getPoint(p4, r * .09, 180);
        p3 = getPoint(p5, r * .09, 180);
        drawLine(p2, p4, g2);
        drawLine(p3, p5, g2);
        double h = .225 / 3 * r;
        int len;
        for (int i = 0; i <= 4; i++) {
            if (i == 0) continue;
            if (i % 2 == 0) {
                len = (int) Math.round(r * .04);
                p1 = new Point2D.Double(aicp.getX() + r * .95, aicp.getY() - i * h + r * .0375);
                drawTextLeft(r, p1, g2, .5, "" + (i / 2));
                p1 = new Point2D.Double(aicp.getX() + r * .95, aicp.getY() + i * h + r * .0375);
                drawTextLeft(r, p1, g2, .5, "" + (i / 2));
            } else {
                len = (int) Math.round(r * .02);
            }
            p1 = new Point2D.Double(aicp.getX() + r * .9, aicp.getY() - i * h);
            p2 = new Point2D.Double(aicp.getX() + r * .9 + len, aicp.getY() - i * h);
            drawLine(p1, p2, g2);
            p1 = new Point2D.Double(aicp.getX() + r * .9, aicp.getY() + i * h);
            p2 = new Point2D.Double(aicp.getX() + r * .9 + len, aicp.getY() + i * h);
            drawLine(p1, p2, g2);
        }
        p1 = new Point2D.Double(aicp.getX() + r * .9, aicp.getY() - vsf * r);
        p2 = getPoint(p1, r * .07, 25);
        p3 = getPoint(p2, r * .12, 0);
        p4 = getPoint(p3, r * .059, 270);
        p5 = getPoint(p4, r * .12, 180);
        pol = new Polygon();
        pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
        pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
        pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
        pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
        pol.addPoint((int) Math.round(p5.getX()), (int) Math.round(p5.getY()));
        g2.setColor(Color.black);
        g2.fill(pol);
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(pol);
        p1 = new Point2D.Double(aicp.getX() + r * 1.08, aicp.getY() - vsf * r + r * .04);
        String s = Math.round(vs * 2 / 100) * 10 / 2 + "0";
        if (s.equals("00")) s = "";
        drawTextRight(r, p1, g2, .5, s);
        if (gsf == 1.0) {
            g2.setColor(Color.LIGHT_GRAY);
            h = r * .225;
            p1 = new Point2D.Double(aicp.getX() + r * .645, aicp.getY() - h);
            p2 = getPoint(p1, r * .075, 180);
            drawLine(p1, p2, g2);
            p3 = getPoint(p2, 2 * h, 270);
            drawLine(p2, p3, g2);
            p4 = getPoint(p3, r * .075, 0);
            drawLine(p3, p4, g2);
            for (int i = -2; i <= 2; i++) {
                if (i == 0) continue;
                p1 = new Point2D.Double(aicp.getX() + r * .595, aicp.getY() - r * .014 - i * h / 2.5);
                ell = new Ellipse2D.Double(p1.getX(), p1.getY(), r * .027, r * .028);
                g2.draw(ell);
            }
            p1 = new Point2D.Double(aicp.getX() + r * .645, aicp.getY());
            p2 = getPoint(p1, r * .075, 180);
            drawLine(p1, p2, g2);
            double gsi = gsa > 1 ? 1 : gsa;
            gsi = gsi < -1 ? -1 : gsi;
            double gsy = r * (.225 / 2.5) * gsi * 2;
            p1 = new Point2D.Double(aicp.getX() + r * .595 + r * .042, aicp.getY() + gsy);
            p2 = getPoint(p1, r * .04, 135);
            p3 = getPoint(p2, r * .04, 135 + 90);
            p4 = getPoint(p3, r * .04, 135 + 180);
            pol = new Polygon();
            pol.addPoint((int) Math.round(p1.getX()), (int) Math.round(p1.getY()));
            pol.addPoint((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
            pol.addPoint((int) Math.round(p3.getX()), (int) Math.round(p3.getY()));
            pol.addPoint((int) Math.round(p4.getX()), (int) Math.round(p4.getY()));
            g2.setColor(new Color(113, 163, 104));
            g2.fill(pol);
        }
        if (editNav == 1) {
            p1 = new Point2D.Double(cp.getX() - r * 1.27, cp.getY() - r * .99);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .18, r * .06);
        } else {
            p1 = new Point2D.Double(cp.getX() - r * 1.275, cp.getY() - r * .92);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .2, r * .06);
        }
        g2.setColor(new Color(44, 52, 234));
        g2.draw(rr);
        g2.setColor(clrWhite);
        double freqspace = 1.175 - .92;
        p1 = new Point2D.Double(cp.getX() - r * 1.175, cp.getY() - r * .92);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav1Stby.getValue()));
        p1 = new Point2D.Double(cp.getX() - r * 1.175, cp.getY() - r * .85);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav2Stby.getValue()));
        p1 = new Point2D.Double(cp.getX() - r * .92, cp.getY() - r * .92);
        g2.setColor(navHasNav1.getBooleanValue() ? clrGreen : clrWhite);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav1Freq.getValue()));
        p1 = new Point2D.Double(cp.getX() - r * .92, cp.getY() - r * .85);
        g2.setColor(navHasNav2.getBooleanValue() ? clrGreen : clrWhite);
        drawText(r, p1, g2, .5, "" + freqNF.format(varNav2Freq.getValue()));
        if (editCom == 1) {
            p1 = new Point2D.Double(cp.getX() + r * 1.065, cp.getY() - r * .99);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .18, r * .06);
        } else {
            p1 = new Point2D.Double(cp.getX() + r * 1.065, cp.getY() - r * .92);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .18, r * .06);
        }
        g2.setColor(new Color(44, 52, 234));
        g2.draw(rr);
        g2.setColor(clrWhite);
        double freq = (Double) varCom1Stby.getValue();
        p1 = new Point2D.Double(cp.getX() + r * 1.23, cp.getY() - r * .92);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        freq = (Double) varCom2Stby.getValue();
        p1 = new Point2D.Double(cp.getX() + r * 1.23, cp.getY() - r * .85);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        freq = (Double) varCom1Freq.getValue();
        p1 = new Point2D.Double(cp.getX() + r * (1.23 - freqspace), cp.getY() - r * .92);
        g2.setColor(com1Xmit.getBooleanValue() || comBoth.getBooleanValue() ? clrGreen : clrWhite);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        freq = (Double) varCom2Freq.getValue();
        p1 = new Point2D.Double(cp.getX() + r * (1.23 - freqspace), cp.getY() - r * .85);
        g2.setColor(com2Xmit.getBooleanValue() || comBoth.getBooleanValue() ? clrGreen : clrWhite);
        drawTextRight(r, p1, g2, .5, "1" + (freqNF.format((float) Integer.parseInt(Integer.toHexString((int) freq)) / 100)));
        g2.setColor(clrPink);
        String[] txt = { gpswni, distNF.format(gpswd) + " NM", Math.round(gpswdt) + "�", Math.round(gpsgmt) + "�" };
        double delta = r * 1.47 / 4;
        p1 = new Point2D.Double(cp.getX() - r * .55, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[0]);
        p1 = new Point2D.Double(cp.getX() - r * .55 + .34 * r, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[1]);
        p1 = new Point2D.Double(cp.getX() - r * .55 + .73 * r, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[2]);
        p1 = new Point2D.Double(cp.getX() - r * .55 + 1.1 * r, cp.getY() - r * .925);
        drawTextLeft(r, p1, g2, .45, txt[3]);
        double tw = r * .23;
        if (System.currentTimeMillis() - headingChanged < 3000) {
            p1 = new Point2D.Double(cp.getX() - r * .3 - tw, cp.getY() + r * .055);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), tw, r * .07);
            g2.setColor(clrBlack);
            g2.fill(rr);
            g2.setColor(clrWhite);
            g2.draw(rr);
            p2 = new Point2D.Double(p1.getX() + r * .01, p1.getY() + r * .06);
            drawTextLeft(r, p2, g2, .4, "HDG");
            g2.setColor(clrBlue);
            p2 = new Point2D.Double(p1.getX() - r * .01 + tw, p1.getY() + r * .07);
            drawTextRight(r, p2, g2, .5, Math.round(headingLockDir.getDoubleValue()) + "�");
        }
        if (System.currentTimeMillis() - obsChanged < 3000) {
            p1 = new Point2D.Double(cp.getX() + r * .3, cp.getY() + r * .055);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), tw, r * .07);
            g2.setColor(clrBlack);
            g2.fill(rr);
            g2.setColor(clrWhite);
            g2.draw(rr);
            p2 = new Point2D.Double(p1.getX() + r * .01, p1.getY() + r * .06);
            drawTextLeft(r, p2, g2, .4, "CRS");
            g2.setColor(clrGreen);
            p2 = new Point2D.Double(p1.getX() - r * .01 + tw, p1.getY() + r * .07);
            drawTextRight(r, p2, g2, .5, Math.round(obs) + "�");
        }
        delta = r * 2.8 / 12;
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(cp.getX() - r * 1.39, cp.getY() + r * .85);
        drawTextLeft(r, p1, g2, .45, "OAT " + Math.round((Double) varOAT.getValue()) + "�C");
        p1 = new Point2D.Double(cp.getX() + r * 1.41 - 3 * delta, cp.getY() + r * .85);
        drawTextLeft(r, p1, g2, .45, "XPDR");
        double xpdr = (Double) varXpdrCode.getValue();
        p1 = new Point2D.Double(cp.getX() + r * 1.39 - 1.5 * delta, cp.getY() + r * .85);
        g2.setColor(new Color(100, 150, 100));
        drawTextRight(r, p1, g2, .45, lz(Integer.parseInt(Integer.toHexString((int) xpdr)), 4) + "ALT");
        g2.setColor(Color.LIGHT_GRAY);
        p1 = new Point2D.Double(cp.getX() + r * 1.41 - 1.5 * delta, cp.getY() + r * .85);
        double lt = (Double) varLocalTime.getValue();
        int hours = (int) (lt / 3600);
        int minutes = (int) (lt / 60 % 60);
        int seconds = (int) (lt % 60);
        drawTextLeft(r, p1, g2, .45, "LCL  " + lz(hours, 2) + ":" + lz(minutes, 2) + ":" + lz(seconds, 2));
        if (markerI.getBooleanValue() || markerO.getBooleanValue() || markerM.getBooleanValue()) {
            double w = r * .07;
            p1 = new Point2D.Double(aicp.getX() + r * .45, aicp.getY() - r * .4);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), w, w);
            g2.setColor(new Color(197, 199, 123));
            g2.fill(rr);
            g2.setColor(Color.BLACK);
            g2.draw(rr);
            String str = "X";
            str = markerI.getBooleanValue() ? "I" : str;
            str = markerM.getBooleanValue() ? "M" : str;
            str = markerO.getBooleanValue() ? "O" : str;
            p1 = new Point2D.Double(p1.getX() + w / 2, p1.getY() + w);
            g2.setColor(Color.BLACK);
            drawText(r, p1, g2, .5, str);
        }
        Area ar;
        if (navHasNav1.getBooleanValue()) {
            p1 = new Point2D.Double(hsicp.getX() - r * .65, hsicp.getY() - r * 0.055);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .75, r * .2);
            ell = new Ellipse2D.Double((int) hsicp.getX() - r * .4, (int) hsicp.getY() - r * .4, (int) (r * .8), (int) (r * .8));
            ar = new Area(rr);
            ar.subtract(new Area(ell));
            g2.setColor(Color.BLACK);
            g2.fill(ar);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(ar);
            g2.setColor(Color.LIGHT_GRAY);
            if ((Double) hasDME1.getValue() == -1.0) {
                p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * .02);
                drawTextLeft(r, p1, g2, .5, dmeNF.format((Double) navDME1.getValue()) + " NM");
            }
            boolean gdn1 = (Double) gpsDrivesNav1.getValue() == 1;
            g2.setColor(gdn1 ? new Color(168, 108, 167) : new Color(108, 168, 107));
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.02 + .06));
            drawTextLeft(r, p1, g2, .5, ((Double) gpsDrivesNav1.getValue() == 1 ? hsiID.getStringValue() : "VOR1"));
            g2.setColor(Color.LIGHT_GRAY);
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.02 + .12));
            drawTextLeft(r, p1, g2, .5, (String) (nav1ident.getValue()));
        }
        if (navHasNav2.getBooleanValue()) {
            p1 = new Point2D.Double(hsicp.getX() - r * .65, hsicp.getY() + r * .175);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .65, r * .2);
            ell = new Ellipse2D.Double((int) hsicp.getX() - r * .4, (int) hsicp.getY() - r * .4, (int) (r * .8), (int) (r * .8));
            ar = new Area(rr);
            ar.subtract(new Area(ell));
            g2.setColor(Color.BLACK);
            g2.fill(ar);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(ar);
            g2.setColor(Color.LIGHT_GRAY);
            if ((Double) hasDME2.getValue() == -1.0) {
                p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * .245);
                drawTextLeft(r, p1, g2, .5, dmeNF.format((Double) navDME2.getValue()) + " NM");
            }
            g2.setColor(clrBlue);
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.245 + .06));
            drawTextLeft(r, p1, g2, .5, (String) (nav2ident.getValue()));
            g2.setColor(clrWhite);
            p1 = new Point2D.Double(hsicp.getX() - r * .63, hsicp.getY() + r * (.245 + .12));
            drawTextLeft(r, p1, g2, .5, "NAV2");
        }
        if (adfSignal.getDoubleValue() > 0.1) {
            p1 = new Point2D.Double(hsicp.getX(), hsicp.getY() + r * .175);
            rr = new Rectangle2D.Double(p1.getX(), p1.getY(), r * .65, r * .2);
            ell = new Ellipse2D.Double((int) hsicp.getX() - r * .4, (int) hsicp.getY() - r * .4, (int) (r * .8), (int) (r * .8));
            ar = new Area(rr);
            ar.subtract(new Area(ell));
            g2.setColor(Color.BLACK);
            g2.fill(ar);
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(ar);
            g2.setColor(clrWhite);
            p1 = new Point2D.Double(hsicp.getX() + r * .63, hsicp.getY() + r * .245);
            freq = (Double) adfFreq.getValue();
            drawTextRight(r, p1, g2, .5, "" + lz((float) Integer.parseInt(Integer.toHexString((int) freq)) / 10000, 6));
            g2.setColor(clrBlue);
            p1 = new Point2D.Double(hsicp.getX() + r * .63, hsicp.getY() + r * (.245 + .06));
            drawTextRight(r, p1, g2, .5, adfIdent.getStringValue());
            g2.setColor(clrWhite);
            p1 = new Point2D.Double(hsicp.getX() + r * .63, hsicp.getY() + r * (.245 + .12));
            drawTextRight(r, p1, g2, .5, "ADF1");
        }
        String[] buttons = { "", "INSET", "", "PFD", "", "CDI", "", "XPDR", "IDENT", "TMR/REF", "NRST", "ALERTS", "" };
        g2.setColor(Color.LIGHT_GRAY);
        delta = r * 2.8 / 12;
        for (int i = 1; i < 12; i++) {
            p1 = new Point2D.Double(cp.getX() - r * 1.4 + i * delta + delta / 2, cp.getY() + r * .92);
            drawText(r, p1, g2, .45, buttons[i]);
        }
    }
