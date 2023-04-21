    private void writeRelationComposition(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            RelationComposition entry = new RelationComposition();
            entry.setId(Integer.valueOf(data[0]));
            entry.setTermByRelation1Id((Term) session.get(Term.class, Integer.valueOf(data[1])));
            entry.setTermByRelation2Id((Term) session.get(Term.class, Integer.valueOf(data[2])));
            entry.setTermByInferredRelationId((Term) session.get(Term.class, Integer.valueOf(data[3])));
            insertObject(entry);
        }
        reader.close();
    }
