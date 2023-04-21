    public void render(GamePanel gamePanel, Object object, int x, int y, int width, int height, int layer) {
        Boolean bool = (Boolean) object;
        if (!bool) {
            TileMapWrapper map = Source.getInstance().getCurrentMap();
            Graphics2D buffer = gamePanel.getBackBuffer();
            buffer.setColor(LayerColour.getInstance().getColour(layer));
            Camera camera = gamePanel.getCamera();
            int halfWidth = (width + 1) / 2;
            int halfHeight = (height + 1) / 2;
            x -= camera.getPosition().x;
            y -= camera.getPosition().y;
            if (layer % 2 == 0) {
                int count = map.celWidth / 10;
                for (int i = 0; i < count; i++) {
                    int left = x + layer * 2 - halfWidth + i * 10;
                    int top = y - halfHeight;
                    buffer.drawLine(left, top, left, top + height - 1);
                }
            } else {
                int count = map.celHeight / 10;
                for (int i = 0; i < count; i++) {
                    int left = x - halfWidth;
                    int top = y + layer * 2 - halfHeight + i * 10;
                    buffer.drawLine(left, top, left + width - 1, top);
                }
            }
        }
    }
