    protected void drawPrimaryLineAsPath(XYItemRendererState state, Graphics2D g2, XYPlot plot, XYDataset dataset, int pass, int series, int item, ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
        if (!Double.isNaN(transX1) && !Double.isNaN(transY1)) {
            ControlPoint p = new ControlPoint(plot.getOrientation() == PlotOrientation.HORIZONTAL ? (float) transY1 : (float) transX1, plot.getOrientation() == PlotOrientation.HORIZONTAL ? (float) transX1 : (float) transY1);
            if (!this.points.contains(p)) {
                this.points.add(p);
            }
        }
        if (item == dataset.getItemCount(series) - 1) {
            State s = (State) state;
            if (this.points.size() > 1) {
                ControlPoint cp0 = (ControlPoint) this.points.get(0);
                s.seriesPath.moveTo(cp0.x, cp0.y);
                if (this.points.size() == 2) {
                    ControlPoint cp1 = (ControlPoint) this.points.get(1);
                    s.seriesPath.lineTo(cp1.x, cp1.y);
                } else {
                    int np = this.points.size();
                    float[] d = new float[np];
                    float[] x = new float[np];
                    float y;
                    float t;
                    float oldy = 0;
                    float oldt = 0;
                    float[] a = new float[np];
                    float t1;
                    float t2;
                    float[] h = new float[np];
                    for (int i = 0; i < np; i++) {
                        ControlPoint cpi = (ControlPoint) this.points.get(i);
                        x[i] = cpi.x;
                        d[i] = cpi.y;
                    }
                    for (int i = 1; i <= np - 1; i++) {
                        h[i] = x[i] - x[i - 1];
                    }
                    float[] sub = new float[np - 1];
                    float[] diag = new float[np - 1];
                    float[] sup = new float[np - 1];
                    for (int i = 1; i <= np - 2; i++) {
                        diag[i] = (h[i] + h[i + 1]) / 3;
                        sup[i] = h[i + 1] / 6;
                        sub[i] = h[i] / 6;
                        a[i] = (d[i + 1] - d[i]) / h[i + 1] - (d[i] - d[i - 1]) / h[i];
                    }
                    solveTridiag(sub, diag, sup, a, np - 2);
                    oldt = x[0];
                    oldy = d[0];
                    s.seriesPath.moveTo(oldt, oldy);
                    for (int i = 1; i <= np - 1; i++) {
                        for (int j = 1; j <= this.precision; j++) {
                            t1 = (h[i] * j) / this.precision;
                            t2 = h[i] - t1;
                            y = ((-a[i - 1] / 6 * (t2 + h[i]) * t1 + d[i - 1]) * t2 + (-a[i] / 6 * (t1 + h[i]) * t2 + d[i]) * t1) / h[i];
                            t = x[i - 1] + t1;
                            s.seriesPath.lineTo(t, y);
                            oldt = t;
                            oldy = y;
                        }
                    }
                }
                drawFirstPassShape(g2, pass, series, item, s.seriesPath);
            }
            this.points = new Vector();
        }
    }
