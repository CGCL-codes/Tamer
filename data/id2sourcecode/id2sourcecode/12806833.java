    public void create(ContentWriter contentWriter) throws IOException {
        File targetFile = this.getFile();
        if (targetFile.exists()) {
            String message = "The file '" + targetFile.getAbsolutePath() + "' already exists. Will not overwrite it.";
            throw new PackageException(message);
        }
        OutputStream outputStream = new FileOutputStream(targetFile);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            contentWriter.writeContent(writer);
            writer.flush();
        } finally {
            StreamUtil.tryCloseStream(outputStream);
        }
        log.info("Wrote file '" + targetFile.getAbsolutePath() + "'");
    }
