    private void writeRelationProperties(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            RelationProperties entry = new RelationProperties();
            Term term = (Term) session.get(Term.class, Integer.valueOf(data[0]));
            if (term != null) entry.setTerm(term); else entry.setTerm((Term) session.get(Term.class, Integer.valueOf(1)));
            if (!data[1].equals("\\N")) entry.setIsTransitive(Integer.valueOf(data[1]));
            if (!data[2].equals("\\N")) entry.setIsSymmetric(Integer.valueOf(data[2]));
            if (!data[3].equals("\\N")) entry.setIsAntiSymmetric(Integer.valueOf(data[3]));
            if (!data[4].equals("\\N")) entry.setIsCyclic(Integer.valueOf(data[4]));
            if (!data[5].equals("\\N")) entry.setIsReflexive(Integer.valueOf(data[5]));
            if (!data[6].equals("\\N")) entry.setIsMetadataTag(Integer.valueOf(data[6]));
            insertObject(entry);
        }
        reader.close();
    }
