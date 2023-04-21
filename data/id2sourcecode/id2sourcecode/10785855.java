    void updateSlider(float min, float max) {
        float val = slider.getValue();
        if (val != val || val <= min || val >= max) {
            val = (min + max) / 2;
        }
        slider.setBounds(min, max, val);
    }
