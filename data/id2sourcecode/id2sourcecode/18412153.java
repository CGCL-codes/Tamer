    public void writeRepositoriesToXML(Collection<TaskRepository> repositories, File file) {
        ZipOutputStream outputStream = null;
        try {
            if (!file.exists()) file.createNewFile();
            outputStream = new ZipOutputStream(new FileOutputStream(file));
            ZipEntry zipEntry = new ZipEntry(TaskRepositoryManager.OLD_REPOSITORIES_FILE);
            outputStream.putNextEntry(zipEntry);
            outputStream.setMethod(ZipOutputStream.DEFLATED);
            writer.setOutputStream(outputStream);
            writer.writeRepositoriesToStream(repositories);
            outputStream.flush();
            outputStream.closeEntry();
            outputStream.close();
        } catch (IOException e) {
            StatusHandler.fail(e, "Could not write: " + file.getAbsolutePath(), false);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    StatusHandler.fail(e, "Unable to terminate output stream to repositories file.", false);
                }
            }
        }
    }
