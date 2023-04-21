    private static ObjectInputStream openSerializationStream(File legacyPanelsObjFile) throws RestoreFailureException {
        BufferedInputStream bis;
        try {
            bis = new BufferedInputStream(new FileInputStream(legacyPanelsObjFile));
        } catch (FileNotFoundException e) {
            throw new RestoreFailureException("Previously checked " + legacyPanelsObjFile.getAbsoluteFile() + " can no longer be found, or was not a proper file : " + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new RestoreFailureException("Security manager has denied read access to " + legacyPanelsObjFile.getAbsoluteFile() + ". Read/write access to designer temporary directory is required.", e);
        }
        try {
            return new ObjectInputStream(bis);
        } catch (SecurityException e) {
            throw new RestoreFailureException("Error in creating object input stream : " + e.getMessage(), e);
        } catch (StreamCorruptedException e) {
            throw new RestoreFailureException("Serialization header is corrupted : " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RestoreFailureException("I/O Error when attempting to read serialization header : " + e.getMessage(), e);
        }
    }
