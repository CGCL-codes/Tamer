    public void render(Graphics2D g2) {
        int yp;
        int xp;
        int internalYMargin = 2;
        int internalXMargin = 10;
        final Font nameFont = new Font("Arial", 0, 9);
        int left = x - width / 2;
        int right = x + width / 2;
        int top = y - height / 2;
        int bottom = y + height / 2;
        int centre = (left + right) / 2;
        Font accessionFont = new Font("Arial", 0, 8);
        FontRenderContext frc = new FontRenderContext(null, true, false);
        g2.setFont(accessionFont);
        g2.setColor(fill);
        g2.fillRect(x - width / 2, y - height / 2, width, height);
        g2.setColor(line);
        g2.setStroke(border);
        g2.drawRect(x - width / 2, y - height / 2, width, height);
        Rectangle2D r = accessionFont.getStringBounds(text, frc);
        LineMetrics lm = accessionFont.getLineMetrics("X", frc);
        g2.setColor(Color.BLACK);
        yp = top;
        g2.setColor(Color.blue);
        yp += internalYMargin;
        yp += lm.getAscent();
        g2.setFont(nameFont);
        yp += lm.getDescent() + lm.getLeading();
        g2.setColor(Color.black);
        Map attrs = new HashMap();
        attrs.put(TextAttribute.FONT, nameFont);
        LineBreakMeasurer measurer = new LineBreakMeasurer(new AttributedString(text, attrs).getIterator(), frc);
        float wrappingWidth = right - left - internalXMargin + 8;
        xp = centre;
        int limit = 4, count = 0;
        while (measurer.getPosition() < text.length()) {
            count++;
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            yp += (layout.getAscent());
            layout.draw(g2, xp - layout.getAdvance() / 2, yp - 6);
            yp += layout.getDescent() + layout.getLeading();
        }
    }
