    private void addDndSupport(GraphicalViewer viewer, String type) {
        IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(UMLPlugin.PLUGIN_ID, "dnd");
        IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IExtension extension = extensions[i];
            IConfigurationElement[] elements = extension.getConfigurationElements();
            for (int j = 0; j < elements.length; j++) {
                IConfigurationElement element = elements[j];
                try {
                    Object object = element.createExecutableExtension("class");
                    if (object instanceof UMLDropTargetListenerFactory) {
                        UMLDropTargetListenerFactory factory = (UMLDropTargetListenerFactory) object;
                        if (factory.accept(type)) {
                            TransferDropTargetListener targetListener = factory.getDropTargetListener(viewer);
                            viewer.addDropTargetListener(targetListener);
                        }
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
    }
