    private void writeGeneProductSynonym(BufferedReader reader) throws IOException {
        String line;
        GeneProductSynonym before = new GeneProductSynonym();
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Integer id = 0;
            try {
                if (data.length == 2) {
                    id = Integer.valueOf(data[0]);
                    GeneProductSynonym entry = new GeneProductSynonym(new GeneProductSynonymId(id, data[1]), (GeneProduct) session.get(GeneProduct.class, id));
                    try {
                        insertObject(entry);
                    } catch (ConstraintViolationException e) {
                        Log.writeWarningLog(this.getClass(), e.getMessage(), e);
                    }
                    before = entry;
                }
            } catch (NumberFormatException e) {
                GeneProductSynonymId entry_id = before.getId();
                GeneProductSynonym entry = before;
                entry_id.setProductSynonym(entry_id.getProductSynonym().replace("\\", "").concat(line));
                entry.setId(entry_id);
                updateObject(entry);
            }
        }
        reader.close();
    }
