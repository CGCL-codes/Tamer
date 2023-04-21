    private void loadGraph(URL url, LoadedGraph<SceneGraphObject> graph) throws IOException, IncorrectFormatException, ParsingErrorException {
        NullArgumentException.check(url);
        final String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            final String filename = url.getFile();
            loadGraph(filename, graph);
            return;
        }
        final String path = url.getPath();
        final int filenameIndex = path.lastIndexOf('/');
        final String filename = path.substring(filenameIndex + 1);
        final int extensionIndex = filename.indexOf('.');
        final String prefix = filename.substring(0, extensionIndex) + '_';
        final String suffix = filename.substring(extensionIndex);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(prefix, suffix);
            tempFile.deleteOnExit();
            final String tempFilename = tempFile.getCanonicalPath();
            System.out.println(getClass().getName() + ":  " + "creating temporary file \"" + tempFilename + "\"...");
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
            int i;
            while ((i = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(i);
            }
            bufferedInputStream.close();
            bufferedOutputStream.close();
            loadGraph(tempFilename, graph);
            return;
        } catch (final IOException ex) {
            throw (FileNotFoundException) new FileNotFoundException().initCause(ex);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }
