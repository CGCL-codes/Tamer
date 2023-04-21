    private byte[] readFile(final String filePath) {
        InputStream is = null;
        byte[] data = null;
        try {
            String path = filePath;
            String inputFileName;
            File file = new File(filePath);
            if (file.exists()) {
                is = new FileInputStream(file);
                inputFileName = file.getName();
            } else {
                URL url = new URL(path);
                is = url.openConnection().getInputStream();
                inputFileName = url.getFile();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[0xFF];
            int i = is.read(buffer);
            int done = 0;
            while (i != -1) {
                bos.write(buffer, 0, i);
                done += i;
                i = is.read(buffer);
            }
            data = bos.toByteArray();
            fileName = simpleFileName(inputFileName);
            showStatus((data.length / 1024) + "k " + fileName);
        } catch (Throwable e) {
            Logger.error(e);
            showStatus("Download error " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return data;
    }
