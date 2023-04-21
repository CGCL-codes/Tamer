    @Override
    protected void onBoundsChange(Rect bounds) {
        int left = bounds.left;
        int top = bounds.top;
        int right = bounds.right;
        int height = bounds.bottom - top;
        Config config = mConfig;
        int iconLeft = left + config.mIconLeft;
        int iconSize = config.mIconSize;
        Drawable icon = mIcon;
        if (icon != null) {
            int iconY = top + (height - iconSize) / 2;
            icon.setBounds(iconLeft, iconY, iconLeft + iconSize, top + iconSize);
        }
        int outerWidth = right - config.mTitleLeft;
        String title = mTitle;
        mTitleLayout = new StaticLayout(title, 0, title.length(), config.mPaint, outerWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true, TextUtils.TruncateAt.MIDDLE, outerWidth);
        mTitleY = top + (height - mTitleLayout.getHeight()) / 2;
    }
