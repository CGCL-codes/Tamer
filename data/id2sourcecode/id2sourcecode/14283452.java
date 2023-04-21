    private <T> Collection<T> getItemNamesHttp(Class<T> cls, String addressPath, int from, int max) throws ResourceException, IOException {
        int clientPort = configuration.getInt(CoordinationProperties.PROP.COORDINATION_PORT.toString(), (Integer) CoordinationProperties.PROP.COORDINATION_PORT.getDefaultValue());
        LOG.info("Connecting client to " + clientPort);
        ClientResource clientResource = new ClientResource("http://localhost:" + clientPort + addressPath);
        StringWriter writer = new StringWriter();
        if (from > -1) {
            clientResource.setRanges(Arrays.asList(new Range(from, max)));
        }
        try {
            clientResource.get().write(writer);
        } finally {
            clientResource.release();
        }
        Collection<T> coll = objectMapper.readValue(writer.toString(), new TypeReference<Collection<T>>() {
        });
        return coll;
    }
