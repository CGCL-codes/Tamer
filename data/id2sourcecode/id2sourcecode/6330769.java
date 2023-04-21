        public void map(IntWritable key, FastaRecordWritable value, Context context) throws IOException, InterruptedException {
            byte[] seq = new byte[value.m_sequence.getLength()];
            System.arraycopy(value.m_sequence.getBytes(), 0, seq, 0, value.m_sequence.getLength());
            int chrom_id = value.m_offset.get();
            try {
                Path thePath = new Path(filename);
                FileSystem fs = FileSystem.get(conf);
                SequenceFile.Reader theReader = null;
                System.out.println("key = " + key.toString());
                if (fs.exists(thePath)) {
                    theReader = new SequenceFile.Reader(fs, thePath, conf);
                    assert (theReader != null);
                    if (key.get() == 1) {
                        Text pseudokey = (Text) (theReader.getKeyClass().newInstance());
                        theReader.sync(theReader.getPosition() + 10);
                        if (!theReader.next(pseudokey)) {
                            IOUtils.closeStream(theReader);
                            return;
                        }
                    }
                    Smap smap = new Smap();
                    smap.initialize(read_width, max_mismatches, seed_num, seed_weight);
                    boolean stop = false;
                    int numread = 0;
                    do {
                        numread = smap.load_reads(max_mismatches, READS_PER_ROUND, theReader);
                        if (numread < READS_PER_ROUND) stop = true;
                        if (numread > 0) {
                            smap.execute(seq, chrom_id);
                            Vector<MultiMapResult> bests = smap.best_maps;
                            int score = max_mismatches;
                            for (int i = 0; i < bests.size(); ++i) if (!bests.elementAt(i).isEmpty()) {
                                bests.elementAt(i).sort();
                                for (int j = 0; j < bests.elementAt(i).mr.size(); ++j) if (j == 0 || bests.elementAt(i).mr.elementAt(j - 1).isSmaller(bests.elementAt(i).mr.elementAt(j))) {
                                    final int start = bests.elementAt(i).mr.elementAt(j).strand ? bests.elementAt(i).mr.elementAt(j).site : seq.length - bests.elementAt(i).mr.elementAt(j).site - Smap.read_width;
                                    score = bests.elementAt(i).score;
                                    context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i))), new ReadInfoWritable(smap.read_names.elementAt(smap.read_index.elementAt(i)), score, chrom_id, start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                }
                            }
                        }
                    } while (stop == false);
                    IOUtils.closeStream(theReader);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
