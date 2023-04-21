    public Collection<Attribute> readAttributes() throws IOException {
        Collection<Attribute> returnValue = new ArrayList<Attribute>();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        while (ready()) {
            stream.write(read());
        }
        stream.close();
        String attributeDatas = stream.toString();
        if (attributeDatas != null) {
            for (String attributeData : attributeDatas.split(ParameterKeys.ATTRIBUTES_SEPARATOR)) {
                Attribute attribute = null;
                try {
                    attribute = convertToAttribute(attributeData);
                    returnValue.add(attribute);
                } catch (IOException e) {
                    LOG.debug(e.getMessage());
                }
            }
        }
        return returnValue;
    }
