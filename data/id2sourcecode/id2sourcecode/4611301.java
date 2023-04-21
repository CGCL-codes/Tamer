    private static void writePrepreparedText(String filename, Writer w) throws IOException {
        URL url = Util.getResource(SCHEMAGEN_DIR + filename);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(url.openStream()));
        while (true) {
            String line = bReader.readLine();
            if (line == null) {
                break;
            }
            w.write(line + "\n");
        }
        bReader.close();
    }
