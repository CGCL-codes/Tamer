    protected RadicalCleavageIntensity(int Nmer) {
        super("Radical cleavage intensity (" + Nmer + "-mer)");
        this.Nmer = Nmer;
        mapping = new HashMap<String, double[]>();
        try {
            URL url = RadicalCleavageIntensity.class.getResource("rci" + Nmer + ".tsv");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = in.readLine();
            while (line != null) {
                String[] arr = line.split("\t");
                double[] tmp = new double[Nmer];
                for (int i = 1; i <= Nmer; i++) {
                    tmp[i - 1] = Double.parseDouble(arr[i]);
                }
                mapping.put(arr[0].toLowerCase(), tmp);
                line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not construct radical cleavage intensity property");
        }
    }
