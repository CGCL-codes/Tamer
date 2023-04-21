    public boolean writeFile(InputStream is, File dest) {
        try {
            FileOutputStream os = new FileOutputStream(dest);
            int c;
            byte[] buffer = new byte[512];
            while ((c = is.read(buffer)) != -1) os.write(buffer, 0, c);
            os.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
