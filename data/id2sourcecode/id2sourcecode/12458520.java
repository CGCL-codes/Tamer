    private static void initPalette() {
        ColorModel cm = robby.createScreenCapture(new Rectangle(0, 0, 1, 1)).getColorModel();
        String s = "'";
        s += colorletter(cm, 0x000000);
        s += colorletter(cm, 0x000080);
        s += colorletter(cm, 0x008000);
        s += colorletter(cm, 0x008080);
        s += colorletter(cm, 0x800000);
        s += colorletter(cm, 0x800080);
        s += colorletter(cm, 0x808000);
        s += colorletter(cm, 0x7f7f7f);
        s += colorletter(cm, 0xbfbfbf);
        s += colorletter(cm, 0x0000ff);
        s += colorletter(cm, 0x00ff00);
        s += colorletter(cm, 0x00ffff);
        s += colorletter(cm, 0xff0000);
        s += colorletter(cm, 0xff00ff);
        s += colorletter(cm, 0xffff00);
        s += colorletter(cm, 0xffffff);
        s += "'";
        addLine("'0123456789abcdef' Turbo palette");
        addLine(s);
    }
