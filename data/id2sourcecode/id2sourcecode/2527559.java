    public void drawGrafic(Graphics g) {
        double rezolutieFrecventa, q, domeniuFrecventa;
        int val = 0, pas, contor = 0;
        top = vertSpace;
        bottom = getSize().height - vertSpace;
        left = horzSpace;
        right = getSize().width - horzSpace;
        widthGraph = right - left;
        fullHeight = bottom - top;
        centru = (top + bottom) / 2;
        xAxisPos = centru;
        int yHeight = fullHeight / 2;
        xAxisPos = bottom;
        yHeight = fullHeight;
        bgColor = Color.WHITE;
        g.setColor(axisColor);
        g.drawString(denumireAxaX, right, bottom + 5);
        g.drawString(denumireAxaY, left - 23, top - 5);
        g.drawString(titlu, widthGraph / 2, top - 10);
        g.drawLine(left, top - 10, left, bottom);
        g.drawLine(left, xAxisPos, right, xAxisPos);
        g.setColor(plotColor);
        if (tracePlot) {
            nPoints = model.getCount();
        }
        if (nPoints != 0) {
            xScale = (widthGraph) / (float) (nPoints - 1);
            yScale = yHeight / ymax;
            int[] xCoords = new int[nPoints];
            int[] yCoords = new int[nPoints];
            if (tracePlot) {
                int oldx = left, oldy = getHeight() / 2;
                int newx, newy;
                xAxisPos = top;
                g.setColor(axisColor);
                g.drawString("0", left - 5, centru);
                g.drawString("" + (int) (ymax + 1), left - 5, (int) (centru - ymax));
                g.drawString("-" + (int) (ymax + 1), left - 5, (int) (centru + ymax));
                for (int i = 0; i <= vertIntervals; i++) {
                    x = left + i * width / vertIntervals;
                    g.drawLine(x, top, x, bottom);
                }
                for (int i = 0; i <= horzIntervals; i++) {
                    y = top + i * fullHeight / horzIntervals;
                    g.drawLine(left, y, right, y);
                }
                g.setColor(plotColor);
                for (int i = 0; i < model.getCount() - 1; i++) {
                    synchronized (model) {
                        newx = (model.getX(i, getWidth() - 2 * horzSpace)) % (getWidth());
                        newy = model.getY(i, getHeight());
                    }
                    newx += left;
                    if (newx != left) {
                        if ((oldx - newx) < 10) g.drawLine(oldx, oldy, newx, newy);
                    }
                    if (newy < 1) {
                        g.drawString("ss " + newx, newx, bottom + 10);
                        g.drawLine(newx, top, newx, bottom);
                    }
                    oldx = newx;
                    oldy = newy;
                }
            } else {
                domeniuFrecventa = sampleFreq / 2;
                pas = (int) domeniuFrecventa / 10;
                for (int j = pas; j < domeniuFrecventa; j += pas) {
                    g.setColor(axisColor);
                    rezolutieFrecventa = sampleFreq / 256.0;
                    q = j / rezolutieFrecventa;
                    if (q - (int) q > 0.5) q = (int) q + 1; else q = (int) q;
                    int pozX = (int) (left + q * xScale);
                    g.drawString(" " + j, pozX - 5, bottom + 10);
                    g.drawLine(pozX + 5, top, pozX + 5, bottom);
                }
                for (int i = 0; i <= horzIntervals; i++) {
                    y = top + i * fullHeight / horzIntervals;
                    g.drawLine(left, y, right, y);
                }
                g.drawString(" " + (int) (ymax + 1), left - 22, top + 5);
                g.setColor(plotColor);
                for (int i = 0; i < nPoints; i++) {
                    xCoords[i] = left + Math.round(i * xScale);
                    yCoords[i] = xAxisPos - Math.round(plotValues[i] * yScale);
                    g.drawLine(xCoords[i], xAxisPos, xCoords[i], yCoords[i]);
                }
            }
        }
    }
