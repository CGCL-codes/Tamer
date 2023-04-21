    public String getFilesStringFromZipStream(InputStream in, String EntryFileName) throws Exception {
        String fileValueString = new String("");
        ZipInputStream zin = null;
        zin = new ZipInputStream(new BufferedInputStream(in));
        ByteArrayOutputStream out = null;
        ZipEntry zipEntry = null;
        while ((zipEntry = zin.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                String EntryName = zipEntry.getName();
                if (EntryFileName.equals(EntryName)) {
                    out = new ByteArrayOutputStream();
                    int readLen;
                    byte dataBuff[] = new byte[bufferSize];
                    while ((readLen = zin.read(dataBuff)) > 0) {
                        out.write(dataBuff, 0, readLen);
                    }
                    out.flush();
                    out.close();
                    zin.close();
                    if ("".equals(fileValueString)) {
                        fileValueString = out.toString();
                        return fileValueString;
                    }
                } else {
                    int readLen;
                    byte dataBuff[] = new byte[bufferSize];
                    while ((readLen = zin.read(dataBuff)) > 0) {
                    }
                }
            }
        }
        zin.close();
        return fileValueString;
    }
