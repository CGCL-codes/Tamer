    private void writeTermSubset(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            TermSubset entry = new TermSubset();
            entry.setTermId((Term) session.get(Term.class, Integer.valueOf(data[0])));
            entry.setSubsetId((Term) session.get(Term.class, Integer.valueOf(data[1])));
            insertObject(entry);
        }
        reader.close();
    }
