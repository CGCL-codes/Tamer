    protected void paintSelection(Graphics g) {
        if (hasSelection()) {
            Graphics2D g2 = (Graphics2D) g;
            Color oldColor = g2.getColor();
            if (selectionColor != null) {
                g2.setColor(selectionColor);
            } else {
                g2.setColor(Color.RED);
            }
            Stroke old = g2.getStroke();
            BasicStroke bs = new BasicStroke(2);
            g2.setStroke(bs);
            Rectangle r = buildSelectionRect();
            Rectangle c = new Rectangle(0, 0, cornerWidth, cornerWidth);
            switch(mode) {
                case MODE_LINE:
                    g2.drawLine(x1, y1, x2, y2);
                    g2.setStroke(old);
                    int xc = (x1 + x2) / 2;
                    int yc = (y1 + y2) / 2;
                    double xn = (y2 - y1);
                    double yn = -(x2 - x1);
                    double n = Math.sqrt(xn * xn + yn * yn);
                    int vxn = (int) (8.0 * (xn / n));
                    int vyn = (int) (8.0 * (yn / n));
                    xn = (x2 - x1);
                    yn = (y2 - y1);
                    n = Math.sqrt(xn * xn + yn * yn);
                    int vx = (int) (10.0 * (xn / n));
                    int vy = (int) (10.0 * (yn / n));
                    g2.drawLine(xc - vxn, yc - vyn, xc + vxn, yc + vyn);
                    g2.drawLine(xc + vxn, yc + vyn, xc + vx, yc + vy);
                    g2.drawLine(xc + vx, yc + vy, xc - vxn, yc - vyn);
                    c.translate(x1 - cornerWidth / 2, y1 - cornerWidth / 2);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    c.translate(x2 - x1, y2 - y1);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    break;
                case MODE_RECT:
                    g2.drawRect(r.x, r.y, r.width, r.height);
                    g2.setStroke(old);
                    c.translate(r.x - cornerWidth / 2, r.y - cornerWidth / 2);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    c.translate(r.width, 0);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    c.translate(0, r.height);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    c.translate(-r.width, 0);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    c.translate(r.width / 2, -r.height / 2);
                    g2.drawRect(c.x, c.y, c.width, c.height);
                    break;
                case MODE_CROSS:
                    int xv, yh;
                    switch(horizontalPosition) {
                        case HORIZONTAL_TOP:
                            yh = (y1 < y2 ? y1 : y2);
                            break;
                        case HORIZONTAL_CENTER:
                            yh = (y1 + y2) / 2;
                            break;
                        case HORIZONTAL_BOTTOM:
                            yh = (y1 < y2 ? y2 : y1);
                            break;
                        default:
                            yh = -1;
                    }
                    switch(verticalPosition) {
                        case VERTICAL_LEFT:
                            xv = (x1 < x2 ? x1 : x2);
                            break;
                        case VERTICAL_CENTER:
                            xv = (x1 + x2) / 2;
                            break;
                        case VERTICAL_RIGHT:
                            xv = (x1 < x2 ? x2 : x1);
                            break;
                        default:
                            xv = -1;
                    }
                    if (xv >= 0 && yh >= 0) {
                        g2.drawLine(x1, yh, x2, yh);
                        g2.drawLine(xv, y1, xv, y2);
                        if (x1 < x2) {
                            g2.drawLine(x2, yh, x2 - 8, yh - 8);
                            g2.drawLine(x2 - 8, yh - 8, x2 - 8, yh + 8);
                            g2.drawLine(x2 - 8, yh + 8, x2, yh);
                        } else {
                            g2.drawLine(x2, yh, x2 + 8, yh - 8);
                            g2.drawLine(x2 + 8, yh - 8, x2 + 8, yh + 8);
                            g2.drawLine(x2 + 8, yh + 8, x2, yh);
                        }
                        if (y1 < y2) {
                            g2.drawLine(xv, y2, xv - 8, y2 - 8);
                            g2.drawLine(xv - 8, y2 - 8, xv + 8, y2 - 8);
                            g2.drawLine(xv + 8, y2 - 8, xv, y2);
                        } else {
                            g2.drawLine(xv, y2, xv - 8, y2 + 8);
                            g2.drawLine(xv - 8, y2 + 8, xv + 8, y2 + 8);
                            g2.drawLine(xv + 8, y2 + 8, xv, y2);
                        }
                        g2.setStroke(old);
                        c.translate(r.x - cornerWidth / 2, r.y - cornerWidth / 2);
                        g2.drawRect(c.x, c.y, c.width, c.height);
                        c.translate(r.width, 0);
                        g2.drawRect(c.x, c.y, c.width, c.height);
                        c.translate(0, r.height);
                        g2.drawRect(c.x, c.y, c.width, c.height);
                        c.translate(-r.width, 0);
                        g2.drawRect(c.x, c.y, c.width, c.height);
                        c.translate(r.width / 2, -r.height / 2);
                        g2.drawRect(c.x, c.y, c.width, c.height);
                    }
                    break;
            }
            g2.setColor(oldColor);
            g2.setPaintMode();
        }
    }
