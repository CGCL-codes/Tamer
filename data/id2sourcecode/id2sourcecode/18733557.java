    public static File extractDirectory(String directoryName, ZipInputStream zipin, File parentDir) throws FileNotFoundException, IOException {
        byte data[] = new byte[BUFFER];
        BufferedOutputStream out = null;
        String formattedDirectoryName = directoryName.substring(0, directoryName.length() - 1);
        logger.info(TAG + ": Nom du r�pertoire: " + formattedDirectoryName);
        logger.info(TAG + ": Nom du r�pertoire parent: " + parentDir.getAbsolutePath());
        File f = new File(parentDir, formattedDirectoryName);
        if (!f.exists()) {
            if (!new File(parentDir, formattedDirectoryName).mkdir()) {
                logger.warning(TAG + ": Impossible de cr�er le fichier: ");
                return null;
            }
        }
        ZipEntry entry;
        while ((entry = zipin.getNextEntry()) != null && entry.getName().startsWith(formattedDirectoryName)) {
            if (entry.isDirectory()) {
                new File(parentDir, entry.getName()).mkdir();
                continue;
            }
            File tempFile = new File(parentDir, entry.getName());
            if (tempFile.exists()) tempFile.delete();
            tempFile.createNewFile();
            FileOutputStream dest = new FileOutputStream(tempFile);
            out = new BufferedOutputStream(dest, BUFFER);
            int count;
            while ((count = zipin.read(data, 0, BUFFER)) != -1) out.write(data, 0, BUFFER);
            out.flush();
            out.close();
            zipin.closeEntry();
        }
        return new File(parentDir, formattedDirectoryName);
    }
