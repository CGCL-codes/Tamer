    public void render(GamePanel gamePanel, Object object, int x, int y, int width, int height, int layer) {
        Color color = (Color) object;
        if (color.getAlpha() == 255) {
            Graphics2D buffer = gamePanel.getBackBuffer();
            buffer.setColor(color);
            Camera camera = gamePanel.getCamera();
            int halfWidth = (width + 1) / 2;
            int halfHeight = (height + 1) / 2;
            x -= camera.getPosition().x;
            y -= camera.getPosition().y;
            int left = x - halfWidth;
            int top = y - halfHeight;
            buffer.fillRect(left, top, width, height);
        }
    }
