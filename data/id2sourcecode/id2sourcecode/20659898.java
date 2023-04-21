    public void save(File saveFile) throws IOException {
        boolean replace = false;
        if (saveFile == null) {
            replace = true;
            saveFile = File.createTempFile("zodiak-", ".zip");
        }
        ZipInputStream zIn = new ZipInputStream(new FileInputStream(docFile));
        ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(saveFile));
        ZipEntry entry = null;
        while ((entry = zIn.getNextEntry()) != null) {
            String name = entry.getName();
            zOut.putNextEntry(entry);
            if (tempFiles.containsKey(name)) {
                FileInputStream tmpIn = new FileInputStream(tempFiles.get(name));
                copy(tmpIn, zOut);
                tmpIn.close();
            } else {
                copy(zIn, zOut);
            }
            zIn.closeEntry();
            zOut.closeEntry();
        }
        zIn.close();
        zOut.close();
        if (replace) {
            saveFile.renameTo(docFile);
        }
        close();
    }
