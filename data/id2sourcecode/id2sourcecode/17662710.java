    private void makeSelectionVisible() {
        float selectionCentre = (selectionEndPixel + selectionStartPixel) / 2;
        Rectangle r = new Rectangle((int) (selectionCentre - visibleRectangle.width / 2 + 0.5), 0, visibleRectangle.width, 0);
        scrollRectToVisible(r);
        repaint();
    }
