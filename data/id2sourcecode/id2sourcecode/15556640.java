    public KeyedDocumentDigest(KeyedDocument keyedDoc, String digestAlg) throws Exception {
        this.documentName = keyedDoc.getDocumentName();
        this.documentKey = keyedDoc.getDocumentKey();
        this.documentsDigestVector = new ASN1EncodableVector();
        MessageDigest md = MessageDigest.getInstance(digestAlg);
        AlgorithmIdentifier algId = LtansUtils.getAlgorithmIdentifier(digestAlg);
        for (int x = 0; x < keyedDoc.getDocumentCount(); x++) {
            md.reset();
            byte[] digestVal = md.digest(keyedDoc.getDocument(x).getOctets());
            documentsDigestVector.add(new DigestInfo(algId, digestVal));
        }
    }
