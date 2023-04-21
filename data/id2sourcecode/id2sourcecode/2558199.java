    private static String getShaderString(URL url) {
        String fileName = url.getFile();
        String content = savedShaders.get(fileName);
        if (content != null) return content;
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buf = new StringBuilder();
            while (r.ready()) {
                buf.append(r.readLine()).append('\n');
            }
            content = buf.toString();
            savedShaders.put(fileName, content);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
