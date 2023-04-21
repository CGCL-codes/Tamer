    private void setNumMajorTics(float axisLength) {
        int suggestedNumMajorTics = (int) (axisLength / 40 + 1);
        double axisRange = axisMax - axisMin;
        if (axisRangeEqualsDataRange) {
            if ((int) axisRange % 10 == 0) numMajorTics = 10 + 1; else if ((int) axisRange % 5 == 0) numMajorTics = 5 + 1; else if ((int) axisRange % 2 == 0) numMajorTics = 2 + 1;
        } else {
            if (axisRange >= 0 && axisRange <= 1) {
                double axisRangeExponent = (Math.floor(MathPlus.log10(axisRange)));
                double axisRangeInt = axisRange * Math.pow(10.0, (-1) * axisRangeExponent);
                numMajorTics = (int) axisRangeInt + 1;
            } else if (axisRange >= 1 && axisRange <= 10) {
                numMajorTics = (int) axisRange + 1;
            } else if (axisRange > 10) {
                double axisRangeExponent = (Math.floor(MathPlus.log10(axisRange)));
                double axisRangeInt = axisRange * Math.pow(10.0, (-1) * axisRangeExponent);
                numMajorTics = (int) axisRangeInt + 1;
            }
        }
        while ((2 * numMajorTics - 1) <= suggestedNumMajorTics) {
            numMajorTics = numMajorTics * 2 - 1;
        }
        while (numMajorTics > suggestedNumMajorTics) {
            numMajorTics = (numMajorTics + 1) / 2;
        }
    }
