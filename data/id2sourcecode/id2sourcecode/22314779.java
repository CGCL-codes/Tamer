    void scrValid() {
        if (xloc >= columns) if (wrap) {
            xloc = 0;
            yloc++;
        } else xloc = columns - 1;
        if (yloc >= lines) {
            for (int j = 0; j < lines - 1; j++) {
                screen[j] = screen[j + 1];
                screenfg[j] = screenfg[j + 1];
                screenbg[j] = screenbg[j + 1];
                lineRedraw[j] = true;
            }
            screen[lines - 1] = new char[columns];
            screenfg[lines - 1] = new Color[columns];
            screenbg[lines - 1] = new Color[columns];
            for (int j = 0; j < columns; j++) {
                screen[lines - 1][j] = ' ';
                screenfg[lines - 1][j] = fgcolor;
                screenbg[lines - 1][j] = bgcolor;
            }
            lineRedraw[lines - 1] = true;
            yloc--;
        }
    }
