    private JIOPiConfig.Controlpanel getAssemblingConfig(String registerClassName, String configurationID) {
        if ("null".equals(configurationID)) throw new RuntimeException("test");
        JIOPiConfig.Controlpanel cpConfigs = null;
        LinkedHashSet<URL> assemblingConfigFiles = new LinkedHashSet<URL>();
        URL contextAssemblingConfigFile = originContextClassLoader.get().getResource(JiopiConfigConstants.ASSEMBLING_FILE);
        if (contextAssemblingConfigFile != null) assemblingConfigFiles.add(contextAssemblingConfigFile);
        LinkedHashSet<IBeanClassLoader> callerModules = originIBeanClassLoaderStack.get();
        for (IBeanClassLoader icl : callerModules) {
            URL assemblingConfigFile = icl.getLocalResource(JiopiConfigConstants.ASSEMBLING_FILE);
            addAssemblingConfigFile(assemblingConfigFiles, assemblingConfigFile);
        }
        Stack<IBeanClassLoader> assemblingStack = getAssemblingStack();
        for (IBeanClassLoader icl : assemblingStack) {
            URL assemblingConfigFile = icl.getLocalResource(JiopiConfigConstants.ASSEMBLING_FILE);
            addAssemblingConfigFile(assemblingConfigFiles, assemblingConfigFile);
        }
        Document assemblingConfigDocument = null;
        if (assemblingConfigFiles.size() > 0) {
            URL[] assemblingConfigURls = assemblingConfigFiles.toArray(new URL[assemblingConfigFiles.size()]);
            for (int i = assemblingConfigURls.length - 1; i > -1; i--) {
                URL url = assemblingConfigURls[i];
                try {
                    InputStream is = url.openStream();
                    Document configDocument = XMLMerger.readDocumentAndCloseStream(is);
                    if (assemblingConfigDocument == null) {
                        assemblingConfigDocument = configDocument;
                    } else {
                        assemblingConfigDocument = XMLMerger.mergeXML(assemblingConfigDocument, configDocument);
                    }
                } catch (Exception e) {
                    logger.warn("load assemblingConfigDocument error " + url, e);
                }
            }
        }
        if (assemblingConfigDocument != null) {
            JIOPiConfig assemblingConfig = new JIOPiConfig(assemblingConfigDocument);
            JIOPiConfig.Module findModuleConfig = assemblingConfig.getModuleConfig(moduleVersion);
            if (findModuleConfig != null) {
                cpConfigs = findModuleConfig.getMatchedControlpanel(moduleClassLoader, registerClassName, configurationID);
            }
        }
        return cpConfigs;
    }
