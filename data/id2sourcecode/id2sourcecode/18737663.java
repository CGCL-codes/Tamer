    public void setGraphicalViewer(ScrollingGraphicalViewer primaryViewer) {
        Assert.isNotNull(primaryViewer);
        Assert.isNotNull(primaryViewer.getControl());
        Assert.isTrue(diagramViewer == null);
        diagramViewer = primaryViewer;
        editor = (FigureCanvas) diagramViewer.getControl();
        layoutListener = new Listener() {

            public void handleEvent(Event event) {
                layout(true);
            }
        };
        addListener(SWT.Resize, layoutListener);
        editor.getHorizontalBar().addListener(SWT.Show, layoutListener);
        editor.getHorizontalBar().addListener(SWT.Hide, layoutListener);
        editor.getVerticalBar().addListener(SWT.Show, layoutListener);
        editor.getVerticalBar().addListener(SWT.Hide, layoutListener);
        propertyListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                String property = evt.getPropertyName();
                if (RulerProvider.PROPERTY_HORIZONTAL_RULER.equals(property)) {
                    setRuler((RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER), PositionConstants.NORTH);
                } else if (RulerProvider.PROPERTY_VERTICAL_RULER.equals(property)) {
                    setRuler((RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER), PositionConstants.WEST);
                } else if (RulerProvider.PROPERTY_RULER_VISIBILITY.equals(property)) setRulerVisibility(((Boolean) diagramViewer.getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY)).booleanValue());
            }
        };
        diagramViewer.addPropertyChangeListener(propertyListener);
        Boolean rulerVisibility = (Boolean) diagramViewer.getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
        if (rulerVisibility != null) setRulerVisibility(rulerVisibility.booleanValue());
        setRuler((RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER), PositionConstants.NORTH);
        setRuler((RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER), PositionConstants.WEST);
    }
