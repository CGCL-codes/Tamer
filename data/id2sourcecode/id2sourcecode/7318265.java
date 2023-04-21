    public static Map<TableView, Location> getTableLocationMap(GraphicalViewer viewer, ERDiagram diagram) {
        Map<TableView, Location> tableLocationMap = new HashMap<TableView, Location>();
        ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = ((LayerManager) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);
        int translateX = ExportToImageAction.translateX(rootFigure.getBounds().x);
        int translateY = ExportToImageAction.translateY(rootFigure.getBounds().y);
        Category category = diagram.getCurrentCategory();
        for (Object child : rootEditPart.getContents().getChildren()) {
            NodeElementEditPart editPart = (NodeElementEditPart) child;
            NodeElement nodeElement = (NodeElement) editPart.getModel();
            if (!(nodeElement instanceof TableView)) {
                continue;
            }
            if (category == null || category.isVisible(nodeElement, diagram)) {
                IFigure figure = editPart.getFigure();
                Rectangle figureRectangle = figure.getBounds();
                Location location = new Location(figureRectangle.x + translateX, figureRectangle.y + translateY, figureRectangle.width, figureRectangle.height);
                tableLocationMap.put((TableView) nodeElement, location);
            }
        }
        return tableLocationMap;
    }
