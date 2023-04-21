    protected String readResource(String path) {
        InputStream in = getClass().getResourceAsStream(path);
        if (in == null) throw new RuntimeException("can't find resource `" + path + "'");
        try {
            StringWriter writer = new StringWriter();
            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
            char[] buf = new char[1024];
            for (int r; (r = reader.read(buf)) != -1; ) writer.write(buf, 0, r);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("error reading resource `" + path + "'", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
