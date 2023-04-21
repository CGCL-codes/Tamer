    private void run() {
        final boolean debug = false;
        System.out.println("============================== Sport Ontology ==============================");
        newline();
        String path = debug ? Constants.articlePath : (readLine("- Enter path to articles (default: " + Constants.articlePath + "):", Constants.articlePath));
        boolean useCache = debug ? true : readBoolean("- Try to use cache?", true);
        String cacheName = debug ? Constants.cacheName : (useCache ? readLine("     Cache name (default: " + Constants.cacheName + "):", Constants.cacheName) : Constants.cacheName);
        if (useCache) {
            cache = new DataCache("cache" + File.separator, cacheName);
        }
        boolean displayOntology = debug ? true : readBoolean("- Display created ontology graphically?", true);
        boolean displayRelationInstances = debug ? false : (displayOntology ? readBoolean("     Display relation instances?", false) : false);
        boolean writeXMLOntology = debug ? true : readBoolean("- Write created ontology into XML file?", true);
        String xmlOutputPath = debug ? Constants.outputPath : (writeXMLOntology ? readLine("     Enter output path (default: " + Constants.outputPath + "):", Constants.outputPath) : "");
        boolean printResults = debug ? true : readBoolean("- Print results to console?", false);
        newline();
        DocumentCollection docColl = loadDocumentCollection(path);
        extraction.Stopwords sw = stopwordsDetection(docColl);
        if (printResults) {
            output("Stopwords:", true);
            output(util.CollectionUtil.collectionToString(sw.getStopwords()));
            newline();
        }
        List<Concept> concepts = conceptDetection(docColl, sw);
        List<String> conceptStringList = new ArrayList<String>();
        List<String> conceptinstanceStringList = new ArrayList<String>();
        List<ConceptInstance> conceptinstanceList = new ArrayList<ConceptInstance>();
        if (printResults) {
            output("Found the following concepts:", true);
            int index = 1;
            for (Concept c : concepts) {
                output("Concept #" + (index++) + ": \"" + c.getName() + "\" with instances:");
                output(util.CollectionUtil.collectionToString(c.getInstances()));
                newline();
                conceptStringList.add(c.getName());
                for (ConceptInstance ci : c.getInstances()) {
                    conceptinstanceList.add(ci);
                    conceptinstanceStringList.add(ci.getName());
                }
            }
            newline();
        }
        List<RelationInstance> relationInstances = relationInstanceDetection(docColl, concepts);
        if (printResults) {
            output("Found the following relation instances:", true);
            for (RelationInstance relInst : relationInstances) {
                output(relInst.toString());
                newline();
            }
            newline();
        }
        List<Relation> relations = relationDetection(docColl, concepts, relationInstances);
        if (printResults) {
            output("Found the following relations:", true);
            for (Relation rel : relations) {
                output(rel.toString());
                newline();
            }
            newline();
        }
        if (displayOntology) {
            new Visualization(concepts, relations, relationInstances, displayRelationInstances);
        }
        if (writeXMLOntology) {
            Ontology ontology = new Ontology(concepts, relations);
            try {
                IOUtil.writeOntology(ontology, xmlOutputPath, IOUtil.FileType.UTF8);
            } catch (FileNotFoundException e) {
                System.err.println("Could not write the ontology to the XML file, because the file was not found. " + e.getMessage());
            }
        }
        System.out.println("---------- finished ----------");
        System.out.println("(depending on your choices, an output window and/or an xml file shall be generated)");
    }
