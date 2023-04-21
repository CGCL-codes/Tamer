    protected void drawSegment(int x0, int y0, int x1, int y1, String strokeColor, int strokeWidth) {
        int tmpX = Math.min(x0, x1);
        int tmpY = Math.min(y0, y1);
        int width = Math.max(x0, x1) - tmpX;
        int height = Math.max(y0, y1) - tmpY;
        x0 = tmpX;
        y0 = tmpY;
        if (width == 0 || height == 0) {
            String s = "overflow:hidden;position:absolute;" + "left:" + String.valueOf(x0) + "px;" + "top:" + String.valueOf(y0) + "px;" + "width:" + String.valueOf(width) + "px;" + "height:" + String.valueOf(height) + "px;" + "border-color:" + strokeColor + ";" + "border-style:solid;" + "border-width:1 1 0 0px;";
            Element elem = document.createElement("div");
            elem.setAttribute("style", s);
            appendHtmlElement(elem);
        } else {
            int x = x0 + (x1 - x0) / 2;
            drawSegment(x0, y0, x, y0, strokeColor, strokeWidth);
            drawSegment(x, y0, x, y1, strokeColor, strokeWidth);
            drawSegment(x, y1, x1, y1, strokeColor, strokeWidth);
        }
    }
