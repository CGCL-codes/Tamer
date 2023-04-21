    private static void initScreen(boolean used) {
        int x = screen.width;
        int y = screen.height;
        int w = x / 80;
        int h = y / 60;
        int dw = w / 4;
        int dh = h / 4;
        if (os.isMsWindows()) {
            dw = 1;
            dh = 1;
        }
        screenbutton = getRows("screenbutton", robby.createScreenCapture(new Rectangle(x - w - dw, dh, w, h)));
        closebutton = getRows("closebutton", robby.createScreenCapture(new Rectangle(x - w - dw, dh, w, h)));
        if (used) {
            addLine("Calibrating...");
            initPalette();
            createCanvas();
            addLine("+-canvas-+");
            for (int i = 0; i < canvastile.height; i++) addLine("| " + canvastile.getScanline(i) + " |");
            addLine("+--------+");
            addLine(canvasline.toString());
            addLine(canvascolumn.toString());
            String s = "close button";
            while (s.length() < w) s += "-";
            s = "+-" + s + "-+";
            addLine(s);
            for (int i = 0; i < closebutton.height; i++) {
                addLine("| " + closebutton.getScanline(i) + " |");
            }
            setFocusable(false);
            s = "";
            while (s.length() < w) s += "-";
            s = "+-" + s + "-+";
            addLine(s);
        }
    }
