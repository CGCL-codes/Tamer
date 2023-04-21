    public void callback() {
        try {
            int sliderData = bs.getChannel(0);
            int sliderDataRange = sliderData >> 10;
            count++;
            if (sliderDataRange != lastSliderRange) {
                lastSliderRange = sliderDataRange;
                postEvent(sliderData);
                if (DEBUG) System.out.println("Data: " + sliderData + "\tCount: " + count);
                count = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
