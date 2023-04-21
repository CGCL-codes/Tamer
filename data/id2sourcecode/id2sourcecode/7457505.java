    public static CreateSignatureResult process(String filePath, List<HashFunction> hashFunctions, int length) {
        String sourcePath = filePath;
        String documentHash = MD5.calculateHash(filePath);
        String targetPath = documentHash;
        if (targetPath.equals("")) return new CreateSignatureResult(false, "MD5 invalid hash");
        try {
            File source = new File(sourcePath);
            File target = new File(DOCS_ROOT + "/" + targetPath.substring(0, 1) + "/" + targetPath.substring(1, 2), targetPath);
            filePath = target.getAbsolutePath();
            if (target.exists()) {
            }
            FileUtils.copyFile(source, target);
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateSignatureResult(false, e.getMessage());
        }
        StringBuffer title = new StringBuffer(sourcePath);
        NodeList bodyList = getBodyList(filePath, title);
        NodeList filteredNodes = DocumentManager.performNodeFiltering(bodyList);
        LinkedList<String> extractedText = DocumentManager.performTextExtraction(filteredNodes);
        LinkedList<String> filteredText = DocumentManager.performTextFiltering(extractedText);
        DocumentHashing documentHashing = new DocumentHashing();
        int documentId = documentHashing.addDocument(documentHash, title.toString());
        documentHashing.close();
        List<BitSet> wordSignatures = new ArrayList<BitSet>(filteredText.size());
        try {
            for (String word : filteredText) {
                BitSet wordSignature = new BitSet(length);
                for (HashFunction hashFunction : hashFunctions) {
                    Integer hashValue = hashFunction.hash(word).intValue();
                    wordSignature.set(hashValue, true);
                    wordSignatures.add(wordSignature);
                }
            }
            BitSet signature = new BitSet(length);
            for (BitSet wordSignature : wordSignatures) {
                signature.or(wordSignature);
            }
            Index documentIndex = new SignatureIndex(hashFunctions, length);
            documentIndex.open();
            documentIndex.add(signature, documentId);
            documentIndex.close();
            return new CreateSignatureResult(true, "success!!");
        } catch (HashFunctionException e) {
            e.printStackTrace();
            return new CreateSignatureResult(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateSignatureResult(false, e.getMessage());
        }
    }
