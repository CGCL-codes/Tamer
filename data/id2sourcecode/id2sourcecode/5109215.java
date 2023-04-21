    public void map(IntWritable key, FastaRecordWritable value, Context context) throws IOException, InterruptedException {
        byte[] seq = new byte[value.m_sequence.getLength()];
        System.arraycopy(value.m_sequence.getBytes(), 0, seq, 0, value.m_sequence.getLength());
        int chrom_id = value.m_offset.get();
        try {
            Path thePath = new Path(filename);
            FileSystem fs = FileSystem.get(URI.create(filename), conf);
            SequenceFile.Reader theReader = null;
            if (fs.exists(thePath)) {
                theReader = new SequenceFile.Reader(fs, thePath, conf);
                assert (theReader != null);
                if (key.get() % 2 == 0) {
                    Text pseudokey = (Text) (theReader.getKeyClass().newInstance());
                    theReader.sync(theReader.getPosition() + 10);
                    if (!theReader.next(pseudokey)) {
                        IOUtils.closeStream(theReader);
                        return;
                    }
                }
                if (mapperMapmode == CloudAligner.MAP_MODE.BISULFITE) System.out.println("CloudMapper: In Bisulfite map mode");
                Aligner smap = new Aligner(mapperInputmode, CloudAligner.RUN_MODE.RUN_MODE_MISMATCH, mapperMapmode, true);
                smap.initialize(read_width, max_mismatches, seed_num, seed_weight);
                boolean stop = false;
                int numread = 0;
                byte[] pseudo = { -1 };
                if (mapperMapmode == CloudAligner.MAP_MODE.PAIR_END) {
                    do {
                        numread = smap.load_pe_reads(max_mismatches, readperround, theReader);
                        if (numread < readperround) stop = true;
                        if (numread > 0) {
                            smap.execute(seq, chrom_id);
                            Vector<MultiMapResultPE> bests = smap.best_mapsPE;
                            double score = max_mismatches;
                            for (int i = 0; i < bests.size(); ++i) if (!bests.elementAt(i).isEmpty()) {
                                bests.elementAt(i).sort();
                                for (int j = 0; j < bests.elementAt(i).mr.size(); ++j) if (j == 0 || bests.elementAt(i).mr.elementAt(j - 1).isSmaller(bests.elementAt(i).mr.elementAt(j))) {
                                    final int left_start = bests.elementAt(i).mr.elementAt(j).strand ? bests.elementAt(i).mr.elementAt(j).site : seq.length - bests.elementAt(i).mr.elementAt(j).site2 - Aligner.read_width;
                                    final int right_start = bests.elementAt(i).mr.elementAt(j).strand ? bests.elementAt(i).mr.elementAt(j).site2 : seq.length - bests.elementAt(i).mr.elementAt(j).site - Aligner.read_width;
                                    score = (mapperInputmode == CloudAligner.INPUT_MODE.FASTQ_FILE) ? FastReadQuality.value_to_quality(bests.elementAt(i).score) : bests.elementAt(i).score;
                                    if (!smap.read_qualities.isEmpty()) {
                                        context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i)) + "_L"), new ReadInfoWritable(smap.original_reads.elementAt(smap.read_index.elementAt(i)), smap.read_qualities.elementAt(smap.read_index.elementAt(i)), score, chrom_id, left_start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                        context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i)) + "_R"), new ReadInfoWritable(smap.original_reads.elementAt(smap.read_index.elementAt(i)), smap.read_qualities.elementAt(smap.read_index.elementAt(i)), score, chrom_id, right_start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                    } else {
                                        context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i)) + "_L"), new ReadInfoWritable(smap.original_reads.elementAt(smap.read_index.elementAt(i)), pseudo, score, chrom_id, left_start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                        context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i)) + "_R"), new ReadInfoWritable(smap.original_reads.elementAt(smap.read_index.elementAt(i)), pseudo, score, chrom_id, right_start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                    }
                                }
                            }
                        }
                    } while (stop == false);
                } else {
                    do {
                        numread = smap.load_reads(max_mismatches, readperround, theReader);
                        if (numread < readperround) stop = true;
                        if (numread > 0) {
                            smap.execute(seq, chrom_id);
                            Vector<MultiMapResult> bests = smap.best_maps;
                            double score = max_mismatches;
                            for (int i = 0; i < bests.size(); ++i) if (!bests.elementAt(i).isEmpty()) {
                                bests.elementAt(i).sort();
                                for (int j = 0; j < bests.elementAt(i).mr.size(); ++j) if (j == 0 || bests.elementAt(i).mr.elementAt(j - 1).isSmaller(bests.elementAt(i).mr.elementAt(j))) {
                                    final int start = bests.elementAt(i).mr.elementAt(j).strand ? bests.elementAt(i).mr.elementAt(j).site : seq.length - bests.elementAt(i).mr.elementAt(j).site - Aligner.read_width;
                                    score = (mapperInputmode == CloudAligner.INPUT_MODE.FASTQ_FILE) ? FastReadQuality.value_to_quality(bests.elementAt(i).score) : bests.elementAt(i).score;
                                    if (!smap.read_qualities.isEmpty()) {
                                        context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i))), new ReadInfoWritable(smap.original_reads.elementAt(smap.read_index.elementAt(i)), smap.read_qualities.elementAt(smap.read_index.elementAt(i)), score, chrom_id, start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                    } else {
                                        context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i))), new ReadInfoWritable(smap.original_reads.elementAt(smap.read_index.elementAt(i)), pseudo, score, chrom_id, start, bests.elementAt(i).mr.elementAt(j).strand, !bests.elementAt(i).ambiguous()));
                                    }
                                }
                            }
                        }
                    } while (stop == false);
                }
                IOUtils.closeStream(theReader);
            } else System.err.print("Read file not found!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
