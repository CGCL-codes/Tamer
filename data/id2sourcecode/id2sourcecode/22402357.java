    public String calculateHash() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(document);
        Result outputTarget = new StreamResult(outputStream);
        try {
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        } catch (TransformerException e1) {
            e1.printStackTrace();
        } catch (TransformerFactoryConfigurationError e1) {
            e1.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        MessageDigest md = null;
        DigestInputStream digestStream;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digestStream = new DigestInputStream(is, md);
        try {
            while (digestStream.read() != -1) ;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            digestStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] calculatedHash = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < calculatedHash.length; i++) {
            hexString.append(Integer.toHexString(0xFF & calculatedHash[i]));
        }
        String newHash = hexString.toString();
        Element hashCode = document.createElement("property");
        hashCode.setAttribute("name", "hash");
        hashCode.setAttribute("value", newHash);
        NodeList nodes = executeXPath("//jasperReport");
        Node jasperReports = nodes.item(0);
        jasperReports.insertBefore(hashCode, jasperReports.getFirstChild());
        return newHash;
    }
