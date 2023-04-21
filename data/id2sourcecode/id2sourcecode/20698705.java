    public static void showText(Graphics g, String s, int x, int y, int width, int height, int fontSize, int fontStyle, int textColor) {
        TextList paintList = new TextList(null);
        paintList.setFontSize(fontSize);
        paintList.addBigTextInternal(s, textColor, fontStyle, -1, width);
        int line, textHeight = 0;
        int linesCount = paintList.getSize();
        for (line = 0; line < linesCount; line++) textHeight += paintList.getLine(line).getHeight(fontSize);
        int top = y + (height - textHeight) / 2;
        for (line = 0; line < linesCount; line++) {
            paintList.getLine(line).paint(x, top, g, fontSize, paintList);
            top += paintList.getLine(line).getHeight(fontSize);
        }
    }
