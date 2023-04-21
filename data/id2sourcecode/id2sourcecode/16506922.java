    private DataSource createZipStreamFromDataHandlers(DataHandler[] mediaDH) {
        Calendar now = new GregorianCalendar();
        String tempDir = "C:\\WesodiTemp\\";
        String nowTime = "" + now.getTimeInMillis();
        File zipOutDir = new File(tempDir);
        if (!zipOutDir.exists()) {
            zipOutDir.mkdirs();
        }
        File zipOutFile = new File(tempDir + "Temp_" + nowTime + ".zip");
        try {
            zipOutFile.createNewFile();
            FileOutputStream dest = new FileOutputStream(zipOutFile);
            ZipOutputStream zipOutStream = new ZipOutputStream(new BufferedOutputStream(dest));
            for (DataHandler dh : mediaDH) {
                ZipEntry entry = new ZipEntry(new File(dh.getName()).getName());
                zipOutStream.putNextEntry(entry);
                dh.writeTo(zipOutStream);
            }
            zipOutStream.close();
            DataSource out = new FileDataSource(zipOutFile);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
