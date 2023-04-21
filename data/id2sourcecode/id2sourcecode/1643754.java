    private void writeTerm2TermMetadata(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Term2termMetadata entry = new Term2termMetadata();
            entry.setId(Integer.valueOf(data[0]));
            entry.setTermByRelationshipTypeId((Term) session.get(Term.class, Integer.valueOf(data[1])));
            entry.setTermByTerm1Id((Term) session.get(Term.class, Integer.valueOf(data[2])));
            entry.setTermByTerm2Id((Term) session.get(Term.class, Integer.valueOf(data[3])));
            insertObject(entry);
        }
        reader.close();
    }
