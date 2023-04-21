        public RGB[] calibrate() {
            final double step = maxValue.getStepIn255();
            final double[] targetJNDIArray = jndi.getTargetJNDICurve();
            final boolean[] calibrated = new boolean[256];
            calibrated[0] = calibrated[255] = true;
            for (int x = 1; x < 255; x++) {
                final int index = x;
                new Thread() {

                    private StringBuilder buf = new StringBuilder();

                    public void run() {
                        double delta = deltaArray[index];
                        if (delta != 0) {
                            buf.append(index + " start adjust (" + calibratedRGBArray[index] + ")\n");
                        } else {
                            calibrated[index] = true;
                        }
                        while (delta != 0) {
                            RGB measureRGB = (RGB) calibratedRGBArray[index].clone();
                            double adjuststep = delta > 0 ? -step : step;
                            measureRGB.addValues(adjuststep);
                            if (calibratedChannel != RGBBase.Channel.W) {
                                measureRGB.reserveValue(calibratedChannel);
                            }
                            if (!measureRGB.isLegal()) {
                                buf.append(index + " abnormal adjust end\n");
                                calibrated[index] = true;
                                break;
                            }
                            Patch p = mi.measure(measureRGB);
                            buf.append(index + " measure " + p + '\n');
                            double JNDI = getJNDI(p.getXYZ());
                            double newdelta = JNDI - targetJNDIArray[index];
                            if (newdelta * delta < 0) {
                                if (Math.abs(delta) > Math.abs(newdelta)) {
                                    calibratedRGBArray[index] = measureRGB;
                                }
                                buf.append(index + " adjust end (" + calibratedRGBArray[index] + ")\n");
                                calibrated[index] = true;
                                break;
                            }
                            delta = newdelta;
                            calibratedRGBArray[index] = measureRGB;
                        }
                        traceDetail(buf.toString());
                    }
                }.start();
            }
            mi.triggerMeasure(new Trigger(calibrated));
            traceDetail("measure end");
            return calibratedRGBArray;
        }
