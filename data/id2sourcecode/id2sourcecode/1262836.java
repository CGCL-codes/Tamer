    public Collection<String> citesteConcluzii() throws IOException {
        URL url = new URL(urlulSpreLocatiaCurenta, fisier);
        BufferedReader reader = new BufferedReader(new InputStreamReader((url.openStream())));
        Collection<String> rezultat = new ArrayList<String>();
        String line = "";
        while (!"".equals(line = reader.readLine())) ;
        while ((line = reader.readLine()) != null) {
            if (line != "") rezultat.add(line);
        }
        return rezultat;
    }
