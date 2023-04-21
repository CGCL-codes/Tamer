    private void loadDrawing(String filename) {
        try {
            URL url = new URL(getCodeBase(), filename);
            InputStream stream = url.openStream();
            StorableInput reader = new StorableInput(stream);
            fDrawing = (Drawing) reader.readStorable();
        } catch (IOException e) {
            fDrawing = createDrawing();
            System.err.println("Error when Loading: " + e);
            showStatus("Error when Loading: " + e);
        }
    }
