    public void perform(TaskRequest req, TaskResponse res) {
        URL url = resource.evaluate(req, res);
        ZipInputStream zip = null;
        try {
            final String urlExternalForm = url.toExternalForm();
            try {
                zip = new ZipInputStream(new BufferedInputStream(url.openStream()));
            } catch (IOException ioe) {
                throw new RuntimeException("Failed to open input stream for URL: " + urlExternalForm, ioe);
            }
            res.setAttribute(Attributes.CONTEXT, "jar:" + urlExternalForm + "!/");
            try {
                for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    res.setAttribute(Attributes.LOCATION, entry.getName());
                    super.performSubtasks(req, res);
                    zip.closeEntry();
                }
            } catch (IOException ioe) {
                throw new RuntimeException("Failed to read specified archive:  " + urlExternalForm, ioe);
            }
        } finally {
            IOUtils.closeQuietly(zip);
        }
    }
