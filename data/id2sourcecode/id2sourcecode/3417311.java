    public static void copyFileFromUrl(File trg, URL url) throws IOException {
        trg.getParentFile().mkdirs();
        InputStream is = url.openStream();
        try {
            FileOutputStream fos = new FileOutputStream(trg);
            try {
                BufferedInputStream bis = new BufferedInputStream(is, 16384);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 16384);
                int c;
                while ((c = bis.read()) >= 0) {
                    bos.write(c);
                }
                bos.close();
            } finally {
                fos.close();
            }
        } finally {
            is.close();
        }
    }
