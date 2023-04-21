    public byte[] encapsulate(DicomOutputStream dos, boolean writeFMI) throws IOException, ParserConfigurationException, SAXException, TransformerException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        DigestOutputStream dgstos = new DigestOutputStream(dos, md);
        dgstos.on(false);
        byte[] hash = null;
        try {
            int docLen = docWriter.size();
            if (writeFMI) dos.writeFileMetaInformation(attrs);
            dos.writeDataset(attrs.subSet(Tag.SpecificCharacterSet, Tag.EncapsulatedDocument), transferSyntax);
            log.info("Encapsulated Document length:" + docLen);
            dos.writeHeader(Tag.EncapsulatedDocument, VR.OB, (docLen + 1) & ~1);
            dgstos.on(true);
            docWriter.writeTo(dgstos);
            dgstos.on(false);
            if ((docLen & 1) != 0) {
                dos.write(0);
            }
            dgstos.on(false);
            dos.writeDataset(attrs.subSet(Tag.EncapsulatedDocument, -1), transferSyntax);
        } finally {
            dgstos.close();
            docWriter.close();
        }
        hash = md.digest();
        return hash;
    }
