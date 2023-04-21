    private void writeDbxref(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Dbxref entry = new Dbxref();
            entry.setId(Integer.valueOf(data[0]));
            if (!data[1].equals("\\N")) entry.setXrefDbname(data[1]);
            if (!data[2].equals("\\N")) {
                if (data[1].equalsIgnoreCase("ec")) {
                    if (data[2].startsWith("EC")) data[2] = data[2].replace("EC", "");
                    if (data[2].startsWith(":")) data[2] = data[2].replace(":", "");
                }
                entry.setXrefKey(data[2]);
            }
            if (!data[3].equals("\\N")) entry.setXrefKeytype(data[3]);
            insertObject(entry);
        }
        reader.close();
    }
