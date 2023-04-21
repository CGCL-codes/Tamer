    @Override
    protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
        final StringBounder stringBounder = ug.getStringBounder();
        final double x1 = p1.getCenterX(stringBounder);
        final double x2 = p2.getCenterX(stringBounder);
        final double middle = (x1 + x2) / 2;
        final double textWidth = compText.getPreferredWidth(stringBounder);
        ug.translate(middle - textWidth / 2, getStartingY());
        final Dimension2D dim = new Dimension2DDouble(textWidth, compText.getPreferredHeight(stringBounder));
        compText.drawU(ug, new Area(dim), context);
    }
