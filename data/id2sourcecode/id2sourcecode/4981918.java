    public static void writeToFile(URL url, File file) throws IOException, FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(file);
        try {
            InputStream is = url.openStream();
            try {
                byte[] buf = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
            } finally {
                is.close();
            }
        } finally {
            fos.close();
        }
    }
