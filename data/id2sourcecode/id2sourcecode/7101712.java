    public static GenotypeTable readFasta(String filename) throws FileNotFoundException, IOException {
        if (filename.startsWith("http")) {
            URL url = new URL(filename);
            return readFasta(new BufferedReader(new InputStreamReader(url.openStream())), filename);
        } else {
            return readFasta(new BufferedReader(new FileReader(filename)), filename);
        }
    }
