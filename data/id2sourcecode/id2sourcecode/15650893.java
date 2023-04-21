    public OTFClientQuad convertToClient(String id, final OTFServerRemote host, final OTFConnectionManager connect) {
        final OTFClientQuad client = new OTFClientQuad(id, host, 0., 0., this.easting, this.northing);
        client.offsetEast = this.minEasting;
        client.offsetNorth = this.minNorthing;
        this.execute(0., 0., this.easting, this.northing, new ConvertToClientExecutor(connect, client));
        for (OTFDataWriter<?> element : this.additionalElements) {
            Collection<Class<OTFDataReader>> readerClasses = connect.getReadersForWriter(element.getClass());
            for (Class<? extends OTFDataReader> readerClass : readerClasses) {
                try {
                    Object reader = readerClass.newInstance();
                    client.addAdditionalElement((OTFDataReader) reader);
                    log.info("Connected additional element writer " + element.getClass().getName() + "(" + element + ")  to " + reader.getClass().getName() + " (" + reader + ")");
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return client;
    }
