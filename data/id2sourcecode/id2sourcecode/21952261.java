    private void saveModel(File targetFile) {
        BpmoModel model = (BpmoModel) getGraphicalViewer().getContents().getModel();
        if (model.getNSHolder().getDefaultNamespace() != null) {
            Map<String, Object> newConfig = new HashMap<String, Object>();
            newConfig.put(BpmoSerializer.RESULT_NAMESPACE, model.getNSHolder().getDefaultNamespace());
            Activator.getBpmoFactory().configure(newConfig);
        }
        Map<WorkflowEntityNode, String> errors = new HashMap<WorkflowEntityNode, String>();
        UIModelValidator.validateModelConsistency(model, errors);
        if (errors.size() > 0) {
            WorkflowEntityNode errorNode = errors.keySet().iterator().next();
            Utils.expandPathFromRoot(errorNode);
            EditPart errorEditpart = (EditPart) getEditPartViewer().getEditPartRegistry().get(errorNode);
            if (errorEditpart != null) {
                getEditPartViewer().select(errorEditpart);
            }
            if (false == MessageDialog.openConfirm(getSite().getShell(), "Inconsistent Diagram", "The current diagram might not be represented correctly in BPMO:\n\n " + errors.get(errorNode) + ((errors.size() > 1) ? ("\n\n  (+ " + (errors.size() - 1) + " more error(s))") : "") + "\n\nDo you want to proceed (at your risk) with saving anyway?")) {
                return;
            }
        }
        if (targetFile == null) {
            doSaveAs();
            return;
        }
        BPMOExporter uiExporter = new BPMOExporter();
        Identifiable[] bpmoModel = null;
        try {
            bpmoModel = uiExporter.convertVisualModel(model);
        } catch (Exception ex) {
            MessageDialog.openError(getSite().getShell(), "BPMO Export", "Error in converting diagram to BPMO model:\n" + ex.getMessage());
            LogManager.logError(ex);
            return;
        }
        Map<String, Object> props = new HashMap<String, Object>();
        if (model.getNSHolder().getDefaultNamespace() != null) {
            props.put(BpmoSerializer.RESULT_NAMESPACE, model.getNSHolder().getDefaultNamespace());
        }
        props.put(BpmoSerializer.RESULT_ONTOLOGY_ID, model.getNSHolder().getIdentifier());
        BpmoSerializer bpmoSerializer = Factory.createSerializer(props);
        Ontology instOntology = bpmoSerializer.serialize(bpmoModel);
        Utils.recoverNonBPMOData(instOntology, this.nonBpmoWsmlData, this.nonBpmoAttributes);
        org.wsmostudio.runtime.io.Utils.updateStudioNFP(instOntology);
        model.updateNamespacesTo(instOntology);
        Serializer wsmlSerializar = WSMORuntime.getRuntime().getWsmlSerializer();
        StringBuffer str = new StringBuffer();
        wsmlSerializar.serialize(new TopEntity[] { instOntology }, str);
        try {
            WSMORuntime.getIOManager().saveContent(instOntology, new Path(targetFile.getAbsolutePath()));
            this.getEditDomain().getCommandStack().markSaveLocation();
            this.dirty = false;
        } catch (Exception e) {
            LogManager.logError(e);
        }
        File layoutFile = new File(targetFile.getAbsolutePath() + ".layout");
        try {
            uiExporter.exportLayout(layoutFile, instOntology.getIdentifier());
            IFile iResource = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(layoutFile.getAbsolutePath()));
            if (iResource == null) {
                return;
            }
            iResource.refreshLocal(IResource.DEPTH_ONE, null);
        } catch (Exception ex) {
            MessageDialog.openWarning(getSite().getShell(), "Saving Layout", "Error while saving diagram layout:\n" + ex.getMessage());
            LogManager.logError(ex);
        }
        firePropertyChange(PROP_DIRTY);
    }
