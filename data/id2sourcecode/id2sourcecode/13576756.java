    private void resourceCopy(String resource, IProject project, String target, IProgressMonitor monitor) throws URISyntaxException, IOException {
        IFile targetFile = project.getFile(target);
        URL url = bundle.getEntry(resource);
        InputStream is = null;
        try {
            is = FileLocator.toFileURL(url).openStream();
            if (targetFile.exists()) {
                targetFile.setContents(is, true, false, monitor);
            } else {
                targetFile.create(is, true, monitor);
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
