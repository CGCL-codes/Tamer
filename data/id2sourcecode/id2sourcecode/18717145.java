    private void copyFiles() throws IOException {
        for (Iterator it = filesToCopy.iterator(); it.hasNext(); ) {
            String[] files = (String[]) it.next();
            if (files[0].startsWith("file:///")) {
                files[0] = files[0].substring("file:///".length());
            }
            URL url = new URL(files[0]);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection != null) {
                InputStream reader = urlConnection.getInputStream();
                FileOutputStream fs = new FileOutputStream(files[1]);
                try {
                    byte[] cbuf = new byte[4096];
                    int cnt = 0;
                    while ((cnt = reader.read(cbuf)) != -1) fs.write(cbuf, 0, cnt);
                } finally {
                    reader.close();
                    fs.close();
                }
            }
        }
    }
