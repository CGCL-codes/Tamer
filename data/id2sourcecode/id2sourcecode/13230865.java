    public static void drawKneeLine(Graphics g, ViewState view, OutLink out, InLink in, boolean firstHorizontal) {
        double scale = view.getScale();
        int x1 = (int) (scale * out.getOutX() - view.getRx());
        int y1 = (int) (scale * out.getOutY() - view.getRy());
        int x2 = x1;
        int y2 = y1;
        boolean isInLeft = false;
        int middleInX = 0;
        if (in != null) {
            if (in instanceof EPICSVarOutLink) {
                int lx = in.getLeftX();
                int rx = in.getRightX();
                middleInX = (lx + rx) / 2;
                if (out.getOutX() > middleInX) x2 = (int) (scale * rx - view.getRx()); else {
                    x2 = (int) (scale * lx - view.getRx());
                    isInLeft = true;
                }
            } else {
                x2 = (int) (scale * in.getInX() - view.getRx());
                isInLeft = !in.isRight();
            }
            y2 = (int) (scale * in.getInY() - view.getRy());
        }
        if (out.getMode() == OutLink.INVISIBLE_MODE) {
            int s = (int) (scale * Constants.INVISIBLE_CROSS_SIZE);
            if (firstHorizontal) {
                g.drawLine(x1 - s, y1, x1 + s, y1);
                g.drawLine(x1, y1 - s, x1, y1 + s);
                g.drawLine(x2, y2 - s, x2, y2 + s);
                g.drawLine(x2 - s, y2, x2 + s, y2);
            } else {
                g.drawLine(x1, y1 - s, x1, y1 + s);
                g.drawLine(x1 - s, y1, x1 + s, y1);
                g.drawLine(x2 - s, y2, x2 + s, y2);
                g.drawLine(x2, y2 - s, x2, y2 + s);
            }
            Linkable descPoint = out;
            int r = (int) (LARGE_RECT * view.getScale());
            int dx = tailLenOfR * r;
            String label = null;
            validateFont(view);
            if (font != null) {
                g.setFont(font);
                Linkable target = null;
                {
                    target = EPICSLinkOut.getEndPoint(descPoint);
                    if (target instanceof Descriptable) label = ((Descriptable) target).getDescription();
                }
                if (label != null) {
                    dx = -(fontMetrics.stringWidth(label) + 2 * r);
                    g.drawString(label, x1 + dx, y1 + dy);
                }
            }
            label = null;
            descPoint = in;
            r = (int) (LARGE_RECT * view.getScale());
            dx = tailLenOfR * r;
            if (font != null) {
                g.setFont(font);
                Linkable target = null;
                {
                    target = EPICSLinkOut.getStartPoint(descPoint);
                    if (target instanceof Descriptable) label = ((Descriptable) target).getDescription();
                }
                if (label != null) {
                    dx = -(fontMetrics.stringWidth(label) + 2 * r);
                    g.drawString(label, x2 + dx, y2 + dy);
                }
            }
            return;
        } else if (out.getMode() == OutLink.EXTERNAL_OUTPUT_MODE) {
            int s = (int) (scale * Constants.LINK_STUB_SIZE / 2.0);
            if (!firstHorizontal) s = -s;
            g.drawLine(x1, y1, x1 + s, y1);
            x1 += s;
            g.drawLine(x1, y1 - s, x1, y1 + s);
            g.drawLine(x1, y1 - s, x1 + s, y1 - s);
            g.drawLine(x1, y1 + s, x1 + s, y1 + s);
            g.drawLine(x1 + s, y1 - s, x1 + 2 * s, y1);
            g.drawLine(x1 + s, y1 + s, x1 + 2 * s, y1);
            Linkable descPoint = out;
            int r = (int) (LARGE_RECT * view.getScale());
            int dx = tailLenOfR * r;
            String label = null;
            validateFont(view);
            if (font != null) {
                g.setFont(font);
                Linkable target = null;
                {
                    target = EPICSLinkOut.getStartPoint(descPoint);
                    if (target instanceof Field) label = ((Field) target).getFieldData().getValue();
                }
                if (label != null) {
                    if (!firstHorizontal) dx = -fontMetrics.stringWidth(label) + 3 * s;
                    g.drawString(label, x1 + dx, y1 + dy);
                }
            }
            return;
        } else if (out.getMode() == OutLink.EXTERNAL_INPUT_MODE) {
            int s = (int) (scale * Constants.LINK_STUB_SIZE / 2.0);
            if (!firstHorizontal) s = -s;
            g.drawLine(x1, y1, x1 + s, y1);
            x1 += s;
            g.drawLine(x1, y1 - s, x1, y1 + s);
            g.drawLine(x1, y1 - s, x1 + 2 * s, y1 - s);
            g.drawLine(x1, y1 + s, x1 + 2 * s, y1 + s);
            g.drawLine(x1 + 2 * s, y1 - s, x1 + s, y1);
            g.drawLine(x1 + 2 * s, y1 + s, x1 + s, y1);
            Linkable descPoint = out;
            int r = (int) (LARGE_RECT * view.getScale());
            int dx = tailLenOfR * r;
            String label = null;
            validateFont(view);
            if (font != null) {
                g.setFont(font);
                Linkable target = null;
                {
                    target = EPICSLinkOut.getStartPoint(descPoint);
                    if (target instanceof Field) label = ((Field) target).getFieldData().getValue();
                }
                if (label != null) {
                    if (!firstHorizontal) dx = -fontMetrics.stringWidth(label) + 3 * s;
                    g.drawString(label, x1 + dx, y1 + dy);
                }
            }
            return;
        }
        int dotSize = view.getDotSize();
        int dotSize2 = 2 * dotSize;
        if (in != null) {
            boolean doDots = false;
            boolean drawDot = in instanceof MultiInLink && ((MultiInLink) in).getLinkCount() > 1;
            if (drawDot && in instanceof EPICSVarOutLink) doDots = drawDot = checkForSameSideLinks((EPICSVarOutLink) in, isInLeft, middleInX); else doDots = drawDot;
            if (doDots) {
                if (out instanceof Connector) {
                    if (!firstHorizontal) {
                        int ix = isInLeft ? in.getLeftX() : in.getRightX();
                        drawDot = checkConnectorOuterMost((MultiInLink) in, out.getOutX(), ix, isInLeft, middleInX);
                    } else drawDot = checkHorizontalFirstDootNeeded((MultiInLink) in, out.getOutY(), in.getInY(), isInLeft, middleInX);
                } else drawDot = checkHorizontalFirstDootNeeded((MultiInLink) in, out.getOutY(), in.getInY(), isInLeft, middleInX);
            }
            if (firstHorizontal) {
                g.drawLine(x1, y1, x2, y1);
                g.drawLine(x2, y1, x2, y2);
                if (doDots && drawDot) {
                    g.fillOval(x2 - dotSize, y1 - dotSize, dotSize2, dotSize2);
                } else {
                    if (doDots && checkMiddleDotNeededCase((MultiInLink) in, out.getOutY(), in.getInY(), isInLeft, middleInX)) g.fillOval(x2 - dotSize, y2 - dotSize, dotSize2, dotSize2);
                }
            } else {
                g.drawLine(x1, y1, x1, y2);
                g.drawLine(x1, y2, x2, y2);
                if (drawDot) {
                    g.fillOval(x1 - dotSize, y2 - dotSize, dotSize2, dotSize2);
                }
            }
        }
        if (Settings.getInstance().isWireCrossingAvoidiance()) {
            final double rlsw = Constants.LINK_SLOT_WIDTH * scale;
            if (out instanceof Field) {
                if (out.isRight()) g.drawLine(x1, y1, x1 - (int) (((Field) out).getVerticalPosition() * rlsw), y1); else g.drawLine(x1, y1, x1 + (int) (((Field) out).getVerticalPosition() * rlsw), y1);
            }
        }
    }
