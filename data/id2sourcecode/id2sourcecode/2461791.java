    private void createRulers() {
        if (topRuler == null) {
            topRuler = new RepEditorRulerProvider(reportsDiagram, true);
        }
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, topRuler);
        if (leftRuler == null) {
            leftRuler = new RepEditorRulerProvider(reportsDiagram, false);
        }
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, leftRuler);
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, new Boolean(reportsDiagram.isRulerVisibility()));
    }
