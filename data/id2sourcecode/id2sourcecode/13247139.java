    public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
        final UEllipse circle = new UEllipse(SIZE, SIZE);
        if (getSkinParam().shadowing()) {
            circle.setDeltaShadow(4);
        }
        ug.getParam().setStroke(new UStroke(1.5));
        ug.getParam().setColor(getColor(ColorParam.stateBorder, getStereo()));
        ug.getParam().setBackcolor(getColor(ColorParam.stateBackground, getStereo()));
        ug.draw(xTheoricalPosition, yTheoricalPosition, circle);
        ug.getParam().setStroke(new UStroke());
        final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
        final double widthDesc = dimDesc.getWidth();
        final double heightDesc = dimDesc.getHeight();
        final double x = xTheoricalPosition + (SIZE - widthDesc) / 2;
        final double y = yTheoricalPosition + (SIZE - heightDesc) / 2;
        desc.drawU(ug, x, y);
    }
