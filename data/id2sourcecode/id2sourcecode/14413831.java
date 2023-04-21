    public DjaControl[] getControls() {
        final int x = (xbegin_ + xend_) / 2;
        final int y = (ybegin_ + yend_ - himage_) / 2 - yimage_;
        final DjaControl[] r = new DjaControl[] { new PC(this, 0, VERTICAL, x, y - 3) };
        return r;
    }
