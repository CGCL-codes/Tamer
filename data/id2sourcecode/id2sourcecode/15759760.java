    public static byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read;
            while ((read = ios.read(buffer)) != -1) ous.write(buffer, 0, read);
        } finally {
            if (ous != null) ous.close();
            if (ios != null) ios.close();
        }
        return ous.toByteArray();
    }
