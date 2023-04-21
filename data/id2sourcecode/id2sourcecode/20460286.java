    public void startElement(String namespace, String localName, String prefix, Attributes attributes, DeserializationContext context) throws SAXException {
        super.startElement(namespace, localName, prefix, attributes, context);
        if (getValue() instanceof DataHandler) {
            try {
                DataHandler dh = (DataHandler) getValue();
                InputStream in = dh.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int byte1 = -1;
                while ((byte1 = in.read()) != -1) baos.write(byte1);
                OctetStream os = new OctetStream(baos.toByteArray());
                setValue(os);
            } catch (IOException ioe) {
            }
        }
    }
