    public static byte[] readInputStream(String sPath) {
        try {
            int readSize;
            File file = new File(sPath);
            if (file.isFile() && file.exists()) {
                InputStream in = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while ((readSize = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, readSize);
                }
                in.close();
                return out.toByteArray();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
