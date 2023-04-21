    void gotoMarker(IMarker marker, EObject modelObject) {
        GraphicalViewer graphViewer = fDesignViewer.getGraphicalViewer();
        EObject refObj = null;
        EditPart editPart = null;
        if (modelObject instanceof Variable || modelObject instanceof PartnerLink || modelObject instanceof CorrelationSet || modelObject instanceof MessageExchange) {
            refObj = ModelHelper.getContainingScope(modelObject);
            editPart = (EditPart) graphViewer.getEditPartRegistry().get(refObj);
            if (editPart != null) {
                graphViewer.reveal(editPart);
            }
            fDesignViewer.selectModelObject(modelObject);
        } else if (modelObject instanceof Activity) {
            refObj = modelObject;
            editPart = (EditPart) graphViewer.getEditPartRegistry().get(refObj);
            if (editPart != null) {
                graphViewer.reveal(editPart);
            }
            fDesignViewer.selectModelObject(modelObject);
        } else {
            refObj = modelObject;
            while (refObj != null && !(refObj instanceof Activity)) {
                refObj = refObj.eContainer();
            }
            if (refObj == null) {
                refObj = ModelHelper.getProcess(modelObject);
            }
            modelObject = refObj;
            editPart = (EditPart) graphViewer.getEditPartRegistry().get(modelObject);
            if (editPart != null) {
                graphViewer.reveal(editPart);
            }
            fDesignViewer.selectModelObject(modelObject);
        }
        BPELTabbedPropertySheetPage propertySheetPage = currentPropertySheetPage;
        if (propertySheetPage == null) {
            return;
        }
        TabbedPropertyViewer viewer = propertySheetPage.getTabbedPropertyViewer();
        int j = 0;
        while (true) {
            TabDescriptor descriptor = null;
            try {
                descriptor = (TabDescriptor) viewer.getElementAt(j++);
            } catch (IndexOutOfBoundsException iobe) {
                break;
            }
            if (descriptor == null) {
                break;
            }
            Tab tab = descriptor.createTab();
            ISection[] sections = tab.getSections();
            for (int i = 0; i < sections.length; i++) {
                if (BPELPropertySection.class.isInstance(sections[i]) == false) {
                    continue;
                }
                BPELPropertySection section = (BPELPropertySection) sections[i];
                section.createControls(new Composite(getSite().getShell(), 0), propertySheetPage);
                section.setInput(this, new StructuredSelection(modelObject));
                if (section.isValidMarker(marker)) {
                    showPropertiesView();
                    viewer = currentPropertySheetPage.getTabbedPropertyViewer();
                    viewer.setSelection(new StructuredSelection(descriptor));
                    tab = currentPropertySheetPage.getCurrentTab();
                    section = (BPELPropertySection) tab.getSectionAtIndex(i);
                    section.gotoMarker(marker);
                    return;
                }
            }
        }
    }
