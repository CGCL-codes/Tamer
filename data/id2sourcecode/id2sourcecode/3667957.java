    public int getTabCount() {
        int children = getChildCount();
        if (mDividerDrawable != null) {
            children = (children + 1) / 2;
        }
        return children;
    }
