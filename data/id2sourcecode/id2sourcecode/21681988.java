    protected Rectangle calculateRect(int xPos, int yPos) {
        int halfWidth = (celWidth + 1) / 2;
        int halfHeight = (celHeight + 1) / 2;
        int x = (int) (xPos - camera.getPosition().x);
        int y = (int) (yPos - camera.getPosition().y);
        return new Rectangle(x - halfWidth - 1, y - halfHeight - 1, celWidth + 1, celHeight + 1);
    }
