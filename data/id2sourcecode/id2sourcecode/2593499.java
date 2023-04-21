    public void loadVertexShader(URL url) {
        String source;
        try {
            source = PApplet.join(PApplet.loadStrings(url.openStream()), "\n");
            createVertexProgram(source, url.getFile());
        } catch (IOException e) {
            System.err.println("Cannot load file " + url.getFile());
        }
    }
