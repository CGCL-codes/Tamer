    private void setRuler(RulerProvider provider, int orientation) {
        Object ruler = null;
        if (isRulerVisible && provider != null) ruler = provider.getRuler();
        if (ruler == null) {
            setRulerContainer(null, orientation);
            layout(true);
            return;
        }
        GraphicalViewer container = getRulerContainer(orientation);
        if (container == null) {
            container = createRulerContainer(orientation);
            setRulerContainer(container, orientation);
        }
        if (container.getContents() != ruler) {
            container.setContents(ruler);
            needToLayout = true;
            Display.getCurrent().asyncExec(runnable);
        }
    }
