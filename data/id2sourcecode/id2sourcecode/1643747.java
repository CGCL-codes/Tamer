    private void writeTerm2Term(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Term2term entry = new Term2term();
            entry.setId(Integer.valueOf(data[0]));
            entry.setTermByRelationshipTypeId((Term) session.get(Term.class, Integer.valueOf(data[1])));
            entry.setTermByTerm1Id((Term) session.get(Term.class, Integer.valueOf(data[2])));
            entry.setTermByTerm2Id((Term) session.get(Term.class, Integer.valueOf(data[3])));
            entry.setComplete(Integer.valueOf(data[4]));
            insertObject(entry);
        }
        reader.close();
    }
