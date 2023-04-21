        public void update(DasAxis xAxis, DasAxis yAxis, QDataSet dataSet, ProgressMonitor mon) {
            if (lastIndex - firstIndex == 0) {
                this.fillToRefPath1 = null;
                return;
            }
            QDataSet xds = SemanticOps.xtagsDataSet(dataSet);
            QDataSet vds;
            vds = ytagsDataSet(ds);
            Units xUnits = SemanticOps.getUnits(xds);
            Units yUnits = SemanticOps.getUnits(vds);
            if (unitsWarning) yUnits = yAxis.getUnits();
            if (xunitsWarning) xUnits = xAxis.getUnits();
            QDataSet wds = SemanticOps.weightsDataSet(vds);
            int pathLengthApprox = Math.max(5, 110 * (lastIndex - firstIndex) / 100);
            GeneralPath fillPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, pathLengthApprox);
            Datum sw = SemanticOps.guessXTagWidth(xds, dataSet);
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
            if (reference != null && reference.getUnits() != yAxis.getUnits()) {
                reference = yAxis.getUnits().createDatum(reference.doubleValue(reference.getUnits()));
            }
            if (reference == null) {
                reference = yUnits.createDatum(yAxis.isLog() ? 1.0 : 0.0);
            }
            double yref = (double) reference.doubleValue(yUnits);
            double x = Double.NaN;
            double y = Double.NaN;
            double x0 = Double.NaN;
            double y0 = Double.NaN;
            float fyref = (float) yAxis.transform(yref, yUnits);
            float fx = Float.NaN;
            float fy = Float.NaN;
            float fx0 = Float.NaN;
            float fy0 = Float.NaN;
            int index;
            index = firstIndex;
            x = (double) xds.value(index);
            y = (double) vds.value(index);
            fx = (float) xAxis.transform(x, xUnits);
            fy = (float) yAxis.transform(y, yUnits);
            if (histogram) {
                float fx1;
                fx1 = midPoint(xAxis, x, xUnits, xSampleWidthExact, logStep, -0.5);
                fillPath.moveTo(fx1 - 1, fyref);
                fillPath.lineTo(fx1 - 1, fy);
                fillPath.lineTo(fx, fy);
            } else {
                fillPath.moveTo(fx, fyref);
                fillPath.lineTo(fx, fy);
            }
            x0 = x;
            y0 = y;
            fx0 = fx;
            fy0 = fy;
            if (psymConnector != PsymConnector.NONE || fillToReference) {
                boolean ignoreCadence = !cadenceCheck;
                for (; index < lastIndex; index++) {
                    x = xds.value(index);
                    y = vds.value(index);
                    final boolean isValid = wds.value(index) > 0 && xUnits.isValid(x);
                    fx = (float) xAxis.transform(x, xUnits);
                    fy = (float) yAxis.transform(y, yUnits);
                    if (isValid) {
                        double step = logStep ? Math.log(x / x0) : x - x0;
                        if (ignoreCadence || step < xSampleWidth) {
                            if (histogram) {
                                float fx1 = (fx0 + fx) / 2;
                                fillPath.lineTo(fx1, fy0);
                                fillPath.lineTo(fx1, fy);
                                fillPath.lineTo(fx, fy);
                            } else {
                                fillPath.lineTo(fx, fy);
                            }
                        } else {
                            if (histogram) {
                                float fx1 = midPoint(xAxis, x0, xUnits, xSampleWidthExact, logStep, 0.5);
                                fillPath.lineTo(fx1, fy0);
                                fillPath.lineTo(fx1, fyref);
                                fx1 = midPoint(xAxis, x, xUnits, xSampleWidthExact, logStep, -0.5);
                                fillPath.moveTo(fx1, fyref);
                                fillPath.lineTo(fx1, fy);
                                fillPath.lineTo(fx, fy);
                            } else {
                                fillPath.lineTo(fx0, fyref);
                                fillPath.moveTo(fx, fyref);
                                fillPath.lineTo(fx, fy);
                            }
                        }
                        x0 = x;
                        y0 = y;
                        fx0 = fx;
                        fy0 = fy;
                    }
                }
            }
            if (histogram) {
                float fx1 = midPoint(xAxis, x0, xUnits, xSampleWidthExact, logStep, 0.5);
                fillPath.lineTo(fx1, fy0);
                fillPath.lineTo(fx1, fyref);
            } else {
                fillPath.lineTo(fx0, fyref);
            }
            this.fillToRefPath1 = fillPath;
            if (simplifyPaths) {
                GeneralPath newPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, pathLengthApprox);
                int count = GraphUtil.reducePath(fillToRefPath1.getPathIterator(null), newPath);
                fillToRefPath1 = newPath;
                logger.fine(String.format("reduce path(fill) in=%d  out=%d\n", lastIndex - firstIndex, count));
            }
        }
