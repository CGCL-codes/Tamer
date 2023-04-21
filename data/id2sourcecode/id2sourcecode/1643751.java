    private void writeTermSynonym(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            TermSynonym entry = new TermSynonym();
            entry.setTermId((Term) session.get(Term.class, Integer.valueOf(data[0].trim())));
            entry.setTermSynonym(data[1]);
            if (!data[2].equals("\\N")) entry.setAccSynonym(data[2]);
            entry.setTermSynonymTypeId((Term) session.get(Term.class, Integer.valueOf(data[3])));
            if (!data[4].equals("\\N")) entry.setTermSynonymCategoryId((Term) session.get(Term.class, Integer.valueOf(data[4])));
            insertObject(entry);
        }
        reader.close();
    }
