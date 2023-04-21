    public CursorBar(double _min, double _max, double _val) {
        minBar = (float) _min;
        maxBar = (float) _max;
        double tmpVal = _val;
        if (tmpVal < minBar && tmpVal > maxBar) tmpVal = (minBar + maxBar) / 2;
        setSize(100, 15);
        cursor.setSize(8, getSizeY());
        setValue(tmpVal);
        oldPos = cursor.getPos();
    }
