    private void writeTermDefinition(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            TermDefinition entry = new TermDefinition();
            entry.setTermId((Term) session.get(Term.class, Integer.valueOf(data[0])));
            entry.setTermDefinition(data[1]);
            if (!data[2].equals("\\N")) entry.setDbxref((Dbxref) session.get(Dbxref.class, Integer.valueOf(data[2])));
            if (!data[3].equals("\\N")) entry.setTermComment(data[3]);
            if (!data[4].equals("\\N")) entry.setReference(data[4]);
            insertObject(entry);
        }
        reader.close();
    }
