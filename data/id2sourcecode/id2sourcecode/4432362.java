    protected void importModelFromResource(Resource resource, ImportStatistics stats, Event event) throws IOException, ModelImportException {
        stats.beginImporting();
        stats.beginLoadingResource(resource);
        SpecFile specFile = new SpecFile();
        specFile.loadResource(resource);
        stats.finishLoadingResource(resource);
        if (event != null && getEventForeignSource(event) != null) {
            specFile.setForeignSource(getEventForeignSource(event));
        }
        stats.beginAuditNodes();
        createDistPollerIfNecessary();
        Map<String, Integer> foreignIdsToNodes = getForeignIdToNodeMap(specFile.getForeignSource());
        ImportOperationsManager opsMgr = createImportOperationsManager(foreignIdsToNodes, stats);
        opsMgr.setForeignSource(specFile.getForeignSource());
        opsMgr.setScanThreads(m_scanThreads);
        opsMgr.setWriteThreads(m_writeThreads);
        auditNodes(opsMgr, specFile);
        stats.finishAuditNodes();
        opsMgr.persistOperations(m_transTemplate, getNodeDao());
        stats.beginRelateNodes();
        relateNodes(specFile);
        stats.finishRelateNodes();
        stats.finishImporting();
    }
