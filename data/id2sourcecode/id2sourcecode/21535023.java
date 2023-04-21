    protected Document encrypt(Document doc) throws GeneralSecurityException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(doc, writer);
        writer.close();
        byte[] crypt = dodes(os.toByteArray(), Cipher.ENCRYPT_MODE);
        Document secureDoc = new Document();
        Element root = new Element(PROTECTED_QBEAN);
        secureDoc.setRootElement(root);
        Element secureData = new Element("data");
        root.addContent(secureData);
        secureData.setText(ISOUtil.hexString(crypt));
        return secureDoc;
    }
