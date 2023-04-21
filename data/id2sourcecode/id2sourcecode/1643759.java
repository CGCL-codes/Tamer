    private void writeAssociationQualifier(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            AssociationQualifier entry = new AssociationQualifier();
            entry.setId(Integer.valueOf(data[0]));
            entry.setAssociation((Association) session.get(Association.class, Integer.valueOf(data[1])));
            entry.setTerm((Term) session.get(Term.class, Integer.valueOf(data[2])));
            if (!data[3].equals("\\N")) entry.setValue(data[3]);
            insertObject(entry);
        }
        reader.close();
    }
