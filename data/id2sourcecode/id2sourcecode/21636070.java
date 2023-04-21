    public void getOWLFile(Set<String> modelsNames, boolean simpleDump) throws IOException {
        Dataset dataset = SDBFactory.connectDataset(store);
        Iterator<?> modelNameIterator = dataset.listNames();
        if (!modelNameIterator.hasNext()) {
            logger.info("No models to dump");
            return;
        }
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
        zipOut.setLevel(9);
        String modelName = null;
        String fileName = null;
        OntModel ontModel = null;
        int index;
        RDFWriter writer = null;
        boolean entryAdded;
        boolean extractSpecificModels = modelsNames.size() > 0;
        while (modelNameIterator.hasNext()) {
            modelName = (String) modelNameIterator.next();
            if (modelName.equals(MAIN_INFERRED_MODEL)) {
                continue;
            }
            index = modelName.lastIndexOf("/");
            fileName = modelName;
            if (index != -1) {
                fileName = modelName.substring(index + 1, modelName.length());
            }
            if (extractSpecificModels && !modelsNames.contains(fileName)) {
                continue;
            }
            ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, SDBFactory.connectNamedModel(store, modelName));
            logger.info("Saving model \"" + modelName + "\"");
            entryAdded = false;
            try {
                writer = ontModel.getWriter(simpleDump ? "RDF/XML" : "RDF/XML-ABBREV");
                ontModel.setNsPrefix("", modelName + "#");
                logger.info("Base Namespace: " + modelName);
                writer.setProperty("xmlbase", modelName);
                writer.setProperty("showXmlDeclaration", "true");
                writer.setProperty("tab", "2");
                zipOut.putNextEntry(new ZipEntry(fileName));
                entryAdded = true;
                writer.write(ontModel, zipOut, null);
                ontModel.close();
                writer.setErrorHandler(this);
                zipOut.closeEntry();
                entryAdded = false;
            } catch (IOException e) {
                if (entryAdded) {
                    zipOut.close();
                }
                e.printStackTrace();
            }
            logger.info("DONE");
        }
        zipOut.close();
        shutdown();
    }
