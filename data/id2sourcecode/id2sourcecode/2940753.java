    public void renderText(IGraphics g, int x, int y) {
        if (Lable == null) return;
        int vw = W - UserRect.BorderSize * 2 - (Icon != null ? H : 0);
        int tw = g.getStringWidth(Lable);
        int th = g.getStringHeight();
        int dy = y + (H - th) / 2;
        if (TextColor == 0 || Lable == null) return;
        if (--RollTime < 0) {
            RollPos += RollSpeed;
        } else {
            RollPos = -vw;
        }
        if (vw < tw) {
            int size = (tw + vw);
            int sx = x + UserRect.BorderSize + (Icon != null ? H : 0);
            if (size > 0) {
                int cx = g.getClipX();
                int cw = g.getClipWidth();
                g.setClip(sx, g.getClipY(), vw, g.getClipHeight());
                int dx = sx + vw + RollPos % size;
                if (IsTextShadow) {
                    g.setColor(TextColorShadow);
                    g.drawString(Lable, dx + TextShadowPosX, dy + TextShadowPosY);
                }
                g.setColor(TextColor);
                g.drawString(Lable, dx, dy);
                g.setClip(cx, g.getClipY(), cw, g.getClipHeight());
            }
        } else {
            if (IsTextCenter) {
                int dx = x + (Icon != null ? H : 0) + (W - (Icon != null ? H : 0) - tw) / 2;
                if (IsTextShadow) {
                    g.setColor(TextColorShadow);
                    g.drawString(Lable, dx + TextShadowPosX, dy + TextShadowPosY);
                }
                g.setColor(TextColor);
                g.drawString(Lable, dx, dy);
            } else {
                int dx = x + (Icon != null ? H : 0) + UserRect.BorderSize;
                if (IsTextShadow) {
                    g.setColor(TextColorShadow);
                    g.drawString(Lable, dx + TextShadowPosX, dy + TextShadowPosY);
                }
                g.setColor(TextColor);
                g.drawString(Lable, dx, dy);
            }
        }
    }
