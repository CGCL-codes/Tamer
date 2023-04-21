    public void convert() throws ConverterException, IOException, TabConversionException {
        if (xmlFilesToConvert == null) {
            throw new IllegalArgumentException("You must give a non null Collection<File> to convert.");
        }
        if (xmlFilesToConvert.isEmpty()) {
            throw new IllegalArgumentException("You must give a non empty Collection<File> to convert.");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("You must give a non null output file.");
        }
        if (outputFile.exists() && !overwriteOutputFile) {
            throw new IllegalArgumentException(outputFile.getName() + " already exits, overwrite is set to false. abort.");
        }
        if (outputFile.exists() && !outputFile.canWrite()) {
            throw new IllegalArgumentException(outputFile.getName() + " is not writable. abort.");
        }
        OntologyNameFinder finder = new OntologyNameFinder(ontologyIndexSearcher);
        for (String name : ontologyNameToAutocomplete) {
            finder.addOntologyName(name);
        }
        Xml2Tab x2t = new IntactXml2Tab(finder);
        x2t.addOverrideSourceDatabase(new CrossReferenceImpl("psi-mi", "MI:0469", "intact"));
        x2t.setExpansionStrategy(expansionStragegy);
        if (interactorPairClustering) {
            x2t.setPostProcessor(new IntactClusterInteractorPairProcessor());
        } else {
            x2t.setPostProcessor(null);
        }
        Collection<BinaryInteraction> interactions = x2t.convert(xmlFilesToConvert);
        if (interactions.isEmpty()) {
            if (logWriter != null) {
                logWriter.write("The following file(s) didn't yield any binary interactions:" + NEW_LINE);
                for (File file : xmlFilesToConvert) {
                    logWriter.write("  - " + file.getAbsolutePath() + NEW_LINE);
                }
                logWriter.write(outputFile.getName() + " was not generated." + NEW_LINE);
                logWriter.flush();
            } else {
                log.warn("The MITAB file " + outputFile.getName() + " didn't contain any data");
            }
        } else {
            PsimiTabWriter writer = new IntactPsimiTabWriter();
            writer.write(interactions, outputFile);
        }
    }
