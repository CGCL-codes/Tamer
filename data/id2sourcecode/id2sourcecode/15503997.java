    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getDiagramGraphicalViewer().addDropTargetListener(new DiagramDropTargetListener(getDiagramGraphicalViewer(), SelectionTransfer.getInstance()) {

            @Override
            protected List getObjectsBeingDropped() {
                if (getCurrentEvent().data == null) {
                    return null;
                }
                List<Object> objectsBeingDropped = new ArrayList<Object>();
                TransferData[] data = getCurrentEvent().dataTypes;
                for (int i = 0; i < data.length; i++) {
                    if (SelectionTransfer.getInstance().isSupportedType(data[i])) {
                        IStructuredSelection selection = (IStructuredSelection) SelectionTransfer.getInstance().nativeToJava(data[i]);
                        for (Object o : selection.toList()) {
                            if (o instanceof TreeItem && ((TreeItem) o).getData() instanceof AbstractOwlEntityTreeElement) {
                                AbstractOwlEntityTreeElement treeElement = (AbstractOwlEntityTreeElement) ((TreeItem) o).getData();
                                OWL owl = (OWL) getDiagram().getElement();
                                OdmModelSearcher searcher = new OdmModelSearcher(owl);
                                objectsBeingDropped.addAll(searcher.getEObjectsAndTreeElement(treeElement));
                            }
                        }
                    }
                }
                return objectsBeingDropped;
            }

            protected void handleDragOver() {
                getCurrentEvent().detail = DND.DROP_COPY;
                super.handleDragOver();
            }

            @Override
            public boolean isEnabled(DropTargetEvent event) {
                super.isEnabled(event);
                ISelection selection = SelectionTransfer.getInstance().getSelection();
                if (selection instanceof IStructuredSelection) {
                    List selectedElements = ((IStructuredSelection) selection).toList();
                    if (selectedElements == null) return false;
                    for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                        Object o = i.next();
                        if (!(o instanceof TreeItem)) return false;
                    }
                    return true;
                }
                return false;
            }

            @Override
            protected void updateTargetRequest() {
                ((DropObjectsRequest) getTargetRequest()).setLocation(getDropLocation());
            }
        });
        DiagramRootEditPart root = (DiagramRootEditPart) getDiagramGraphicalViewer().getRootEditPart();
        LayeredPane printableLayers = (LayeredPane) root.getLayer(LayerConstants.PRINTABLE_LAYERS);
        FreeformLayer extLabelsLayer = new FreeformLayer();
        extLabelsLayer.setLayoutManager(new DelegatingLayout());
        printableLayers.addLayerAfter(extLabelsLayer, OdmEditPartFactory.EXTERNAL_NODE_LABELS_LAYER, LayerConstants.PRIMARY_LAYER);
        LayeredPane scalableLayers = (LayeredPane) root.getLayer(LayerConstants.SCALABLE_LAYERS);
        FreeformLayer scaledFeedbackLayer = new FreeformLayer();
        scaledFeedbackLayer.setEnabled(false);
        scalableLayers.addLayerAfter(scaledFeedbackLayer, LayerConstants.SCALED_FEEDBACK_LAYER, DiagramRootEditPart.DECORATION_UNPRINTABLE_LAYER);
    }
