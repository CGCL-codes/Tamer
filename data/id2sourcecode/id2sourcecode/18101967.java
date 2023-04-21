    public void drawIcon() {
        if (icon == null) return;
        Graphics2D g2 = menu.buff.g2;
        float rMid = (rLo + rHi) / 2;
        float imgDiag = (float) Math.sqrt(icon.width * icon.width + icon.height * icon.height);
        hintSize = (rHi - rLo) / imgDiag;
        hintSize = Math.min(hintSize, (float) Math.sin(tHi - tLo) * rMid);
        hintSize *= 0.8f;
        float imgW = icon.width * hintSize;
        float imgH = icon.height * hintSize;
        float midX = (innerX + outerX) / 2f;
        float midY = (innerY + outerY) / 2f;
        if (!isEnabled()) menu.canvas.tint(200);
        menu.canvas.image(icon, midX - imgW / 2, midY - imgH / 2, imgW, imgH);
        if (!isEnabled()) menu.canvas.noTint();
    }
