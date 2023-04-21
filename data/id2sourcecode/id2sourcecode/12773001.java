    public static AddDocumentResult addDocument(String path) {
        String sourcePath = path;
        String documentHash = MD5.calculateHash(path);
        String targetPath = documentHash;
        if (targetPath.equals("")) return new AddDocumentResult(false, "MD5 invalid hash");
        try {
            File source = new File(sourcePath);
            File target = new File(DOCS_ROOT + "/" + targetPath.substring(0, 1) + "/" + targetPath.substring(1, 2), targetPath);
            path = target.getAbsolutePath();
            if (target.exists()) {
            }
            FileUtils.copyFile(source, target);
        } catch (Exception e) {
            e.printStackTrace();
            return new AddDocumentResult(false, e.getMessage());
        }
        String title = sourcePath;
        NodeList bodyList = new NodeList();
        try {
            Parser parser = new Parser();
            NodeList tags;
            parser.setResource(path);
            tags = parser.parse(null);
            NodeFilter filter;
            NodeList headList = new NodeList();
            filter = new TagNameFilter("HEAD");
            for (NodeIterator it = tags.elements(); it.hasMoreNodes(); ) it.nextNode().collectInto(headList, filter);
            NodeList titleList = new NodeList();
            filter = new TagNameFilter("TITLE");
            for (NodeIterator it = headList.elements(); it.hasMoreNodes(); ) it.nextNode().collectInto(titleList, filter);
            if (titleList.size() > 0) {
                try {
                    title = titleList.elementAt(0).getChildren().elementAt(0).getText();
                } catch (Exception e) {
                }
            }
            filter = new TagNameFilter("BODY");
            for (NodeIterator it = tags.elements(); it.hasMoreNodes(); ) it.nextNode().collectInto(bodyList, filter);
        } catch (ParserException e) {
            e.printStackTrace();
            return new AddDocumentResult(false, e.getMessage());
        }
        NodeList filteredNodes = performNodeFiltering(bodyList);
        LinkedList<String> extractedText = performTextExtraction(filteredNodes);
        LinkedList<String> filteredText = performTextFiltering(extractedText);
        DocumentHashing documentHashing = new DocumentHashing();
        int documentId = documentHashing.addDocument(documentHash, title);
        documentHashing.close();
        Index documentIndex = new DocumentIndex();
        documentIndex.open();
        for (String term : filteredText) {
            try {
                documentIndex.add(term, documentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        documentIndex.close();
        return new AddDocumentResult(true, "Success!!!");
    }
