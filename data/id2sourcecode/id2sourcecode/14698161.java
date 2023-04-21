    public static void storeEntity(TopEntity entity, SWSRepositoryStub _repository, Set<IRI> localCache) {
        if (localCache.contains(entity.getIdentifier()) && false == MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Entity Overwrite", "Entity '" + entity.getIdentifier().toString() + "' already exists in the repository." + "\n\nPlease confirm overwriting it?")) {
            return;
        }
        Serializer wsmlPrinter = WSMORuntime.getRuntime().getWsmlSerializer();
        StringBuffer wsmoStringContent = new StringBuffer();
        wsmlPrinter.serialize(new TopEntity[] { entity }, wsmoStringContent);
        WSMLDocument strWrapper = new SWSRepositoryStub.WSMLDocument();
        strWrapper.setDocumentContent(wsmoStringContent.toString());
        StoreEntity sendParam = new SWSRepositoryStub.StoreEntity();
        sendParam.setTheEntity(strWrapper);
        try {
            StoreEntityResponse response = _repository.storeEntity(sendParam);
            if (false == response.getOut()) {
                throw new Exception("Storage failure of " + entity.getIdentifier());
            }
            localCache.add((IRI) entity.getIdentifier());
        } catch (Exception ex) {
            LogManager.logError(ex);
            throw new SynchronisationException(ex.getMessage());
        }
    }
