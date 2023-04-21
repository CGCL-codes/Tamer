    public void openURL(URL url) throws IOException {
        InputStream in = url.openStream();
        if (in != null) {
            Graph graph = read(in);
            GraphFrame frame = new GraphFrame(graph);
            frame.setGenerator(this.generator);
            addInternalFrame(frame);
            try {
                frame.setMaximum(true);
            } catch (PropertyVetoException ex) {
            }
        }
    }
