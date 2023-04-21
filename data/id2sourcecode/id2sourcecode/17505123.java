    public void processUrl(URL url) throws IOException {
        File parent = new File(targetLanguage);
        File file = buildFileFromUrl(url, parent);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            URLConnection connection = DocumentHandlingUrlProcessor.openConnection(url);
            InputStream s = connection.getInputStream();
            byte[] b = new byte[16384];
            int c = 0;
            FileOutputStream o = new FileOutputStream(file);
            try {
                do {
                    o.write(b, 0, c);
                    c = s.read(b);
                } while (c > 0);
                o.flush();
            } finally {
                o.close();
            }
            s.close();
        }
    }
