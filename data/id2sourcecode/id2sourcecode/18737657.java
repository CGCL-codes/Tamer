    private GraphicalViewer createRulerContainer(int orientation) {
        ScrollingGraphicalViewer viewer = new RulerViewer();
        final boolean isHorizontal = orientation == PositionConstants.NORTH || orientation == PositionConstants.SOUTH;
        viewer.setRootEditPart(new RulerRootEditPart(isHorizontal));
        viewer.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
        viewer.createControl(this);
        ((GraphicalEditPart) viewer.getRootEditPart()).getFigure().setBorder(new RulerBorder(isHorizontal));
        viewer.setProperty(GraphicalViewer.class.toString(), diagramViewer);
        FigureCanvas canvas = (FigureCanvas) viewer.getControl();
        canvas.setScrollBarVisibility(FigureCanvas.NEVER);
        if (font == null) {
            FontData[] data = canvas.getFont().getFontData();
            for (int i = 0; i < data.length; i++) {
                data[i].setHeight(data[i].getHeight() - 1);
            }
            font = new Font(Display.getCurrent(), data);
        }
        canvas.setFont(font);
        if (isHorizontal) {
            canvas.getViewport().setHorizontalRangeModel(editor.getViewport().getHorizontalRangeModel());
        } else {
            canvas.getViewport().setVerticalRangeModel(editor.getViewport().getVerticalRangeModel());
        }
        if (rulerEditDomain == null) {
            rulerEditDomain = new EditDomain();
            rulerEditDomain.setCommandStack(diagramViewer.getEditDomain().getCommandStack());
        }
        rulerEditDomain.addViewer(viewer);
        return viewer;
    }
