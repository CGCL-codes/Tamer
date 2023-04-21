    public static void sendFile(String filename, OutputStream out) throws FileNotFoundException, IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) out.write(buf, 0, bytesRead);
        } finally {
            if (fis != null) fis.close();
        }
    }
