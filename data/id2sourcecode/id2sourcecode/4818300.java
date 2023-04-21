    public MapToolAsset loadAsset(URL url) {
        Reader reader = null;
        try {
            XStream xstream = ConverterSupport.getXStream(MapToolAsset.class);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            MapToolAsset asset = (MapToolAsset) xstream.fromXML(reader);
            return asset;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Couldn't open property descriptor set url: " + url.toExternalForm(), e);
            throw new IllegalArgumentException("Could not open the file '" + url.toExternalForm() + "'.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn't open property token url: " + url.toExternalForm(), e);
            throw new IllegalArgumentException("Could not open the file '" + url.toExternalForm() + "'.");
        } catch (ConversionException e) {
            LOGGER.log(Level.WARNING, "Invalid Map Tool Asset XML url: " + url.toExternalForm(), e);
            throw new IllegalArgumentException("The Map Tool Asset XML '" + url.toExternalForm() + "' is invalid.", e);
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unexpected error in file : " + url.toExternalForm(), e);
            throw new IllegalArgumentException("Unexpected error in file  '" + url.toExternalForm(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    LOGGER.log(Level.INFO, "Ignoring close exception on '" + url.toExternalForm() + "'.", e1);
                }
            }
        }
    }
