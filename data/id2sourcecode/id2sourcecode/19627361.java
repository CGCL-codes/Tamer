    public String highlight(String name) {
        try {
            URL url = sourceToURL.get(name);
            SourceLines lines = sourceLines.get(name);
            if (url == null || lines == null) return null;
            InputStream is = url.openStream();
            String fname = basedir.getPath() + File.separator + name + ".html";
            File dir = new File(fname.substring(0, fname.lastIndexOf(File.separatorChar)));
            if (!dir.exists()) dir.mkdirs();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Renderer renderer = makeRenderer(name);
            log.fine("using renderer " + renderer.getClass().getName());
            renderer.highlight(name, is, bos, "UTF-8", false);
            addCoveredLinesFormat(name, fname, bos.toByteArray(), lines);
            log.fine("done highlighting, returning " + urlPrefix + name + ".html");
            return urlPrefix + name + ".html";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
