    public void mapChanged(ScalarMapEvent e) {
        if (!autoScale) return;
        double[] range = map.getRange();
        sMinimum = (float) range[0];
        sMaximum = (float) range[1];
        if (integralValues) {
            int tmp = (int) (sMaximum - sMinimum);
            if (tmp != sTicks) {
                sTicks = tmp;
                slider.setMaximum(sTicks);
            }
        }
        sCurrent = (float) control.getValue();
        if (sCurrent < sMinimum || sCurrent > sMaximum) {
            sCurrent = (sMinimum + sMaximum) / 2;
        }
        updateSlider(sCurrent);
    }
