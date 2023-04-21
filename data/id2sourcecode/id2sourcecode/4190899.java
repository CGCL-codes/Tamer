    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setRootEditPart(new ScalableFreeformRootEditPart());
        IAction zoomInAction = new ZoomInAction(getZoomManager());
        IAction zoomOutAction = new ZoomOutAction(getZoomManager());
        getActionRegistry().registerAction(zoomInAction);
        getActionRegistry().registerAction(zoomOutAction);
        IHandlerService handlerService = (IHandlerService) getSite().getWorkbenchWindow().getService(IHandlerService.class);
        handlerService.activateHandler(zoomInAction.getActionDefinitionId(), new ActionHandler(zoomInAction));
        handlerService.activateHandler(zoomOutAction.getActionDefinitionId(), new ActionHandler(zoomOutAction));
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
    }
