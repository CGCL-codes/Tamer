    private Collection<FileTrackingStatus> getFilesHttp(String attribute, int from, int max) throws ResourceException, IOException {
        int clientPort = configuration.getInt(AgentProperties.MONITORING_PORT, 8040);
        LOG.info("Connecting client to " + clientPort);
        String suffix = "/files/list";
        if (attribute != null && attribute.length() > 0) {
            suffix += "/" + attribute;
        }
        ClientResource clientResource = new ClientResource("http://localhost:" + clientPort + suffix);
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
