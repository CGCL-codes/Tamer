    public static OutputStream getOutputStream(URL url) throws IOException {
        if (isFile(url)) {
            File file = new File(url.getPath());
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
            }
            return new FileOutputStream(file);
        }
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }
