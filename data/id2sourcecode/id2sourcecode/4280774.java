    protected void sampledAtUnitTime(float unitTime, int repeatIteration) {
        AnimatableValue value, accumulation, nextValue;
        float interpolation = 0;
        if (unitTime != 1) {
            int keyTimeIndex = 0;
            while (keyTimeIndex < keyTimes.length - 1 && unitTime >= keyTimes[keyTimeIndex + 1]) {
                keyTimeIndex++;
            }
            value = values[keyTimeIndex];
            if (calcMode == CALC_MODE_LINEAR || calcMode == CALC_MODE_PACED || calcMode == CALC_MODE_SPLINE) {
                nextValue = values[keyTimeIndex + 1];
                interpolation = (unitTime - keyTimes[keyTimeIndex]) / (keyTimes[keyTimeIndex + 1] - keyTimes[keyTimeIndex]);
                if (calcMode == CALC_MODE_SPLINE && unitTime != 0) {
                    Cubic c = keySplineCubics[keyTimeIndex];
                    float tolerance = 0.001f;
                    float min = 0;
                    float max = 1;
                    Point2D.Double p;
                    for (; ; ) {
                        float t = (min + max) / 2;
                        p = c.eval(t);
                        double x = p.getX();
                        if (Math.abs(x - interpolation) < tolerance) {
                            break;
                        }
                        if (x < interpolation) {
                            min = t;
                        } else {
                            max = t;
                        }
                    }
                    interpolation = (float) p.getY();
                }
            } else {
                nextValue = null;
            }
        } else {
            value = values[values.length - 1];
            nextValue = null;
        }
        if (cumulative) {
            accumulation = values[values.length - 1];
        } else {
            accumulation = null;
        }
        this.value = value.interpolate(this.value, nextValue, interpolation, accumulation, repeatIteration);
        if (this.value.hasChanged()) {
            markDirty();
        }
    }
