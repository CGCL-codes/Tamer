    @Override
    public IMedium createMedium(String urlString, IMetadata optionalMetadata) throws MM4UCannotCreateMediumElementsException {
        try {
            URL url = new URL(urlString);
            ImageInfo imageInfo = new ImageInfo();
            InputStream imageSteam = url.openStream();
            imageInfo.setInput(imageSteam);
            if (!imageInfo.check()) {
                throw new MM4UCannotCreateMediumElementsException(this, "public IMedium createMedium( String urlString, Metadata optionalMetadata )", "Can not create connector medium from given url: " + urlString + "\n(Reason: Not a supported image file format)");
            }
            int width = imageInfo.getWidth();
            int height = imageInfo.getHeight();
            imageSteam.close();
            String mimeType = Utilities.getMimetype(Utilities.getURISuffix(urlString));
            if (optionalMetadata == null) optionalMetadata = new Metadata();
            optionalMetadata.addIfNotNull(IMedium.MEDIUM_METADATA_MIMETYPE, mimeType);
            return new Image(this, width, height, urlString, optionalMetadata);
        } catch (IOException excp) {
            throw new MM4UCannotCreateMediumElementsException(this, "createConnectorMedium", "Can not create connector medium from given url: " + urlString + "\n(Reason: " + excp.getMessage() + ")");
        }
    }
