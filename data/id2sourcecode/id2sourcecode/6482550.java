    protected boolean resolveArtifactDependencies(URLBuilder builder, Artifact artifact, LocalCacheStore localCache) throws SavantException {
        URL url = builder.buildDepsURL(defaultDomain, mapping, artifact);
        if (url != null) {
            URLConnection uc = openConnection(url);
            if (isValid(uc)) {
                InputStream is = null;
                File tmp = null;
                try {
                    is = uc.getInputStream();
                    tmp = File.createTempFile("savant", "deps");
                    FileTools.output(is, tmp);
                    ArtifactTools.resolveArtifactDependencies(artifact, tmp);
                    localCache.storeDeps(artifact, tmp);
                    return true;
                } catch (IOException e) {
                    Log.log("Unable to locate artifact dependency XML for artifact [" + artifact + "]", Log.DEBUG);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (tmp != null) {
                            tmp.delete();
                        }
                    } catch (IOException ioe) {
                        throw new SavantException(ioe);
                    }
                }
            }
        }
        return false;
    }
