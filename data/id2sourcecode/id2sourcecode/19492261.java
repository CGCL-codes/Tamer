    public static void imgSave(byte[] readData, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(folderPath + fileName);
            fos.write(readData);
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
