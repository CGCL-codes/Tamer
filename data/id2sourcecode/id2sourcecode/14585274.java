    public void writeTNTFile(File file, SequenceList set, int interleaveAt, String otherBlocks, DelayCallback delay) throws IOException, DelayAbortedException {
        boolean interleaved = false;
        if (interleaveAt > 0 && interleaveAt < set.getMaxLength()) interleaved = true;
        set.lock();
        if (delay != null) delay.begin();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.println("nstates 32;");
        writer.println("xread");
        writer.println("'Written by TaxonDNA " + Versions.getTaxonDNA() + " on " + new Date() + "'");
        writer.println(set.getMaxLength() + " " + set.count());
        writer.println("");
        Hashtable names = new Hashtable();
        Vector vec_names = new Vector();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Sequence seq = (Sequence) i.next();
            String name = getTNTName(seq.getFullName(MAX_TAXON_LENGTH), MAX_TAXON_LENGTH);
            name = name.replaceAll("\'", "\'\'");
            name = name.replace(' ', '_');
            int no = 2;
            while (names.get(name) != null) {
                int digits = 5;
                if (no > 0 && no < 10) digits = 1;
                if (no >= 10 && no < 100) digits = 2;
                if (no >= 100 && no < 1000) digits = 3;
                if (no >= 1000 && no < 10000) digits = 4;
                name = getTNTName(seq.getFullName(MAX_TAXON_LENGTH - digits - 1), MAX_TAXON_LENGTH - digits - 1);
                name = name.replaceAll("\'", "\'\'");
                name = name.replace(' ', '_');
                name += "_" + no;
                no++;
                if (no == 10000) {
                    throw new IOException("There are 9999 sequences named '" + seq.getFullName(MAX_TAXON_LENGTH) + "', which is the most I can handle. Sorry. This is an arbitary limit: please let us know if you think we set it too low.");
                }
            }
            names.put(name, seq);
            vec_names.add(name);
        }
        if (!interleaved) {
            Iterator i_names = vec_names.iterator();
            int x = 0;
            while (i_names.hasNext()) {
                if (delay != null) {
                    try {
                        delay.delay(x, vec_names.size());
                    } catch (DelayAbortedException e) {
                        writer.close();
                        set.unlock();
                        throw e;
                    }
                }
                String name = (String) i_names.next();
                Sequence seq = (Sequence) names.get(name);
                writer.println(pad_string(name, MAX_TAXON_LENGTH) + " " + seq.getSequence());
                x++;
            }
        } else {
            for (int x = 0; x < set.getMaxLength(); x += interleaveAt) {
                Iterator i_names = vec_names.iterator();
                if (delay != null) try {
                    delay.delay(x, set.getMaxLength());
                } catch (DelayAbortedException e) {
                    writer.close();
                    set.unlock();
                    throw e;
                }
                writer.println("&");
                while (i_names.hasNext()) {
                    String name = (String) i_names.next();
                    Sequence seq = (Sequence) names.get(name);
                    Sequence subseq = null;
                    int until = 0;
                    try {
                        until = x + interleaveAt;
                        if (until > seq.getLength()) {
                            until = seq.getLength();
                        }
                        subseq = seq.getSubsequence(x + 1, until);
                    } catch (SequenceException e) {
                        delay.end();
                        throw new IOException("Could not get subsequence (" + (x + 1) + ", " + until + ") from sequence " + seq + ". This is most likely a programming error.");
                    }
                    if (subseq.getSequence().indexOf('Z') != -1 || subseq.getSequence().indexOf('z') != -1) delay.addWarning("Sequence '" + subseq.getFullName() + "' contains the letter 'Z'. This letter might not work in TNT.");
                    writer.println(pad_string(name, MAX_TAXON_LENGTH) + " " + subseq.getSequence());
                }
            }
        }
        writer.println(";");
        if (otherBlocks != null) writer.println(otherBlocks);
        writer.close();
        if (delay != null) delay.end();
        set.unlock();
    }
