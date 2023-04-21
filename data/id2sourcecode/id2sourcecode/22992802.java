    protected void sampledAtUnitTime(float unitTime, int repeatIteration) {
        AnimatableValue value, accumulation;
        float interpolation = 0;
        if (unitTime != 1) {
            int keyTimeIndex = 0;
            while (keyTimeIndex < keyTimes.length - 1 && unitTime >= keyTimes[keyTimeIndex + 1]) {
                keyTimeIndex++;
            }
            if (keyTimeIndex == keyTimes.length - 1 && calcMode == CALC_MODE_DISCRETE) {
                keyTimeIndex = keyTimes.length - 2;
                interpolation = 1;
            } else {
                if (calcMode == CALC_MODE_LINEAR || calcMode == CALC_MODE_PACED || calcMode == CALC_MODE_SPLINE) {
                    if (unitTime == 0) {
                        interpolation = 0;
                    } else {
                        interpolation = (unitTime - keyTimes[keyTimeIndex]) / (keyTimes[keyTimeIndex + 1] - keyTimes[keyTimeIndex]);
                    }
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
                }
            }
            float point = keyPoints[keyTimeIndex];
            if (interpolation != 0) {
                point += interpolation * (keyPoints[keyTimeIndex + 1] - keyPoints[keyTimeIndex]);
            }
            point *= pathLength.lengthOfPath();
            Point2D p = pathLength.pointAtLength(point);
            float ang;
            if (rotateAuto) {
                ang = pathLength.angleAtLength(point);
                if (rotateAutoReverse) {
                    ang += Math.PI;
                }
            } else {
                ang = rotateAngle;
            }
            value = new AnimatableMotionPointValue(null, (float) p.getX(), (float) p.getY(), ang);
        } else {
            Point2D p = pathLength.pointAtLength(pathLength.lengthOfPath());
            float ang;
            if (rotateAuto) {
                ang = pathLength.angleAtLength(pathLength.lengthOfPath());
                if (rotateAutoReverse) {
                    ang += Math.PI;
                }
            } else {
                ang = rotateAngle;
            }
            value = new AnimatableMotionPointValue(null, (float) p.getX(), (float) p.getY(), ang);
        }
        if (cumulative) {
            Point2D p = pathLength.pointAtLength(pathLength.lengthOfPath());
            float ang;
            if (rotateAuto) {
                ang = pathLength.angleAtLength(pathLength.lengthOfPath());
                if (rotateAutoReverse) {
                    ang += Math.PI;
                }
            } else {
                ang = rotateAngle;
            }
            accumulation = new AnimatableMotionPointValue(null, (float) p.getX(), (float) p.getY(), ang);
        } else {
            accumulation = null;
        }
        this.value = value.interpolate(this.value, null, interpolation, accumulation, repeatIteration);
        if (this.value.hasChanged()) {
            markDirty();
        }
    }
