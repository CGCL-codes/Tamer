    public void setParent(EditPart parent) {
        super.setParent(parent);
        if (getParent() != null && diagramViewer == null) {
            diagramViewer = (GraphicalViewer) getViewer().getProperty(GraphicalViewer.class.toString());
            RulerProvider hProvider = (RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER);
            if (hProvider != null && hProvider.getRuler() == getModel()) {
                rulerProvider = hProvider;
                horizontal = true;
            } else {
                rulerProvider = (RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER);
            }
        }
    }
