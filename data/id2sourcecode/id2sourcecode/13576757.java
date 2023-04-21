    private void resourceCopy(String resource, IProject project, String target, IProgressMonitor monitor, String toReplace, String replacement, String charset) throws URISyntaxException, IOException {
        IFile targetFile = project.getFile(target);
        URL url = bundle.getEntry(resource);
        InputStream is = null;
        ByteArrayInputStream bais = null;
        try {
            is = FileLocator.toFileURL(url).openStream();
            int len = is.available();
            byte[] buf = new byte[len];
            is.read(buf);
            if (toReplace == null || toReplace.isEmpty()) {
                bais = new ByteArrayInputStream(buf);
            } else {
                String str = new String(buf, charset);
                str = str.replaceAll(toReplace, replacement);
                bais = new ByteArrayInputStream(str.getBytes(charset));
            }
            if (targetFile.exists()) {
                targetFile.setContents(bais, true, false, monitor);
            } else {
                targetFile.create(bais, true, monitor);
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (bais != null) {
                bais.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
