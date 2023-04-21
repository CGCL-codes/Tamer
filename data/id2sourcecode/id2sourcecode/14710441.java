    public PrideModReader(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("Xml file to be indexed must not be null");
        }
        try {
            this.unmarshaller = PrideModUnmarshallerFactory.getInstance().initializeUnmarshaller();
            prideMod_whole = (PrideMod) unmarshaller.unmarshal(url.openStream());
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Error unmarshalling XML file: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading XML file: " + e.getMessage(), e);
        }
    }
