    private void writeGeneProductCount(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            GeneProductCount entry = new GeneProductCount();
            entry.setTerm((Term) session.get(Term.class, Integer.valueOf(data[0])));
            entry.setCode(data[1]);
            if (!data[2].equals("\\N")) entry.setSpeciesdbname(data[2]);
            if (!data[3].equals("\\N")) entry.setSpecies((Species) session.get(Species.class, Integer.valueOf(data[3])));
            entry.setProductCount(Integer.valueOf(data[4]));
            insertObject(entry);
        }
        reader.close();
    }
