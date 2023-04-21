    private Color getTitleTextColor() {
        int greyTop = ColorUtils.getGreyValue(TOP_TITLE);
        int greyBottom = ColorUtils.getGreyValue(BOTTOM_TITLE);
        int greyAvg = (greyTop + greyBottom) / 2;
        return (greyAvg > 128 ? Color.black : Color.white);
    }
