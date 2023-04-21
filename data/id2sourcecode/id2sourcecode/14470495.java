    public void createArchive(File directory) throws ExportException, IllegalArgumentException {
        if (!directory.isDirectory()) throw new IllegalArgumentException("expecting a directory");
        ZipOutputStream zos = null;
        try {
            File[] files = directory.listFiles();
            if (files.length > 0) {
                final File archive = new File(directory, directory.getName() + ".zip");
                zos = new ZipOutputStream(new FileOutputStream(archive));
                for (File file : files) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    IOUtils.write(FileUtils.readFileToByteArray(file), zos);
                    zos.closeEntry();
                }
                if (logger.isDebugEnabled()) logger.debug("archived " + files.length + " files to " + archive.getPath());
            }
        } catch (IOException ioe) {
            throw new ExportException(ioe);
        } finally {
            IOUtils.closeQuietly(zos);
        }
    }
