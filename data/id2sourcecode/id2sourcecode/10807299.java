    @Override
    public void layoutContainer(Container parent) {
        if (!(parent instanceof BarRenderer) || !(parent instanceof BeatBasedBarRenderer)) {
            throw new IllegalArgumentException("parent=" + parent);
        }
        BarRenderer br = (BarRenderer) parent;
        int barWidth = br.getDrawingArea().width;
        int barHeight = br.getDrawingArea().height;
        int barLeft = br.getDrawingArea().x;
        int barTop = br.getDrawingArea().y;
        TimeSignature ts = ((BeatBasedBarRenderer) parent).getTimeSignature();
        for (ItemRenderer ir : br.getItemRenderers()) {
            Position pos = ir.getModel().getPosition();
            int eventWidth = ir.getWidth();
            int eventHeight = ir.getHeight();
            int x = getBeatXPosition(pos.getBeat(), barWidth, ts);
            x += (barLeft - (eventWidth / 2));
            int y = barTop + (barHeight - eventHeight) / 2;
            ir.setLocation(x, y);
        }
    }
