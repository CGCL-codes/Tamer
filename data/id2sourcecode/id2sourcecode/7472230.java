    public String[] getChannelDimTypes(String id) throws FormatException, IOException {
        reader.setId(id);
        return reader.getChannelDimTypes();
    }
