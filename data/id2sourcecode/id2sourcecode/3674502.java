    private synchronized void storeMappingToPropertiesFile(String modelFileAbsolutePath) throws IOException {
        File mapFile = createMapFileObject(modelFileAbsolutePath);
        if (mapFile.exists()) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mapFile);
            FileLock lock = fos.getChannel().lock();
            accessionToGAProps.store(fos, "Mapping of model accessions to GA values.");
            lock.release();
        } catch (Exception ioe) {
            mapFile.delete();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
