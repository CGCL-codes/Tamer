    private void writeSpecies(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Species entry = new Species();
            entry.setId(Integer.valueOf(data[0]));
            entry.setNcbiTaxaId(Integer.valueOf(data[1]));
            if (!data[2].equals("\\N")) entry.setCommonName(data[2]);
            if (!data[3].equals("\\N")) entry.setLineageString(data[3]);
            entry.setGenus(data[4]);
            entry.setSpecies(data[5]);
            if (!data[6].equals("\\N")) entry.setParentId(Integer.valueOf(data[6]));
            if (!data[6].equals("\\N")) entry.setLeftValue(Integer.valueOf(data[7]));
            if (!data[6].equals("\\N")) entry.setRightValue(Integer.valueOf(data[8]));
            entry.setTaxonomicRank(data[9]);
            insertObject(entry);
        }
        reader.close();
    }
