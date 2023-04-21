    public static Point getTopLeftElement(int width, int height, Point centeredElement) {
        Point topleft = new Point(centeredElement);
        int down = (height + 1) / 2;
        down--;
        topleft.x -= down;
        topleft.y -= down;
        int right = (width + 1) / 2;
        right--;
        topleft.x -= right / 2;
        topleft.x -= right % 2;
        topleft.y += right / 2;
        return topleft;
    }
