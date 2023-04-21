    protected synchronized void drawTouchZone(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();
        if (gpsSymbol == null) return;
        symbolXyzRelativeToCameraView.get(symbolArray);
        textXyzRelativeToCameraView.get(textArray);
        float x1 = symbolArray[0];
        float y1 = symbolArray[1];
        float x2 = textArray[0];
        float y2 = textArray[1];
        float width = getWidth();
        float height = getHeight();
        float adjX = (x1 + x2) / 2;
        float adjY = (y1 + y2) / 2;
        float currentAngle = Utilities.getAngle(symbolArray[0], symbolArray[1], textArray[0], textArray[1]) + 90;
        adjX -= (width / 2);
        adjY -= (gpsSymbol.getHeight() / 2);
        Log.w("touchBox", "ul (x=" + (adjX) + " y=" + (adjY) + ")");
        Log.w("touchBox", "ur (x=" + (adjX + width) + " y=" + (adjY) + ")");
        Log.w("touchBox", "ll (x=" + (adjX) + " y=" + (adjY + height) + ")");
        Log.w("touchBox", "lr (x=" + (adjX + width) + " y=" + (adjY + height) + ")");
        if (touchBox == null) touchBox = new PaintableBox(width, height); else touchBox.set(width, height);
        if (touchPosition == null) touchPosition = new PaintablePosition(touchBox, adjX, adjY, currentAngle, 1); else touchPosition.set(touchBox, adjX, adjY, currentAngle, 1);
        touchPosition.paint(canvas);
    }
