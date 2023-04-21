    public void writeTNTFile(File f, SequenceGrid grid, DelayCallback delay) throws IOException, DelayAbortedException {
        StringBuffer buff_title = new StringBuffer();
        Set cols = grid.getColumns();
        if (cols.size() >= 32) {
            delay.addWarning("TOO MANY CHARACTER SETS: According to the manual, TNT can only handle 32 character sets. You have " + cols.size() + " character sets. I will write out the remaining character sets into the file title, from where you can copy it into the correct position in the file as needed.");
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
            for (int x = 0; x < grid.getColumnLength(colName); x++) {
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
        writer.println("xread\n'Exported by TaxonDNA " + Versions.getTaxonDNA() + " on " + new Date() + ".");
        if (buff_title.length() > 0) {
            writer.println("Additional taxonsets and character sets will be placed below this line.");
            writer.println(buff_title.toString());
            writer.println("Additional taxonsets and character sets end here.");
        }
        writer.println("'");
        writer.println(grid.getCompleteSequenceLength() + " " + grid.getSequencesCount());
        Iterator i_rows = grid.getSequences().iterator();
        int count_rows = 0;
        while (i_rows.hasNext()) {
            if (delay != null) delay.delay(count_rows, grid.getSequencesCount());
            count_rows++;
            String seqName = (String) i_rows.next();
            Sequence seq_interleaved = null;
            int length = 0;
            writer.print(getTNTName(seqName, MAX_TAXON_LENGTH) + " ");
            Iterator i_cols = cols.iterator();
            while (i_cols.hasNext()) {
                String colName = (String) i_cols.next();
                Sequence seq = grid.getSequence(colName, seqName);
                if (seq == null) seq = Sequence.makeEmptySequence(colName, grid.getColumnLength(colName));
                length += seq.getLength();
                writer.print(seq.getSequence());
            }
            writer.println();
        }
        writer.println(";\n");
        writer.println(buff_sets);
        writer.flush();
        writer.close();
        if (delay != null) delay.end();
    }
