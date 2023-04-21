    protected void returnFile(String filename, OutputStream out) throws FileNotFoundException, IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
