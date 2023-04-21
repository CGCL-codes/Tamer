    public void renderText(IGraphics g, int x, int y) {
        if (Text == null) return;
        int vw = W - UserRect.BorderSize * 2;
        int tw = Text.getWidth();
        int th = Text.getHeight();
        int dy = y + (H - th) / 2;
        if (--RollTime < 0) {
            int old = RollPos;
            if ((old + RollSpeed) < -tw) {
                RollTotalCount++;
                RollPos = vw;
            }
            RollPos += RollSpeed;
        }
        int sx = x + UserRect.BorderSize;
        int cx = g.getClipX();
        int cw = g.getClipWidth();
        g.setClip(sx, g.getClipY(), vw, g.getClipHeight());
        {
            int dx = sx + RollPos;
            g.setColor(TextColor);
            Text.render(g, dx, dy);
        }
        g.setClip(cx, g.getClipY(), cw, g.getClipHeight());
    }
