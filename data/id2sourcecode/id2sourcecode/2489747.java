    public void loadVertexShader(URL url) {
        String shaderSource;
        try {
            shaderSource = PApplet.join(PApplet.loadStrings(url.openStream()), "\n");
            attachVertexShader(shaderSource, url.getFile());
        } catch (IOException e) {
            System.err.println("Cannot load file " + url.getFile());
        }
    }
