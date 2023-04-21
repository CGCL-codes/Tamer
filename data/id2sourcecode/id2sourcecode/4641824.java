    public void testMinimalModel() throws Exception {
        JModel writeModel = new JModel();
        LoadSystemTypes systemTypeAction = new LoadSystemTypes(writeModel);
        systemTypeAction.perform();
        LoadModulesFromClasspath typeAction = new LoadModulesFromClasspath(writeModel);
        typeAction.perform();
        LoadDetailsFromTypes detailAction = new LoadDetailsFromTypes(writeModel, true);
        detailAction.perform();
        ModelSerialization serialization = new ModelSerialization();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        serialization.writeModelToStream(writeModel, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        JModel readModel = (JModel) serialization.loadModelFromStream(bais);
        assertNotNull(readModel);
        Iterator writeTypes = writeModel.getTypes().iterator();
        while (writeTypes.hasNext()) {
            Type writeType = (Type) writeTypes.next();
            assertNotNull(readModel.lookupType(writeType.id()));
        }
    }
