    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("TextDumper <dbDir> <cacheSize> <outputFile>");
            System.exit(0);
        }
        String dbDir = args[0];
        int cacheSize = Integer.parseInt(args[1]);
        String outputFile = args[2];
        MismatchFactory factory = new MismatchFactory();
        MismatchStore store = null;
        try {
            SeqWareSettings settings = new SeqWareSettings();
            settings.setStoreType("berkeleydb-mismatch-store");
            settings.setFilePath(dbDir);
            settings.setCacheSize(cacheSize);
            settings.setCreateMismatchDB(false);
            settings.setCreateConsequenceAnnotationDB(false);
            settings.setCreateDbSNPAnnotationDB(false);
            settings.setReadOnly(true);
            store = factory.getStore(settings);
            if (store != null) {
                BufferedWriter outputStream = null;
                try {
                    outputStream = new BufferedWriter(new FileWriter(outputFile));
                    SortedCursorIterator cursor = store.getMismatches();
                    int count = 0;
                    outputStream.write("mismatch_id\tref_qual_call\tcons_qual_call\tmax_mapping_qual\tref_max_seq_qual\tref_ave_seq_qual\tcons_max_seq_qual\tcons_ave_seq_qual" + "\tzygosity\tbase_count_forward\tbase_count_reverse\tbase_count\tcontig\tstart\tstop\tref_base\tcons_base\tcalled_base\tread_count" + "\ttype\tread_bases\tbase_qual\tcall_one\tcall_two\treads_support_call_one\treads_support_call_two\treads_support_call_three\n");
                    while (cursor.hasNext()) {
                        count++;
                        if (count % 1000 == 0) {
                            System.out.print(count + "\r");
                        }
                        Mismatch m = (Mismatch) cursor.next();
                        outputStream.write("Mismatch: " + m.getId() + "\t" + m.getReferenceCallQuality() + "\t" + m.getConsensusCallQuality() + "\t" + m.getMaximumMappingQuality() + "\t" + m.getReferenceMaxSeqQuality() + "\t" + m.getReferenceAveSeqQuality() + "\t" + m.getConsensusMaxSeqQuality() + "\t" + m.getConsensusAveSeqQuality() + "\t" + m.getZygosity() + "\t" + m.getCalledBaseCountForward() + "\t" + m.getCalledBaseCountReverse() + "\t" + m.getCalledBaseCount() + "\t" + m.getContig() + "\t" + m.getStartPosition() + "\t" + m.getStopPosition() + "\t" + m.getReferenceBase() + "\t" + m.getConsensusBase() + "\t" + m.getCalledBase() + "\t" + m.getReadCount() + "\t" + m.getType() + "\t" + m.getReadBases() + "\t" + m.getBaseQualities() + "\t" + m.getCallOne() + "\t" + m.getCallTwo() + "\t" + m.getReadsSupportingCallOne() + "\t" + m.getReadsSupportingCallTwo() + "\t" + m.getReadsSupportingCallThree() + "\n");
                        HashMap<String, String> tags = m.getTags();
                        Iterator it = tags.keySet().iterator();
                        outputStream.write("\ttags:\n");
                        while (it.hasNext()) {
                            String tag = (String) it.next();
                            outputStream.write("\t\t" + tag);
                            String value = tags.get(tag);
                            if (value != null) {
                                outputStream.write(":" + value);
                            }
                            outputStream.write("\n");
                        }
                    }
                    cursor.close();
                    System.out.print("\n");
                    ModelAnnotationTagCursorIterator cci = store.getConsequencesByTag("early-termination");
                    outputStream.write("\nTest Consequence Retrieval via Tag: early-termination\n");
                    while (cci.hasNext()) {
                        Consequence c = (Consequence) cci.next();
                        outputStream.write("Consequence: " + c.getId() + "\n");
                        HashMap<String, String> tags = c.getTags();
                        Iterator it = tags.keySet().iterator();
                        outputStream.write("\ttags:\n");
                        while (it.hasNext()) {
                            String tag = (String) it.next();
                            outputStream.write("\t\t" + tag);
                            String value = tags.get(tag);
                            if (value != null) {
                                outputStream.write(":" + value);
                            }
                            outputStream.write("\n");
                        }
                    }
                    cci.close();
                    System.out.print("\n");
                    outputStream.write("\nTest coverage information\n");
                    ContigCursorIterator coverageIt = store.getCoverages("chr22", 14432000, 14439999);
                    while (coverageIt.hasNext()) {
                        Coverage c = (Coverage) coverageIt.next();
                        if (c == null) {
                            outputStream.write("C is null");
                        } else {
                            outputStream.write("Coverage: " + c.getCount() + " start: " + c.getStartPosition() + " stop: " + c.getStopPosition() + "\n");
                            HashMap<Integer, Integer> coverages = c.getCoverage();
                            Iterator<Integer> it = coverages.keySet().iterator();
                            outputStream.write("\t");
                            while (it.hasNext()) {
                                Integer cov = it.next();
                                outputStream.write(cov + ":" + coverages.get(cov) + ",");
                            }
                            outputStream.write("\n");
                        }
                    }
                    coverageIt.close();
                    System.out.print("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
                store.close();
            }
        } catch (SeqWareException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
