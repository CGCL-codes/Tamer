    private void getOrCreateConfiguration(HttpServletRequest request, HttpServletResponse response) throws Exception, OperatorException, ServletException {
        System.out.println("Starting load file");
        String filePath = getParameter(request, CONFIG_FILE_PATH);
        String id = getParameter(request, CONFIG_IDENTIFIER_KEY);
        System.out.println("file path = " + filePath + ", id=" + id);
        if (isEmpty(id)) {
            throw new IllegalStateException("Error: no internal identifier found. Aborting");
        }
        File file = new File(filePath);
        PortalConfigurationDepot pcd = null;
        if (file.exists()) {
            pcd = new PortalConfigurationDepot(filePath);
        } else {
            if (!file.getParentFile().canWrite() || !file.getParentFile().canWrite()) {
                throw new IllegalStateException("Error: The file you specified  does not have a readable or writeable directory \"" + file.getParentFile() + "\"");
            }
            pcd = new PortalConfigurationDepot();
        }
        String rootName = getParameter(request, Rdfs.LABEL);
        PortalRoot portalRoot = null;
        if (isEmpty(rootName)) {
            try {
                portalRoot = (PortalRoot) pcd.findRoot();
                if (portalRoot == null) {
                    pcd.createRoot("default configuration");
                }
            } catch (IllegalStateException x) {
                throw x;
            }
        } else {
            List<PortalRoot> roots = pcd.listPortalRoots();
            if (roots.isEmpty()) {
                portalRoot = (PortalRoot) pcd.createRoot(rootName);
                pcd.setRoot(portalRoot);
            } else {
                for (PortalRoot root : roots) {
                    if (root.getLabel().equals(rootName)) {
                        portalRoot = root;
                        pcd.setRoot(root);
                        break;
                    }
                }
            }
        }
        ConfigRecord configRecord = new ConfigRecord();
        configRecord.fileName = file.getCanonicalPath();
        configRecord.id = id;
        configRecord.pcd = pcd;
        getConfigs().put(id, configRecord);
        PortalParametersModel ppm = null;
        if (!portalRoot.hasA(PortalVocabulary.HAS_PORTAL_PARAMETERS)) {
            ppm = new PortalParametersModel(portalRoot.getMyThingSession(), UriRefFactory.uriRef());
            portalRoot.setPortalParametersModel(ppm);
        } else {
            ppm = portalRoot.getPortalParametersModel();
        }
        setAtt(request, "callback", portalRoot.getPortalParametersModel().getCallback(), PortalVocabulary.PORTAL_CALLBACK_URI);
        setAtt(request, "success", portalRoot.getPortalParametersModel().getSuccess(), PortalVocabulary.PORTAL_SUCCESS_URI);
        setAtt(request, "failure", portalRoot.getPortalParametersModel().getFailure(), PortalVocabulary.PORTAL_FAILURE_URI);
        setAtt(request, "portalName", portalRoot.getPortalParametersModel().getName(), PortalVocabulary.PORTAL_NAME);
        setAtt(request, "portalSkin", portalRoot.getPortalParametersModel().getSkin(), PortalVocabulary.PORTAL_SKIN);
        request.setAttribute("storeType", ConfigConstants.STORE_TYPE);
        if (portalRoot.hasA(SQLVocabulary.HAS_STORE)) {
            StoreModel storeModel = portalRoot.getStore();
            if (storeModel.isA(SQLVocabulary.MEMORY_STORE_TYPE)) {
                request.setAttribute("memoryStoreChecked", "checked");
            } else if (storeModel.isA(SQLVocabulary.FILE_STORE_TYPE)) {
                request.setAttribute("fileStoreChecked", "checked");
            } else if (storeModel.isA(SQLVocabulary.POSTGRES_STORE_TYPE)) {
                request.setAttribute("postgresStoreChecked", "checked");
            }
        }
        request.setAttribute("memoryStore", SQLVocabulary.MEMORY_STORE_TYPE);
        request.setAttribute("fileStore", SQLVocabulary.FILE_STORE_TYPE);
        request.setAttribute("postgresStore", SQLVocabulary.POSTGRES_STORE_TYPE);
        addState(request, id, CONFIG_STATUS_BASIC);
        saveConfig(id);
        fwd(request, response, "setup-basic.jsp");
    }
