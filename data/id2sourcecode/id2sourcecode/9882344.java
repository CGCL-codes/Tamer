    public byte[] generateIdentifier(Document document, String location, int size, Calendar time) {
        this.messageDigest.reset();
        final DocumentInformation documentInformation = document.getDocumentInformation();
        if (documentInformation != null) {
            this.updateMessageDigest(documentInformation.getTitle());
            this.updateMessageDigest(documentInformation.getAuthor());
            this.updateMessageDigest(documentInformation.getSubject());
            this.updateMessageDigest(documentInformation.getKeywords());
            this.updateMessageDigest(documentInformation.getCreator());
            this.updateMessageDigest(documentInformation.getProducer());
            this.updateMessageDigest(documentInformation.getCreationDate());
            this.updateMessageDigest(documentInformation.getModificationDate());
        }
        this.updateMessageDigest(location);
        this.updateMessageDigest(size);
        this.updateMessageDigest(time);
        return this.messageDigest.digest();
    }
