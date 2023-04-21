    public ConversionGraphCreator() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extPoint = registry.getExtensionPoint(Activator.getId(), "encoder");
        IExtension[] extensions = extPoint.getExtensions();
        allExtensions = new ArrayList<ExtensionDescription>();
        for (int i = 0; i < extensions.length; i++) {
            List<Vertex> readableFormats = new ArrayList<Vertex>();
            List<Vertex> writeableFormats = new ArrayList<Vertex>();
            IConversionExtension conversionExtension = null;
            IExtension extension = extensions[i];
            IConfigurationElement[] configurations = extension.getConfigurationElements();
            for (int j = 0; j < configurations.length; j++) {
                IConfigurationElement element = configurations[j];
                String fileExtension = element.getAttribute("fileExtension");
                String isLossless = element.getAttribute("isLossless");
                if (isLossless == null) {
                    isLossless = Boolean.FALSE.toString();
                }
                if (fileExtension != null) {
                    Vertex fileFormat = new Vertex(fileExtension, isLossless.equals(Boolean.TRUE.toString()));
                    if (element.getName().equals("writeableFormat")) {
                        writeableFormats.add(fileFormat);
                    } else if (element.getName().equals("readableFormat")) {
                        readableFormats.add(fileFormat);
                    }
                }
                if (element.getName().equals("implementingClass")) {
                    try {
                        conversionExtension = (IConversionExtension) element.createExecutableExtension("className");
                    } catch (CoreException e) {
                        Logger.logError("Error instantiating extension " + extension.getUniqueIdentifier(), this.getClass());
                        break;
                    }
                }
            }
            allExtensions.add(new ExtensionDescription(conversionExtension, readableFormats, writeableFormats));
        }
        int pos = -1;
        for (ExtensionDescription extDesc : allExtensions) {
            for (Vertex sourceVertex : extDesc.getReadableFormats()) {
                pos = V.indexOf(sourceVertex);
                if (pos == -1) {
                    V.add(sourceVertex);
                    Logger.logInfo("adding source vertex: " + sourceVertex, this.getClass());
                } else {
                    sourceVertex = V.get(pos);
                }
                for (Vertex targetVertex : extDesc.getWriteableFormats()) {
                    pos = V.indexOf(targetVertex);
                    if (pos == -1) {
                        V.add(targetVertex);
                        Logger.logInfo("adding target vertex: " + targetVertex, this.getClass());
                    } else {
                        targetVertex = V.get(pos);
                    }
                    if (sourceVertex.equals(targetVertex)) {
                        continue;
                    }
                    Edge edge = new Edge(sourceVertex, targetVertex, extDesc.getConversionExtension());
                    if (!E.contains(edge)) {
                        E.add(edge);
                        Logger.logInfo("adding edge: " + edge, this.getClass());
                    }
                }
            }
        }
    }
