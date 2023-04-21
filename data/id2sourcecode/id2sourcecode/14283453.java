    private Collection<FileTrackingStatus> getFilesHttp(String attribute, int from, int max) throws ResourceException, IOException {
        int clientPort = configuration.getInt(CoordinationProperties.PROP.COORDINATION_PORT.toString(), (Integer) CoordinationProperties.PROP.COORDINATION_PORT.getDefaultValue());
        LOG.info("Connecting client to " + clientPort);
        ClientResource clientResource = new ClientResource("http://localhost:" + clientPort + "/files/list" + attribute);
        StringWriter writer = new StringWriter();
        if (from > -1) {
            clientResource.setRanges(Arrays.asList(new Range(from, max)));
        }
        try {
            clientResource.get(MediaType.APPLICATION_JSON).write(writer);
        } finally {
            clientResource.release();
        }
        return formatter.readList(FORMAT.JSON, new StringReader(writer.toString()));
    }
