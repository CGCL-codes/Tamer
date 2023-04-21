    private void writeTerm(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Term entry = new Term();
            entry.setId(Integer.valueOf(data[0]));
            entry.setName(data[1]);
            entry.setTermType(data[2]);
            entry.setAcc(data[3]);
            entry.setIsObsolete(Integer.valueOf(data[4]));
            entry.setIsRoot(Integer.valueOf(data[5]));
            entry.setIsRelation(Integer.valueOf(data[6]));
            insertObject(entry);
        }
        reader.close();
    }
