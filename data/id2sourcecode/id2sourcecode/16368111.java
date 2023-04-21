    private void updateRulers() {
        DashboardRuler ruler = getDashboard().getRuler(PositionConstants.WEST);
        RulerProvider provider = null;
        if (ruler != null) {
            provider = new DashboardRulerProvider(ruler);
        }
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, provider);
        ruler = getDashboard().getRuler(PositionConstants.NORTH);
        provider = null;
        if (ruler != null) {
            provider = new DashboardRulerProvider(ruler);
        }
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, provider);
        getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.FALSE);
    }
