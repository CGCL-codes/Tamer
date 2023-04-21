    private void writeGeneProductHomolset(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            GeneProductHomolset entry = new GeneProductHomolset();
            entry.setId(Integer.valueOf(data[0]));
            entry.setGeneProduct((GeneProduct) session.get(GeneProduct.class, Integer.valueOf(data[1])));
            entry.setHomolset((Homolset) session.get(Homolset.class, Integer.valueOf(data[2])));
            insertObject(entry);
        }
        reader.close();
    }
