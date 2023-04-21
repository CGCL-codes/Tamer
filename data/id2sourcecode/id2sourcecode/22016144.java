    private void writeJarSurfClassfiles() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("jarsurf/classes.txt")));
        String filename;
        while ((filename = in.readLine()) != null) writeClassFile(filename);
    }
