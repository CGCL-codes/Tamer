    public ArtifactResult loadArtifactResult(String query) {
        try {
            Unmarshaller unmarshaller = getUnmarshaller();
            unmarshaller.setValidation(false);
            URL url = new URL(query);
            InputStream stream = url.openStream();
            ArtifactResult artifactResult = (ArtifactResult) unmarshaller.unmarshal(new InputSource(stream));
            return artifactResult;
        } catch (Exception ex) {
            Utilities.log(ex);
        }
        return null;
    }
