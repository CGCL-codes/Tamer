    public String[] getChannelDimTypes(String id) throws FormatException, IOException {
        setId(id);
        return getReader().getChannelDimTypes();
    }
