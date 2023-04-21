    public void exportSequencesByColumn(File dir, FormatHandler fh, boolean writeNASequences, DelayCallback delay) throws IOException, DelayAbortedException {
        TableManager tm = matrix.getTableManager();
        if (delay != null) delay.begin();
        Vector vec_sequences = new Vector((Collection) tm.getSequenceNames());
        int count_columns = tm.getCharsets().size();
        Iterator i = tm.getCharsets().iterator();
        int count = 0;
        while (i.hasNext()) {
            if (delay != null) delay.delay(count, count_columns);
            count++;
            String colName = (String) i.next();
            int colLength = tm.getColumnLength(colName);
            SequenceList sl = new SequenceList();
            Iterator i2 = vec_sequences.iterator();
            while (i2.hasNext()) {
                String seqName = (String) i2.next();
                Sequence seq = tm.getSequence(colName, seqName);
                if (seq == null) {
                    if (writeNASequences) {
                        sl.add(Sequence.makeEmptySequence(seqName, colLength));
                    } else {
                    }
                } else {
                    seq = new Sequence(seq);
                    seq.changeName(seqName);
                    sl.add(seq);
                }
            }
            File writeTo = new File(dir, makeFileName(colName) + "." + fh.getExtension());
            if (writeTo.exists()) {
                if (delay != null) delay.end();
                throw new IOException("Can't create file '" + writeTo + "' - it already exists!");
            }
            if (writeTo.exists() && !writeTo.canWrite()) {
                if (delay != null) delay.end();
                throw new IOException("Couldn't open '" + writeTo + "' for writing. Are you sure you have permissions to write into this directory?");
            }
            try {
                fh.writeFile(writeTo, sl, null);
            } catch (IOException e) {
                if (delay != null) delay.end();
                throw e;
            }
        }
        if (delay != null) delay.end();
    }
