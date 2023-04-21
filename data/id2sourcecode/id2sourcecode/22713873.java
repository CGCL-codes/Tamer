    public Attribute readAttribute() throws IOException {
        Attribute returnValue = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        while (ready()) {
            stream.write(read());
        }
        stream.close();
        String attributeData = stream.toString();
        try {
            returnValue = convertToAttribute(attributeData);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return returnValue;
    }
