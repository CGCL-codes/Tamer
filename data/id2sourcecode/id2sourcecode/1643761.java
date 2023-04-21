    private void writeEvidence(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Evidence entry = new Evidence();
            entry.setId(Integer.valueOf(data[0]));
            entry.setCode(data[1]);
            entry.setAssociation((Association) session.get(Association.class, Integer.valueOf(data[2])));
            entry.setDbxref((Dbxref) session.get(Dbxref.class, Integer.valueOf(data[3])));
            if (data.length > 4) entry.setSeqAcc(data[4]);
            insertObject(entry);
        }
        reader.close();
    }
