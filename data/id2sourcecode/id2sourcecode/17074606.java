    @Override
    protected void createRootEditPart(GraphicalViewer viewer) {
        viewer.setRootEditPart(new ScalableFreeformRootEditPart() {

            @SuppressWarnings("rawtypes")
            @Override
            public Object getAdapter(Class adapter) {
                if (adapter == AutoexposeHelper.class) {
                    return new ExtendedViewportAutoexposeHelper(this, new Insets(50), false);
                }
                return super.getAdapter(adapter);
            }
        });
    }
