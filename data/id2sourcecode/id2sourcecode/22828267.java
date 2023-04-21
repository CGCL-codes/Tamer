    public File put(String netPath) {
        File file = null;
        try {
            URL url = new URL(netPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            if (in != null) {
                file = Utilities.getTempFile(cacheDir);
                OutputStream out = file.getOutputStream();
                byte[] buf = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buf)) > 0) out.write(buf, 0, bytesRead);
                out.close();
                in.close();
            }
        } catch (IOException e) {
            Log.error(e);
            if (file.exists()) file.delete();
            file = null;
        }
        if (file != null) {
            catalog.add(new StringPair(file.getName(), netPath));
            saveCatalog();
        }
        return file;
    }
