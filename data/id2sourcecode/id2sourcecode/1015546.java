        public synchronized void update(DasAxis xAxis, DasAxis yAxis, QDataSet dataSet, ProgressMonitor mon) {
            QDataSet xds = SemanticOps.xtagsDataSet(dataSet);
            if (xds.rank() == 2 && xds.property(QDataSet.BINS_1) != null) {
                xds = Ops.reduceMean(xds, 1);
            }
            QDataSet vds = ytagsDataSet(dataSet);
            if (vds.rank() > 1) {
                return;
            }
            QDataSet wds = SemanticOps.weightsDataSet(vds);
            Units xUnits = SemanticOps.getUnits(xds);
            Units yUnits = SemanticOps.getUnits(vds);
            if (unitsWarning) yUnits = yAxis.getUnits();
            if (xunitsWarning) xUnits = yAxis.getUnits();
            Rectangle window = DasDevicePosition.toRectangle(yAxis.getRow(), xAxis.getColumn());
            int buffer = (int) Math.ceil(Math.max(getLineWidth(), 10));
            DasPlot lparent = parent;
            if (lparent == null) return;
            if (lparent.isOverSize()) {
                window = new Rectangle(window.x - window.width / 3, window.y - buffer, 5 * window.width / 3, window.height + 2 * buffer);
            } else {
                window = new Rectangle(window.x - buffer, window.y - buffer, window.width + 2 * buffer, window.height + 2 * buffer);
            }
            if (lastIndex - firstIndex == 0) {
                this.path1 = null;
                return;
            }
            int pathLengthApprox = Math.max(5, 110 * (lastIndex - firstIndex) / 100);
            GeneralPath newPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, pathLengthApprox);
            Datum sw = SemanticOps.guessXTagWidth(xds, vds);
            double xSampleWidth;
            boolean logStep;
            if (sw != null) {
                if (UnitsUtil.isRatiometric(sw.getUnits())) {
                    xSampleWidth = sw.doubleValue(Units.logERatio);
                    logStep = true;
                } else {
                    xSampleWidth = sw.doubleValue(xUnits.getOffsetUnits());
                    logStep = false;
                }
            } else {
                xSampleWidth = 1e37;
                logStep = false;
            }
            double xSampleWidthExact = xSampleWidth;
            xSampleWidth = xSampleWidth * 1.20;
            double x = Double.NaN;
            double y = Double.NaN;
            double x0 = Double.NaN;
            double y0 = Double.NaN;
            float fx = Float.NaN;
            float fy = Float.NaN;
            float fx0 = Float.NaN;
            float fy0 = Float.NaN;
            boolean visible;
            boolean visible0;
            int index;
            index = firstIndex;
            x = (double) xds.value(index);
            y = (double) vds.value(index);
            logger.log(Level.FINE, "firstPoint moveTo,LineTo= {0},{1}", new Object[] { x, y });
            fx = (float) xAxis.transform(x, xUnits);
            fy = (float) yAxis.transform(y, yUnits);
            visible0 = window.contains(fx, fy);
            visible = visible0;
            if (histogram) {
                float fx1 = midPoint(xAxis, x, xUnits, xSampleWidthExact, logStep, -0.5);
                newPath.moveTo(fx1, fy);
                newPath.lineTo(fx, fy);
            } else {
                newPath.moveTo(fx, fy);
                newPath.lineTo(fx, fy);
            }
            x0 = x;
            y0 = y;
            fx0 = fx;
            fy0 = fy;
            index++;
            QDataSet ydc = ArrayDataSet.copy(vds);
            boolean ignoreCadence = !cadenceCheck;
            boolean isValid = false;
            for (; index < lastIndex; index++) {
                x = xds.value(index);
                y = vds.value(index);
                isValid = wds.value(index) > 0 && xUnits.isValid(x);
                fx = (float) xAxis.transform(x, xUnits);
                fy = (float) yAxis.transform(y, yUnits);
                visible = isValid && window.intersectsLine(fx0, fy0, fx, fy);
                if (isValid) {
                    double step = logStep ? Math.log(x / x0) : x - x0;
                    if (ignoreCadence || step < xSampleWidth) {
                        if (histogram) {
                            float fx1 = (fx0 + fx) / 2;
                            newPath.lineTo(fx1, fy0);
                            newPath.lineTo(fx1, fy);
                            newPath.lineTo(fx, fy);
                        } else {
                            if (visible) {
                                if (!visible0) {
                                    newPath.moveTo(fx0, fy0);
                                }
                                newPath.lineTo(fx, fy);
                            }
                        }
                    } else {
                        if (histogram) {
                            float fx1 = (float) xAxis.transform(x0 + xSampleWidthExact / 2, xUnits);
                            newPath.lineTo(fx1, fy0);
                            fx1 = (float) xAxis.transform(x - xSampleWidthExact / 2, xUnits);
                            newPath.moveTo(fx1, fy);
                            newPath.lineTo(fx, fy);
                        } else {
                            if (visible) {
                                newPath.moveTo(fx, fy);
                                newPath.lineTo(fx, fy);
                            }
                        }
                    }
                    x0 = x;
                    y0 = y;
                    fx0 = fx;
                    fy0 = fy;
                    visible0 = visible;
                } else {
                    if (visible0) {
                        if (histogram) {
                            float fx1 = midPoint(xAxis, x0, xUnits, xSampleWidthExact, logStep, 0.5);
                            newPath.lineTo(fx1, fy0);
                        } else {
                            newPath.moveTo(fx0, fy0);
                        }
                    }
                }
            }
            if (histogram) {
                if (isValid) {
                    fx = (float) xAxis.transform(x + xSampleWidthExact / 2, xUnits);
                    newPath.lineTo(fx, fy0);
                }
            }
            if (!histogram && simplifyPaths && colorByDataSetId.length() == 0) {
                this.path1 = new GeneralPath(GeneralPath.WIND_NON_ZERO, pathLengthApprox);
                int count = GraphUtil.reducePath(newPath.getPathIterator(null), path1);
                logger.fine(String.format("reduce path in=%d  out=%d\n", lastIndex - firstIndex, count));
            } else {
                this.path1 = newPath;
            }
        }
