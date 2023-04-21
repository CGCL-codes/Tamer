    public void setSlider() {
        int min = iStartPosition;
        int max = iStartPosition + iNumberOfElements - 1;
        int init = (max + 1) / 2;
        positionSlider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        positionSlider.setMajorTickSpacing(5);
        positionSlider.setMinorTickSpacing(1);
        positionSlider.setPaintTicks(true);
        positionSlider.setPaintLabels(true);
        positionSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        positionSlider.updateUI();
    }
