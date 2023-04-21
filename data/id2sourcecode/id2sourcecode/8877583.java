    @Override
    public void paintComponent(Graphics graphics) {
        field.initializePaintComponent(graphics);
        int w = field.w;
        int w0 = field.w0;
        double displayedProportion = field.displayedProportion;
        int h0 = field.h0;
        int h = field.h;
        double heightProportion = field.heightProportion;
        h0 = drawLabels ? (graphics.getFontMetrics().getHeight()) : h0;
        h = (int) (field.getHeight() - 2 * h0);
        ph = (int) (h * heightProportion);
        ph0 = (h - ph) / 2;
        ph1 = (h + ph) / 2;
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean negativeInfinity = Float.compare(fuzzy.getA(), Float.NEGATIVE_INFINITY) == 0;
        boolean positiveInfinity = Float.compare(fuzzy.getB(), Float.POSITIVE_INFINITY) == 0;
        if (Float.compare(fuzzy.getA(), fuzzy.getB()) == 0 && Float.compare(fuzzy.getBeta(), fuzzy.getAlpha()) == 0 && Float.compare(fuzzy.getBeta(), 0.0f) == 0) {
            graphics.setColor(zeroColor);
            graphics.fillRect(w0, h0 + ph0, w, ph);
            graphics.setColor(Color.black);
            graphics.drawRect(w0, h0 + ph0, w, ph);
            double m = w / 2;
            graphics.setColor(oneColor);
            graphics.fillRect((int) (w0 + m * 0.9), h0, (int) (m * 0.2), h);
            graphics.setColor(Color.black);
            graphics.drawRect((int) (w0 + m * 0.9), h0, (int) (m * 0.2), h);
            graphics.setFont(new Font(graphics.getFont().getFontName(), Font.BOLD, 16));
            field.drawLabel(w / 2, graphics, "" + fuzzy.getA(), h0 + h / 2, -.5f, new Color(220, 220, 220), 1);
        } else {
            if (negativeInfinity && positiveInfinity) {
                graphics.setColor(oneColor);
                graphics.fillRect(w0, h0, w, h);
                graphics.setColor(Color.black);
                graphics.drawLine(w0, h0, w0, h0 + h);
                graphics.drawLine(w0, h0, w0 + w, h0);
                graphics.drawLine(w0, h0 + h, w0 + w, h0 + h);
                graphics.drawLine(w0 + w, h0, w0 + w, h0 + h);
                graphics.setFont(new Font(graphics.getFont().getFontName(), Font.BOLD, 16));
                field.drawLabel(w / 2, graphics, " ? ", h0 + h / 2, -.5f, new Color(220, 220, 220), 1);
            } else {
                double coreWidth = fuzzy.getB() - fuzzy.getA();
                double supportWidth = coreWidth + fuzzy.getBeta() + fuzzy.getAlpha();
                double widthProportion;
                if (negativeInfinity) {
                    widthProportion = w * displayedProportion / (fuzzy.getB() + fuzzy.getBeta());
                } else if (positiveInfinity) {
                    widthProportion = w * displayedProportion / (fuzzy.getA() + fuzzy.getAlpha());
                } else {
                    widthProportion = w * displayedProportion / supportWidth;
                }
                if (negativeInfinity) {
                    f1 = w0;
                    f2 = f1;
                } else {
                    f1 = (int) (w * (1 - displayedProportion) / 2);
                    f2 = f1 + (int) (widthProportion * fuzzy.getAlpha());
                }
                if (positiveInfinity) {
                    f3 = w;
                    f4 = f3;
                } else {
                    f4 = (int) (w - (w * (1 - displayedProportion) / 2));
                    f3 = f4 - (int) (widthProportion * fuzzy.getBeta());
                }
                if (!negativeInfinity) {
                    graphics.setColor(zeroColor);
                    graphics.fillRect(w0, h0 + ph0, f1, ph);
                    graphics.setColor(Color.black);
                    graphics.drawLine(w0, h0 + ph0, w0 + f1, h0 + ph0);
                    graphics.drawLine(w0, h0 + ph0, w0, h0 + ph1);
                    graphics.drawLine(w0, h0 + ph1, w0 + f1, h0 + ph1);
                    double val;
                    double alpha = f2 - f1;
                    double ra = (oneColor.getRed() - zeroColor.getRed()) / alpha;
                    double ga = (oneColor.getGreen() - zeroColor.getGreen()) / alpha;
                    double ba = (oneColor.getBlue() - zeroColor.getBlue()) / alpha;
                    double aa = (oneColor.getAlpha() - zeroColor.getAlpha()) / alpha;
                    int rb = zeroColor.getRed();
                    int gb = zeroColor.getGreen();
                    int bb = zeroColor.getBlue();
                    int ab = zeroColor.getAlpha();
                    for (int i = 0; i < alpha; i++) {
                        val = i / alpha;
                        graphics.setColor(new Color(rb + (int) (i * ra), gb + (int) (i * ga), bb + (int) (i * ba), ab + (int) (i * aa)));
                        graphics.drawLine(w0 + f1 + i, h0 + (int) ((1 - val) * ph0), w0 + f1 + i, h0 + ph1 + (int) (val * ph0));
                    }
                    graphics.setColor(Color.black);
                    graphics.drawLine(w0 + f1, h0 + ph0, w0 + f2, h0);
                    graphics.drawLine(w0 + f1, h0 + ph1, w0 + f2, h0 + ph0 + ph1);
                }
                graphics.setColor(oneColor);
                graphics.fillRect(w0 + f2, h0, f3 - f2, h);
                graphics.setColor(Color.black);
                graphics.drawLine(w0 + f2, h0, w0 + f3, h0);
                graphics.drawLine(w0 + f2, h0 + ph0 + ph1, w0 + f3, h0 + ph0 + ph1);
                if (!positiveInfinity) {
                    double beta = f4 - f3;
                    double ra = (oneColor.getRed() - zeroColor.getRed()) / beta;
                    double ga = (oneColor.getGreen() - zeroColor.getGreen()) / beta;
                    double ba = (oneColor.getBlue() - zeroColor.getBlue()) / beta;
                    double aa = (oneColor.getAlpha() - zeroColor.getAlpha()) / beta;
                    int rb = oneColor.getRed();
                    int gb = oneColor.getGreen();
                    int bb = oneColor.getBlue();
                    int ab = oneColor.getAlpha();
                    for (int i = 0; i < beta; i++) {
                        double val = i / beta;
                        graphics.setColor(new Color(rb - (int) (i * ra), gb - (int) (i * ga), bb - (int) (i * ba), ab - (int) (i * aa)));
                        graphics.drawLine(w0 + f3 + i, h0 + (int) ((val) * ph0), w0 + f3 + i, h0 + ph1 + (int) ((1 - val) * ph0));
                    }
                    graphics.setColor(Color.black);
                    graphics.drawLine(w0 + f3, h0, w0 + f4, h0 + ph0);
                    graphics.drawLine(w0 + f3, h0 + ph0 + ph1, w0 + f4, h0 + ph1);
                    graphics.setColor(zeroColor);
                    graphics.fillRect(w0 + f4, h0 + ph0, w - f4, ph);
                    graphics.setColor(Color.black);
                    graphics.drawLine(w0 + f4, h0 + ph0, w0 + w, h0 + ph0);
                    graphics.drawLine(w0 + f4, h0 + ph1, w0 + w, h0 + ph1);
                    graphics.drawLine(w0 + w, h0 + ph0, w0 + w, h0 + ph1);
                }
                graphics.setColor(Color.black);
                if (!negativeInfinity) {
                    graphics.fill3DRect(w0 + f1 - 2, h0 + ph0, 4, ph, true);
                    graphics.fill3DRect(w0 + f2 - 2, h0, 4, h, true);
                }
                if (!positiveInfinity) {
                    graphics.fill3DRect(w0 + f3 - 2, h0, 4, h, true);
                    graphics.fill3DRect(w0 + f4 - 2, h0 + ph0, 4, ph, true);
                }
                boolean equal = Float.compare(fuzzy.getA(), fuzzy.getB()) == 0;
                if (drawLabels) {
                    String aString;
                    if (Float.compare(fuzzy.getA(), Float.NEGATIVE_INFINITY) == 0) {
                        aString = "-∞";
                    } else {
                        aString = "" + fuzzy.getA();
                    }
                    field.drawTopBottomLabel(f2, graphics, aString, false);
                    float alpha = Math.round(Math.abs(1000 * fuzzy.getAlpha() / fuzzy.getA())) / 10;
                    field.drawTopBottomLabel(f1, graphics, alpha + "%", true, new Color(255, 255, 255, 100));
                    float beta = Math.round(Math.abs(1000 * fuzzy.getBeta() / fuzzy.getB())) / 10;
                    field.drawTopBottomLabel(f4, graphics, beta + "%", false, new Color(255, 255, 255, 100));
                    if (!equal) {
                        String bString;
                        if (Float.compare(fuzzy.getB(), Float.POSITIVE_INFINITY) == 0) {
                            bString = "+∞";
                        } else {
                            bString = "" + fuzzy.getB();
                        }
                        field.drawTopBottomLabel(f3, graphics, bString, true);
                    }
                }
            }
            if (holdHandle != Handle.None) {
                if (holdHandle == Handle.A || holdHandle == Handle.B) {
                    graphics.setColor(new Color(0, 0, 0, 50));
                    graphics.fillRect(mouseX - 2, h0, 4, h);
                } else {
                    graphics.setColor(new Color(0, 0, 0, 50));
                    graphics.fillRect(mouseX - 2, h0 + ph0, 4, ph);
                }
            }
        }
    }
