    public void setSliderBounds() {
        int min = 1;
        int max = iPeptideExample.length() - 1;
        int init = (max + 1) / 2;
        negativeSplit.setMinimum(min);
        negativeSplit.setMaximum(max);
        negativeSplit.setValue(init);
        negativeSplit.setMajorTickSpacing(5);
        negativeSplit.setMinorTickSpacing(1);
        negativeSplit.setPaintTicks(true);
        negativeSplit.setPaintLabels(true);
        negativeSplit.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    }
