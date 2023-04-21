    private String writeDocWithHash(BaseDocument doc, DataHandler dh) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        DigestOutputStream dos = new DigestOutputStream(doc.getOutputStream(), md);
        dh.writeTo(dos);
        dos.close();
        String hash = DocumentStore.toHexString(md.digest());
        assertTrue("Hash value was not set! " + doc.getDocumentUID(), doc.getStorage().setHash(doc, hash));
        return hash;
    }
