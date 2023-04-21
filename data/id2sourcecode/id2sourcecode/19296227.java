    protected URI copyFile(URL base, String name, Map<URI, URI> copied) throws IOException, URISyntaxException {
        URL original = new URL(base, name);
        URI originalURI = original.toURI();
        File saveFile = new File(this.save, name);
        URI saveURI = saveFile.toURI();
        InputStream is = original.openStream();
        OutputStream os;
        byte[] buffer = new byte[1024];
        int count;
        if (copied.containsKey(originalURI)) return copied.get(originalURI);
        this.logger.fine("Copy from " + original + " to " + saveFile);
        if (!saveFile.getParentFile().exists()) if (!saveFile.getParentFile().mkdirs()) throw new IOException("Can't create " + saveFile.getParentFile());
        os = new FileOutputStream(saveFile);
        while ((count = is.read(buffer)) > 0) os.write(buffer, 0, count);
        os.close();
        is.close();
        copied.put(originalURI, saveURI);
        copied.put(saveURI, saveURI);
        return saveURI;
    }
