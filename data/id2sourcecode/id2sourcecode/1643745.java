    private void writeDb(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Db entry = new Db();
            entry.setId(Integer.valueOf(data[0]));
            if (!data[1].equals("\\N")) entry.setName(data[1]);
            if (!data[2].equals("\\N")) entry.setFullname(data[2]);
            if (!data[3].equals("\\N")) entry.setDatatype(data[3]);
            if (!data[4].equals("\\N")) entry.setGenericUrl(data[4]);
            if (!data[5].equals("\\N")) entry.setUrlSyntax(data[5]);
            if (!data[6].equals("\\N")) entry.setUrlExample(data[6]);
            if (!data[7].equals("\\N")) entry.setUriPrefix(data[7]);
            insertObject(entry);
        }
        reader.close();
    }
