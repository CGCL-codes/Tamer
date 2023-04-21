    private void writeGeneProduct(BufferedReader reader) throws IOException {
        String line;
        boolean valid = true;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            if (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1).concat(" ");
                line = line.concat(reader.readLine());
            }
            String data[] = line.split(SEPARATOR_TAB);
            GeneProduct entry = new GeneProduct();
            if (data.length == 2) {
                line = line.replace('\\', ' ');
                String next_line = reader.readLine();
                line = line.concat(next_line);
                data = line.split("\\t");
            } else if (data.length > 2) {
                if (data[2].matches("\\D{1,}\\d*")) {
                    line = line.replace('\\', ' ');
                    ArrayList<String> tmp_data = new ArrayList<String>(data.length - 1);
                    for (String content : data) tmp_data.add(content);
                    tmp_data.set(1, data[1].concat(data[2]));
                    tmp_data.remove(2);
                    data = tmp_data.toArray(new String[tmp_data.size()]);
                }
            }
            try {
                entry.setId(Integer.valueOf(data[0]));
                entry.setSymbol(data[1]);
                entry.setDbxref((Dbxref) session.get(Dbxref.class, Integer.valueOf(data[2])));
                entry.setSpecies((Species) session.get(Species.class, Integer.valueOf(data[3])));
                entry.setTerm((Term) session.get(Term.class, Integer.valueOf(data[4])));
                if (data.length > 5) entry.setFullName(data[5]);
                valid = true;
            } catch (NumberFormatException e) {
                Log.writeWarningLog(this.getClass(), e.getMessage(), e);
                valid = false;
            }
            if (valid) {
                insertObject(entry);
            }
        }
        reader.close();
    }
