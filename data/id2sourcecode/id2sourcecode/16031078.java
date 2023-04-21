    public void unZip(String unZipfileName, String extFolder) {
        FileOutputStream fileOut;
        File file;
        InputStream inputStream;
        try {
            this.zipFile = new ZipFile(unZipfileName);
            for (Enumeration entries = this.zipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                file = new File(extFolder + entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    inputStream = zipFile.getInputStream(entry);
                    fileOut = new FileOutputStream(file);
                    while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
                        fileOut.write(this.buf, 0, this.readedBytes);
                    }
                    fileOut.close();
                    inputStream.close();
                }
            }
            this.zipFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
