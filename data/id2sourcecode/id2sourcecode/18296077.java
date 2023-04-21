    protected AxisState drawTickLabels(int band, Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
        double delta1 = 0.0;
        FontMetrics fm = g2.getFontMetrics(this.labelInfo[band].getLabelFont());
        if (edge == RectangleEdge.BOTTOM) {
            delta1 = this.labelInfo[band].getPadding().calculateTopOutset(fm.getHeight());
        } else if (edge == RectangleEdge.TOP) {
            delta1 = this.labelInfo[band].getPadding().calculateBottomOutset(fm.getHeight());
        }
        state.moveCursor(delta1, edge);
        long axisMin = this.first.getFirstMillisecond(this.calendar);
        long axisMax = this.last.getLastMillisecond(this.calendar);
        g2.setFont(this.labelInfo[band].getLabelFont());
        g2.setPaint(this.labelInfo[band].getLabelPaint());
        RegularTimePeriod p1 = this.labelInfo[band].createInstance(new Date(axisMin), this.timeZone);
        RegularTimePeriod p2 = this.labelInfo[band].createInstance(new Date(axisMax), this.timeZone);
        String label1 = this.labelInfo[band].getDateFormat().format(new Date(p1.getMiddleMillisecond(this.calendar)));
        String label2 = this.labelInfo[band].getDateFormat().format(new Date(p2.getMiddleMillisecond(this.calendar)));
        Rectangle2D b1 = TextUtilities.getTextBounds(label1, g2, g2.getFontMetrics());
        Rectangle2D b2 = TextUtilities.getTextBounds(label2, g2, g2.getFontMetrics());
        double w = Math.max(b1.getWidth(), b2.getWidth());
        long ww = Math.round(java2DToValue(dataArea.getX() + w + 5.0, dataArea, edge));
        if (isInverted()) {
            ww = axisMax - ww;
        } else {
            ww = ww - axisMin;
        }
        long length = p1.getLastMillisecond(this.calendar) - p1.getFirstMillisecond(this.calendar);
        int periods = (int) (ww / length) + 1;
        RegularTimePeriod p = this.labelInfo[band].createInstance(new Date(axisMin), this.timeZone);
        Rectangle2D b = null;
        long lastXX = 0L;
        float y = (float) (state.getCursor());
        TextAnchor anchor = TextAnchor.TOP_CENTER;
        float yDelta = (float) b1.getHeight();
        if (edge == RectangleEdge.TOP) {
            anchor = TextAnchor.BOTTOM_CENTER;
            yDelta = -yDelta;
        }
        while (p.getFirstMillisecond(this.calendar) <= axisMax) {
            float x = (float) valueToJava2D(p.getMiddleMillisecond(this.calendar), dataArea, edge);
            DateFormat df = this.labelInfo[band].getDateFormat();
            String label = df.format(new Date(p.getMiddleMillisecond(this.calendar)));
            long first = p.getFirstMillisecond(this.calendar);
            long last = p.getLastMillisecond(this.calendar);
            if (last > axisMax) {
                Rectangle2D bb = TextUtilities.getTextBounds(label, g2, g2.getFontMetrics());
                if ((x + bb.getWidth() / 2) > dataArea.getMaxX()) {
                    float xstart = (float) valueToJava2D(Math.max(first, axisMin), dataArea, edge);
                    if (bb.getWidth() < (dataArea.getMaxX() - xstart)) {
                        x = ((float) dataArea.getMaxX() + xstart) / 2.0f;
                    } else {
                        label = null;
                    }
                }
            }
            if (first < axisMin) {
                Rectangle2D bb = TextUtilities.getTextBounds(label, g2, g2.getFontMetrics());
                if ((x - bb.getWidth() / 2) < dataArea.getX()) {
                    float xlast = (float) valueToJava2D(Math.min(last, axisMax), dataArea, edge);
                    if (bb.getWidth() < (xlast - dataArea.getX())) {
                        x = (xlast + (float) dataArea.getX()) / 2.0f;
                    } else {
                        label = null;
                    }
                }
            }
            if (label != null) {
                g2.setPaint(this.labelInfo[band].getLabelPaint());
                b = TextUtilities.drawAlignedString(label, g2, x, y, anchor);
            }
            if (lastXX > 0L) {
                if (this.labelInfo[band].getDrawDividers()) {
                    long nextXX = p.getFirstMillisecond(this.calendar);
                    long mid = (lastXX + nextXX) / 2;
                    float mid2d = (float) valueToJava2D(mid, dataArea, edge);
                    g2.setStroke(this.labelInfo[band].getDividerStroke());
                    g2.setPaint(this.labelInfo[band].getDividerPaint());
                    g2.draw(new Line2D.Float(mid2d, y, mid2d, y + yDelta));
                }
            }
            lastXX = last;
            for (int i = 0; i < periods; i++) {
                p = p.next();
            }
        }
        double used = 0.0;
        if (b != null) {
            used = b.getHeight();
            if (edge == RectangleEdge.BOTTOM) {
                used += this.labelInfo[band].getPadding().calculateBottomOutset(fm.getHeight());
            } else if (edge == RectangleEdge.TOP) {
                used += this.labelInfo[band].getPadding().calculateTopOutset(fm.getHeight());
            }
        }
        state.moveCursor(used, edge);
        return state;
    }
