    private void writeGraphPath(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            GraphPath entry = new GraphPath();
            entry.setId(Integer.valueOf(data[0]));
            entry.setTermByTerm1Id((Term) session.get(Term.class, Integer.valueOf(data[1])));
            entry.setTermByTerm2Id((Term) session.get(Term.class, Integer.valueOf(data[2])));
            if (!data[3].equals("\\N")) entry.setTermByRelationshipTypeId((Term) session.get(Term.class, Integer.valueOf(data[3])));
            entry.setDistance(Integer.valueOf(data[4]));
            if (!data[5].equals("\\N")) entry.setRelationDistance(Integer.valueOf(data[5]));
            insertObject(entry);
        }
        reader.close();
    }
