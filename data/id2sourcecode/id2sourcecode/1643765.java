    private void writeHomolset(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Homolset entry = new Homolset();
            entry.setId(Integer.valueOf(data[0]));
            entry.setSymbol(data[1]);
            entry.setDbxref((Dbxref) session.get(Dbxref.class, Integer.valueOf(data[2])));
            if (!data[3].equals("\\N")) entry.setGeneProduct((GeneProduct) session.get(GeneProduct.class, Integer.valueOf(data[3])));
            if (!data[4].equals("\\N")) entry.setSpecies((Species) session.get(Species.class, Integer.valueOf(data[4])));
            if (!data[5].equals("\\N")) entry.setTerm((Term) session.get(Term.class, Integer.valueOf(data[5])));
            if (!data[6].equals("\\N")) entry.setDescription(data[6]);
            insertObject(entry);
        }
        reader.close();
    }
