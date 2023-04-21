    public Document getSdlDomResource(String aResourceName) throws AeException {
        InputStream in = null;
        try {
            URL url = getDeploymentContext().getResourceURL(aResourceName);
            if (url == null) {
                return null;
            } else {
                in = url.openStream();
                return getSdlParser().loadSdlDocument(in, null);
            }
        } catch (Throwable t) {
            logger.error("Error: unable to load: " + aResourceName + " from " + getDeploymentContext().getDeploymentLocation());
            throw new SdlDeploymentException(MessageFormat.format("unable to load: {0} from {1}", new Object[] { aResourceName, getDeploymentContext().getDeploymentLocation() }), t);
        } finally {
            AeCloser.close(in);
        }
    }
