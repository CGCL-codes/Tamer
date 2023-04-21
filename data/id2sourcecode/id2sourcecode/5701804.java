    void scrollRegionUp(int beg, int end) {
        for (int j = beg; j < end; j++) {
            screen[j] = screen[j + 1];
            screenfg[j] = screenfg[j + 1];
            screenbg[j] = screenbg[j + 1];
            lineRedraw[j] = true;
        }
        screen[end] = new char[columns];
        screenfg[end] = new Color[columns];
        screenbg[end] = new Color[columns];
        for (int j = 0; j < columns; j++) {
            screenfg[end][j] = fgcolor;
            screenbg[end][j] = bgcolor;
        }
        lineRedraw[end] = true;
    }
