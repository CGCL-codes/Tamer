    private Color getTitleTextColor() {
        int greyTop = ColorUtils.getGreyValue(titleTopColor());
        int greyBottom = ColorUtils.getGreyValue(titleBottomColor());
        int greyAvg = (greyTop + greyBottom) / 2;
        return (greyAvg > 128 ? Color.black : Color.white);
    }
