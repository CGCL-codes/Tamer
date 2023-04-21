    protected void loadOntInModel(Ont o) throws Exception {
        String uri = o.getURI();
        OntDocumentManager.getInstance().setProcessImports(false);
        if (ontWithThisUriIsAlreadyLoaded(uri)) return;
        String altUri = o.getURL();
        System.out.println("Loading ontology with uri: " + uri + "in OModel. Real URL is: " + altUri);
        String lang = "";
        if (altUri.contains(".nt")) lang = "N-TRIPLE";
        URL url;
        if (!uri.equals(altUri)) url = URLFactory.newURL(altUri); else url = URLFactory.newURL(uri);
        InputStream fis = url.openStream();
        getOntModel().read(fis, "", lang);
        fis.close();
        Set<String> toBeLoaded = new HashSet<String>();
        StmtIterator i = getOntModel().listStatements(null, (new OWLProfile()).IMPORTS(), (RDFNode) null);
        while (i.hasNext()) {
            Statement s = i.nextStatement();
            String urii = s.getObject().toString();
            if (!ontWithThisUriIsAlreadyLoaded(urii)) {
                loadedOnts.add(ontRepository.getOntFromLogicalUri(urii));
            }
        }
        i.close();
        for (String urii : toBeLoaded) addOnt(ontRepository.getOntFromLogicalUri(urii));
        loadedOnts.add(o);
        fireStateChanged();
    }
