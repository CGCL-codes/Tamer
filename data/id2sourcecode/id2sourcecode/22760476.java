    public byte[] toBuffer() throws InvalidPacketException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ZipOutputStream zo = new ZipOutputStream(bo);
        try {
            zo.putNextEntry(new ZipEntry("data"));
        } catch (IOException e1) {
            throw new InvalidPacketException("Compressing failed");
        }
        Source source = new DOMSource(this.packet);
        Result result = new StreamResult(zo);
        Transformer xformer;
        try {
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (Exception e) {
            throw new InvalidPacketException("Failed to serialize xml");
        }
        try {
            zo.close();
        } catch (IOException e) {
            throw new InvalidPacketException("Failed to compress xml");
        }
        return bo.toByteArray();
    }
