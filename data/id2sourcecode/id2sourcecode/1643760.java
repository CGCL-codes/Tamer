    private void writeAssociationSpeciesQualifier(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            AssociationSpeciesQualifier entry = new AssociationSpeciesQualifier();
            entry.setId(Integer.valueOf(data[0]));
            entry.setAssociation((Association) session.get(Association.class, Integer.valueOf(data[1])));
            entry.setSpecies((Species) session.get(Species.class, Integer.valueOf(data[2])));
            insertObject(entry);
        }
        reader.close();
    }
