    protected void processFile(File file, Path path) {
        File staticFile = path.getFile(staticDir);
        String fileName = file.getName();
        try {
            if (FileTypes.isPage(fileName)) {
                long time = System.currentTimeMillis();
                URL url = new URL(contextURL, path.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty(USER_AGENT_HEADER, USER_AGENT);
                InputStream in = connection.getInputStream();
                OutputStream out = new FileOutputStream(staticFile);
                Utils.copyStream(in, out, true);
                connection.disconnect();
                write(path + " page generated in " + (System.currentTimeMillis() - time) + " ms");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
