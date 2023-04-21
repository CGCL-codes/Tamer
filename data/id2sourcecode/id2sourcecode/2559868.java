    public void exportAsTNT(File f, DelayCallback delay) throws IOException, DelayAbortedException {
        TableManager tm = matrix.getTableManager();
        StringBuffer buff_title = new StringBuffer();
        Taxonsets tx = matrix.getTaxonsets();
        StringBuffer buff_taxonsets = new StringBuffer();
        if (tx.getTaxonsetList() != null) {
            if (tx.getTaxonsetList().size() >= 32) {
                new MessageBox(matrix.getFrame(), "Too many taxonsets!", "According to the manual, TNT can only handle 32 taxonsets. You have " + tx.getTaxonsetList().size() + " taxonsets. I will write the remaining taxonsets into the file title, from where you can copy it into the correct position in the file as needed.").go();
            }
            buff_taxonsets.append("agroup\n");
            Vector v = tx.getTaxonsetList();
            Iterator i = v.iterator();
            int x = 0;
            while (i.hasNext()) {
                String taxonsetName = (String) i.next();
                String str = getTaxonset(taxonsetName, 0);
                if (str != null) {
                    if (x == 31) buff_title.append("@agroup\n");
                    if (x <= 31) buff_taxonsets.append("=" + x + " (" + taxonsetName + ") " + str + "\n"); else buff_title.append("=" + x + " (" + taxonsetName + ") " + str + "\n");
                    x++;
                }
            }
            buff_taxonsets.append(";\n\n\n");
            if (x >= 32) buff_title.append(";\n\n");
        }
        List cols = tm.getCharsets();
        if (cols.size() >= 32) {
            new MessageBox(matrix.getFrame(), "Too many character sets!", "According to the manual, TNT can only handle 32 character sets. You have " + cols.size() + " character sets. I will write out the remaining character sets into the file title, from where you can copy it into the correct position in the file as needed.").go();
        }
        StringBuffer buff_sets = new StringBuffer();
        buff_sets.append("xgroup\n");
        Iterator i = cols.iterator();
        int at = 0;
        int colid = 0;
        while (i.hasNext()) {
            String colName = (String) i.next();
            if (colid == 32) buff_title.append("@xgroup\n");
            if (colid <= 31) buff_sets.append("=" + colid + " (" + fixColumnName(colName) + ")\t"); else buff_title.append("=" + colid + " (" + fixColumnName(colName) + ")\t");
            for (int x = 0; x < tm.getColumnLength(colName); x++) {
                if (colid <= 31) buff_sets.append(at + " "); else buff_title.append(at + " ");
                at++;
            }
            if (colid <= 31) buff_sets.append("\n"); else buff_title.append("\n");
            colid++;
        }
        buff_sets.append("\n;\n\n");
        if (colid > 31) buff_title.append("\n;");
        if (delay != null) delay.begin();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        writer.println("nstates dna;");
        writer.println("xread\n'Exported by " + matrix.getName() + " on " + new Date() + ".");
        if (buff_title.length() > 0) {
            writer.println("Additional taxonsets and character sets will be placed below this line.");
            writer.println(buff_title.toString());
            writer.println("Additional taxonsets and character sets end here.");
        }
        writer.println("'");
        writer.println(tm.getSequenceLength() + " " + tm.getSequencesCount());
        Iterator i_rows = tm.getSequenceNames().iterator();
        int count_rows = 0;
        while (i_rows.hasNext()) {
            if (delay != null) delay.delay(count_rows, tm.getSequencesCount());
            count_rows++;
            String seqName = (String) i_rows.next();
            Sequence seq_interleaved = null;
            int length = 0;
            writer.print(getNexusName(seqName) + " ");
            Iterator i_cols = cols.iterator();
            while (i_cols.hasNext()) {
                String colName = (String) i_cols.next();
                Sequence seq = tm.getSequence(colName, seqName);
                if (seq == null) seq = Sequence.makeEmptySequence(colName, tm.getColumnLength(colName));
                length += seq.getLength();
                writer.print(seq.getSequence());
            }
            writer.println();
        }
        writer.println(";\n");
        writer.println(buff_sets);
        writer.println(buff_taxonsets);
        writer.flush();
        writer.close();
        if (delay != null) delay.end();
    }
