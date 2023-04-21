    public void setInput(IEditorInput input) {
        superSetInput(input);
        try {
            ModelEventDispatcher.setEventsEnabled(false);
            Object obj = readModelInstance(input);
            ModelEventDispatcher.setEventsEnabled(true);
            Root c = null;
            if (obj instanceof String) {
            }
            if (obj instanceof Container && !(obj instanceof Root)) {
                obj = convertToRoot((Container) obj);
            }
            if (obj instanceof Container) {
                c = (Root) obj;
                c.setModelInstanceID(logicDiagram.getModelInstanceID());
            } else if (obj instanceof ModelInstance) {
                ModelInstance inst = (ModelInstance) obj;
                Container croot = null;
                if (inst.getRoot() != null) croot = inst.getRoot(); else if (inst.getRootElements().length > 0) croot = (Container) inst.getRootElements()[0];
                if (!(croot instanceof Root)) croot = convertToRoot(croot);
                c = (Root) croot;
                InstanceRepository repo = ModelRepository.getInstance().getInstanceRepository();
                repo.registerInstance(c.getModelInstanceID(), inst);
            }
            if (c.getModelID() == null) c.setModelID(getModelID());
            ModelInstance instance = ModelRepository.getInstance().getInstanceRepository().getInstance(c.getModelInstanceID());
            IFile file = ((FileEditorInput) getEditorInput()).getFile();
            ModelRepository.getInstance().getInstanceRepository().setFile(c.getModelInstanceID(), file);
            ModelPreprocessor[] pre = PluginUtilities.getPreprocessors(c.getModelID());
            for (ModelPreprocessor p : pre) {
                p.process(instance);
            }
            configureEventListeners(instance);
            configurePaletteRoot(instance);
            setLogicDiagram(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!editorSaving) {
            if (getGraphicalViewer() != null) {
                getGraphicalViewer().setContents(getLogicDiagram());
                loadProperties();
            }
            if (outlinePage != null) {
                outlinePage.setContents(getLogicDiagram());
            }
        }
        ModelInstance inst = ModelRepository.getInstance().getInstanceRepository().getInstance(logicDiagram.getModelInstanceID());
        ModelRepository.getInstance().getInstanceRepository().setEditor(inst, this);
        ModelAccessListener[] list = PluginUtilities.getModelAccessListeners(logicDiagram.getModelID());
        if (list != null) {
            for (ModelAccessListener l : list) l.modelOpened(this, inst);
        }
    }
