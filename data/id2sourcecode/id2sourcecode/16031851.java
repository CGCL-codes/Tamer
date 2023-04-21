    protected long lastModifiedImpl(String key) {
        try {
            long lm;
            String realPath = servletContext.getRealPath(rootDirectory + key);
            if (realPath == null) {
                URL url = servletContext.getResource(rootDirectory + key);
                if (url == null) throw new ResourceNotFoundException("Cannot read from file " + key);
                lm = url.openConnection().getLastModified();
            } else {
                File file = new File(realPath);
                if (!file.canRead()) throw new ResourceNotFoundException("Cannot read from file " + key);
                lm = file.lastModified();
            }
            if (lm == 0) lm = 1;
            return lm;
        } catch (IOException e) {
            throw new OXFException(e);
        }
    }
