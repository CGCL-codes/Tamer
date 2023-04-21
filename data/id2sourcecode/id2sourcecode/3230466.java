    public static Polygon makeDiamonHead(int startX, int startY, int endX, int endY, int widthHeightRatio) {
        int yChange = 0;
        int xChange = 0;
        int x_verts[] = new int[4];
        int y_verts[] = new int[4];
        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2;
        xChange = startX - endX;
        yChange = startY - endY;
        x_verts[0] = startX;
        y_verts[0] = startY;
        x_verts[1] = midX + yChange / widthHeightRatio;
        y_verts[1] = midY - xChange / widthHeightRatio;
        x_verts[2] = endX;
        y_verts[2] = endY;
        x_verts[3] = midX - yChange / widthHeightRatio;
        y_verts[3] = midY + xChange / widthHeightRatio;
        return new Polygon(x_verts, y_verts, 4);
    }
