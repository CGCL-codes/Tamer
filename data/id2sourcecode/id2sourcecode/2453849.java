    protected String importResources(LodFile oldLodFile, File newLodFileToCreate, List filesToImportList, LodResource resourceToImport) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(newLodFileToCreate, "rw");
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            return "Unable to create file '" + newLodFileToCreate.getAbsolutePath() + "': File not found -- " + exception.getMessage();
        }
        try {
            oldLodFile.write(importThread, randomAccessFile, filesToImportList, resourceToImport);
        } catch (IOException exception) {
            exception.printStackTrace();
            return "Unable to create file '" + newLodFileToCreate.getAbsolutePath() + "': IOException -- " + exception.getMessage();
        } catch (InterruptedException exception) {
            return "Cancelled by User.";
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
