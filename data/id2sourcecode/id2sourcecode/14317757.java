    public RandomAccessMemoryMapBuffer(InputStream in) {
        this.pointer = -1;
        length = 0;
        FileOutputStream to = null;
        BufferedInputStream from = null;
        try {
            file = File.createTempFile("page", ".bin", new File(ObjectStore.temp_dir));
            to = new java.io.FileOutputStream(file);
            from = new BufferedInputStream(in);
            byte[] buffer = new byte[65535];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytes_read);
                length = length + bytes_read;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (to != null) to.close();
            if (from != null) from.close();
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " closing files");
        }
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
