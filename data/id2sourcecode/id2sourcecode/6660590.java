    public static File createFileFromInputStream(InputStream inputStream, String filePath) {
        File file = null;
        if (null != inputStream) {
            try {
                file = new File(filePath);
                if (!file.exists()) file.createNewFile();
                OutputStream outputStream = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, length);
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                MobilisManager.getLogger().log(Level.WARNING, "createFileFromInputStream Error: " + e.getMessage());
            }
        }
        return file;
    }
