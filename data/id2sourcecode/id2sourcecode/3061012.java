    protected void cleanup(Context context) throws IOException, InterruptedException {
        try {
            int numread = smap.load_qual_reads(max_mismatches, readperround, reads, quals);
            if (numread > 0) {
                for (int j = 0; j < smap.the_seeds.size(); ++j) {
                    context.progress();
                    smap.iterate_over_seeds(refgenome, j, smap.fast_reads_q, smap.max_match_score);
                }
                smap.eliminate_ambigs(smap.max_match_score, smap.fast_reads_q);
                Vector<SAMRecordWritable> results = new Vector<SAMRecordWritable>();
                smap.generateSAMResults(CloudAligner.INPUT_FORMAT.FASTQ_FILE, results);
                for (int i = 0; i < results.size(); i++) context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i))), results.elementAt(i));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
