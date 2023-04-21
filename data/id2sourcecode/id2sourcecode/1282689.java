    public boolean computeTagValue(double time) {
        boolean isDifferentValue = false;
        Object lastTagValue = tagValue;
        if (type.equals(AnimationsToolkit.CONSTANT_FUNCTION)) {
            if (hasChanged) {
                setTagValue(value);
            }
        } else {
            double val = Double.NaN;
            if (!Double.isNaN(period) && period != 0) {
                if (tagType != TagToolkit.ANALOGIC_FLOAT && tagType != TagToolkit.ANALOGIC_INTEGER) {
                    synchronized (this) {
                        initialValue = 0;
                    }
                }
                if (type.equals(AnimationsToolkit.SIN_FUNCTION)) {
                    val = AnimationsToolkit.sin(time, initialValue, period);
                } else if (type.equals(AnimationsToolkit.TRIANGLE_FUNCTION)) {
                    val = AnimationsToolkit.triangle(time, initialValue, period);
                } else if (type.equals(AnimationsToolkit.RAMP_FUNCTION)) {
                    val = AnimationsToolkit.ramp(time, initialValue, period);
                }
            }
            if ((tagType == TagToolkit.ANALOGIC_FLOAT || tagType == TagToolkit.ANALOGIC_INTEGER) && !Double.isNaN(min) && !Double.isNaN(max) && !Double.isNaN(initialValue)) {
                if (min < max) {
                    val = ((val + 1) / 2) * Math.abs(max - min) + min;
                    Object oVal = val;
                    if (tagType == TagToolkit.ANALOGIC_INTEGER) {
                        oVal = (int) val;
                    }
                    setTagValue(oVal);
                } else if (min == max) {
                    val = min;
                    Object oVal = val;
                    if (tagType == TagToolkit.ANALOGIC_INTEGER) {
                        oVal = (int) val;
                    }
                    setTagValue(oVal);
                }
            } else if (tagType == TagToolkit.ENUMERATED && enumeratedValues.size() > 1) {
                double base = 1 / ((double) enumeratedValues.size());
                val = (val + 1) / 2;
                int index = (int) Math.floor(val / base);
                if (index == enumeratedValues.size()) {
                    index = enumeratedValues.size() - 1;
                }
                String strValue = enumeratedValues.get(index);
                if (strValue == null || strValue.equals(invalidLabel)) {
                    setTagValue(null);
                } else {
                    setTagValue(strValue);
                }
            } else if (tagType == TagToolkit.STRING) {
                setTagValue(tagValue);
            } else {
                setTagValue(null);
            }
        }
        if (lastTagValue == null || tagValue == null) {
            if (lastTagValue != null || tagValue != null) {
                isDifferentValue = true;
            }
        } else if (!lastTagValue.equals(tagValue)) {
            isDifferentValue = true;
        }
        lastTagValue = tagValue;
        return isDifferentValue;
    }
