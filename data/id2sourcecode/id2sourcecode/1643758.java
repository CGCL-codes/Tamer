    private void writeAssociation(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Association entry = new Association();
            entry.setId(Integer.valueOf(data[0]));
            entry.setTerm((Term) session.get(Term.class, Integer.valueOf(data[1])));
            GeneProduct gp = (GeneProduct) session.get(GeneProduct.class, Integer.valueOf(data[2]));
            if (gp != null) entry.setGeneProduct(gp); else {
                entry.setGeneProduct((GeneProduct) session.get(GeneProduct.class, Integer.valueOf(0)));
            }
            entry.setIsNot(Integer.valueOf(data[3]));
            if (!data[4].equals("\\N")) entry.setRoleGroup(Integer.valueOf(data[4]));
            entry.setAssocdate(Integer.valueOf(data[5]));
            Db db = (Db) session.get(Db.class, Integer.valueOf(data[6]));
            if (db != null) entry.setDb(db); else {
                entry.setDb((Db) session.get(Db.class, Integer.valueOf(1)));
            }
            insertObject(entry);
        }
        reader.close();
    }
