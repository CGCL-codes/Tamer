    protected void createZipArchive(File file, File[] afile) {
        try {
            byte abyte0[] = new byte[1024];
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            ZipOutputStream zipoutputstream = new ZipOutputStream(fileoutputstream);
            for (int i = 0; i < afile.length; i++) {
                if (afile[i] != null && afile[i].exists() && !afile[i].isDirectory()) {
                    setStatus("Adding " + afile[i].getName());
                    ZipEntry zipentry = new ZipEntry(afile[i].getName());
                    zipentry.setTime(afile[i].lastModified());
                    zipoutputstream.putNextEntry(zipentry);
                    FileInputStream fileinputstream = new FileInputStream(afile[i]);
                    do {
                        int j = fileinputstream.read(abyte0, 0, abyte0.length);
                        if (j <= 0) {
                            break;
                        }
                        zipoutputstream.write(abyte0, 0, j);
                    } while (true);
                    fileinputstream.close();
                }
            }
            zipoutputstream.close();
            fileoutputstream.close();
            setStatus("Adding completed OK");
        } catch (Exception exception) {
            exception.printStackTrace();
            setStatus("Error: " + exception.getMessage());
        }
    }
