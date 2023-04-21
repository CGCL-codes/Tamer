    public void loadGeometryShader(URL url) {
        String source;
        try {
            source = PApplet.join(PApplet.loadStrings(url.openStream()), "\n");
            createGeometryProgram(source, url.getFile());
        } catch (IOException e) {
            System.err.println("Cannot load file " + url.getFile());
        }
    }
