    private void writeTermDbxref(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            TermDbxref entry = new TermDbxref();
            TermDbxrefId id = new TermDbxrefId(Integer.valueOf(data[0]), Integer.valueOf(data[1]), Integer.valueOf(data[2]));
            entry.setId(id);
            entry.setTerm((Term) session.get(Term.class, Integer.valueOf(data[0])));
            entry.setDbxref((Dbxref) session.get(Dbxref.class, Integer.valueOf(data[1])));
            insertObject(entry);
        }
        reader.close();
    }
