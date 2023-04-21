    public void paintSamples(Graphics2D g2d, Color color, float colorGamma) {
        try {
            AChannel ch = getChannelModel();
            ALayer layer = (ALayer) ch.getParent();
            int width = rectangle.width;
            switch(layer.getType()) {
                case ALayer.PARAMETER_LAYER:
                    style = DRAWED;
                    break;
                default:
                    style = FILLED;
                    break;
            }
            int xMin;
            int xMax;
            float yTop;
            float yBottom;
            float yZero;
            float oldYTop = 0;
            float oldYBottom = 0;
            boolean firstPointPlotted = true;
            g2d.setClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            Color brighterColor = GToolkit.mixColors(color, Color.WHITE, 0.17f);
            int l = (int) getXLength() + 3;
            if ((width / getXLength()) > 2.5) {
                if (x == null) {
                    x = new MMArray(l, 0);
                } else if (x.getLength() != l) {
                    x.setLength(l);
                }
                if (y == null) {
                    y = new MMArray(l, 0);
                } else if (y.getLength() != l) {
                    y.setLength(l);
                }
                for (int i = 0; i < l; i++) {
                    x.set(i, sampleToGraphX((int) getXOffset() + i));
                    y.set(i, sampleToGraphY(ch.getSample((int) getXOffset() + i)));
                }
                int oldX = (int) x.get(0) - 1;
                int oldY = (int) y.get(0);
                spline.load(x, y);
                g2d.setColor(color);
                for (int i = 0; i < width; i += 1) {
                    int newX = i;
                    int newY = (int) spline.getResult((float) i);
                    if (ch.getSamples().isInRange((int) graphToSampleX(newX))) {
                        g2d.drawLine(oldX, oldY, newX, newY);
                    }
                    oldX = newX;
                    oldY = newY;
                }
            } else {
                g2d.setColor(color);
                reloadCache();
                yZero = sampleToGraphY(0);
                for (int i = 0; i < width; i++) {
                    xMin = (int) graphToSampleX(i);
                    xMax = (int) graphToSampleX(i + 1);
                    if ((xMin >= ch.getSampleLength()) || (xMax >= ch.getSampleLength())) break;
                    if ((xMin < 0) || (xMax < 0)) continue;
                    yBottom = getMinSample(xMin, xMax);
                    yTop = getMaxSample(xMin, xMax);
                    yBottom = sampleToGraphY(yBottom);
                    yTop = sampleToGraphY(yTop);
                    if (firstPointPlotted) {
                        firstPointPlotted = false;
                        oldYBottom = yBottom;
                        oldYTop = yTop;
                    }
                    switch(style) {
                        case DRAWED:
                            g2d.drawLine(i - 1, (int) oldYBottom, i, (int) yBottom);
                            g2d.drawLine(i - 1, (int) oldYTop, i, (int) yTop);
                            break;
                        case FILLED:
                            if (yTop < yZero && yBottom > yZero) {
                                g2d.setColor(brighterColor);
                                g2d.drawLine(i, (int) yZero, i, (int) yTop);
                                g2d.setColor(color);
                                g2d.drawLine(i, (int) yBottom, i, (int) yZero);
                            } else {
                                g2d.setColor(color);
                                g2d.drawLine(i, (int) yBottom, i, (int) yTop);
                            }
                            break;
                    }
                    oldYBottom = yBottom;
                    oldYTop = yTop;
                }
            }
            if ((width / getXLength()) > 5) {
                g2d.setColor(color);
                for (int i = 0; i < l; i++) {
                    if (ch.getSamples().isInRange((int) graphToSampleX((int) x.get(i)))) {
                        g2d.fillRect((int) x.get(i) - 2, (int) y.get(i) - 2, 4, 4);
                    }
                }
            }
        } catch (Exception e) {
            Debug.printStackTrace(5, e);
        }
    }
