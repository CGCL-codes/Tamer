    public void render(GameContainer container, Graphics g) throws SlickException {
        if (mapRenderer != null) mapRenderer.render(g); else g.drawString("Startup complete, press any key to write/read and render the map", 10, 10);
    }
