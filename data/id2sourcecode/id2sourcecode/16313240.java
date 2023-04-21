    public List<Channel> getChannels() throws IOException {
        Representation representation = channelsResource.get(MediaType.APPLICATION_JSON);
        Reader reader;
        List<Channel> channels;
        reader = representation.getReader();
        try {
            channels = channelsMapper.readValue(reader, new TypeReference<List<Channel>>() {
            });
        } finally {
            reader.close();
        }
        return channels;
    }
