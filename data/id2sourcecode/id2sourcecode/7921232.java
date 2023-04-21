    private static void drawLine(GC gc, int x, int y, int width, String string, int halign) {
        int xx;
        int textWidth = SwtGraphicsHelper.getStringDrawingWidth(gc, string);
        switch(halign) {
            case LEFT:
                xx = x;
                break;
            case RIGHT:
                xx = x + (width - textWidth);
                break;
            case CENTER:
                xx = x + (width - textWidth) / 2;
                break;
            default:
                throw new RuntimeException("illegal alignment");
        }
        gc.drawText(string, xx, y, true);
    }
