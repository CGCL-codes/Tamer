    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if (isDispatching) {
            return;
        }
        fLastEvent = event;
        if (!Preferences.doLinkView() || !fDoSync) {
            return;
        }
        isDispatching = true;
        ISelection selection = event.getSelection();
        Object source = event.getSource();
        if (source instanceof GraphicalViewer && fTreeView != null) {
            List<Object> list = new ArrayList<Object>();
            for (Object o : ((IStructuredSelection) selection).toArray()) {
                if (o instanceof EditPart) {
                    Object model = ((EditPart) o).getModel();
                    if (model instanceof IDiagramModelArchimateObject) {
                        model = ((IDiagramModelArchimateObject) model).getArchimateElement();
                        list.add(model);
                    } else if (model instanceof IDiagramModelArchimateConnection) {
                        model = ((IDiagramModelArchimateConnection) model).getRelationship();
                        list.add(model);
                    } else if (model instanceof IDiagramModel) {
                        list.add(model);
                    }
                }
            }
            fTreeView.getViewer().setSelection(new StructuredSelection(list), true);
        } else if (source instanceof TreeViewer) {
            List<IArchimateElement> list = new ArrayList<IArchimateElement>();
            for (Object o : ((IStructuredSelection) selection).toArray()) {
                if (o instanceof IArchimateElement) {
                    list.add((IArchimateElement) o);
                }
            }
            for (IDiagramModelEditor diagramEditor : fDiagramEditors) {
                if (diagramEditor instanceof IArchimateDiagramEditor) {
                    ((IArchimateDiagramEditor) diagramEditor).selectElements(list.toArray(new IArchimateElement[list.size()]));
                }
            }
        }
        isDispatching = false;
    }
