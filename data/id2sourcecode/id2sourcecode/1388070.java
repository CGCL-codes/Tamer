    public boolean voegDocumentenToe(File locatie, boolean deep) throws IOException {
        if (!locatie.exists()) {
            return false;
        }
        File[] documentFiles;
        if (locatie.isDirectory()) {
            documentFiles = locatie.listFiles();
            System.out.println("DIRECTORY");
        } else {
            documentFiles = new File[1];
            documentFiles[0] = locatie;
            System.out.println("FILE");
        }
        if (documentFiles == null) return false;
        Document document = new Document();
        DTDocument dtdocument;
        File currentfile;
        Inlezer inlezer = new Inlezer();
        if (!deep) {
            analyzer = new StandardAnalyzer();
            indexModifier = new IndexModifier(Settings.getIndexdir(), analyzer, false);
        }
        for (int i = 0; i < documentFiles.length; i++) {
            currentfile = documentFiles[i];
            if (currentfile.isDirectory()) {
                System.out.println(documentFiles[i].getPath() + "  (DIRECTORY) ... verder uitdiepen=>");
                voegDocumentenToe(documentFiles[i], true);
            } else if (currentfile.isFile()) {
                String documentpath = currentfile.getPath();
                dtdocument = inlezer.leesBestandIn(documentpath);
                document = DTDocument.dtdocument2document(dtdocument);
                document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
                Term term = new Term("path", documentpath);
                indexModifier.deleteDocuments(term);
                indexModifier.addDocument(document);
                indexModifier.optimize();
            }
        }
        if (!deep) {
            indexModifier.optimize();
            indexModifier.close();
        }
        return true;
    }
