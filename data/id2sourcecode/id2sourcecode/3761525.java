    private void saveSingleSheet(SingleSheet ss) {
        File temp = new File(sheetsDir, ss.getName() + SINGLE_EXT + ".temp");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(temp);
            FilePathListConsumer c = new FilePathListConsumer(fout);
            PathLister.getAll(ss, c, true);
            fout.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can't save " + ss.getName(), e);
            return;
        } finally {
            IoUtils.close(fout);
        }
        File saved = new File(sheetsDir, ss.getName() + SINGLE_EXT);
        try {
            org.apache.commons.io.FileUtils.copyFile(temp, saved, true);
            org.apache.commons.io.FileUtils.forceDelete(temp);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not rename temp file for " + ss.getName(), e);
        }
    }
