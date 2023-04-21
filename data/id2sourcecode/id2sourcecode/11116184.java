    public void getOWLFile(Set<String> modelsNames) throws IOException {
        ExtendedIterator modelNameIterator = connection.getAllModelNames();
        if (!modelNameIterator.hasNext()) {
            System.out.println("No models to dump");
            return;
        }
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
        zipOut.setLevel(9);
        String modelName = null;
        String fileName = null;
        OntModel ontModel = null;
        int index;
        RDFWriter writer = null;
        boolean extractSpecificModels = modelsNames.size() > 0;
        while (modelNameIterator.hasNext()) {
            modelName = (String) modelNameIterator.next();
            index = modelName.lastIndexOf("/");
            fileName = modelName;
            if (index != -1) {
                fileName = modelName.substring(index + 1, modelName.length());
            }
            if (extractSpecificModels && !modelsNames.contains(fileName)) {
                continue;
            }
            ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, modelMaker.openModel(modelName));
            System.out.println("Saving model \"" + modelName + "\"");
            try {
                writer = ontModel.getWriter("RDF/XML");
                ontModel.setNsPrefix("", modelName + "#");
                System.out.println("Base Namespace: " + modelName);
                writer.setProperty("xmlbase", modelName);
                writer.setProperty("showXmlDeclaration", "true");
                writer.setProperty("tab", "2");
                zipOut.putNextEntry(new ZipEntry(fileName));
                writer.write(ontModel, zipOut, null);
                ontModel.close();
                writer.setErrorHandler(this);
                zipOut.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("DONE");
        }
        zipOut.close();
    }
