    private void testImportFromSpecFile(Resource specFileResource, int writeThreads, int scanThreads) throws IOException, ModelImportException {
        expectServiceTypeCreate("HTTP");
        final SpecFile specFile = new SpecFile();
        specFile.loadResource(specFileResource);
        Map<String, Integer> assetNumbers = getAssetNumberMapInTransaction(specFile);
        final ImportOperationsManager opsMgr = new ImportOperationsManager(assetNumbers, getModelImporter());
        opsMgr.setWriteThreads(writeThreads);
        opsMgr.setScanThreads(scanThreads);
        opsMgr.setForeignSource(specFile.getForeignSource());
        m_transTemplate.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                AbstractImportVisitor accountant = new ImportAccountant(opsMgr);
                specFile.visitImport(accountant);
                return null;
            }
        });
        opsMgr.persistOperations(m_transTemplate, getNodeDao());
    }
