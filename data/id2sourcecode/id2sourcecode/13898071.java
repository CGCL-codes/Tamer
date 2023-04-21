    public void drawInfo(Graphics g) {
        if (textGap != 0) {
            Insets insets = super.getInsets();
            if (imp.isComposite()) {
                CompositeImage ci = (CompositeImage) imp;
                if (ci.getMode() == CompositeImage.COMPOSITE) g.setColor(ci.getChannelColor());
            }
            g.drawString(createSubtitle(), insets.left + 5, insets.top + TEXT_GAP);
        }
    }
