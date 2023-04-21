    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("TextDumper <dbDir> <cacheSize> <outputFile>");
            System.exit(0);
        }
        String dbDir = args[0];
        long cacheSize = Long.parseLong(args[1]);
        String outputFile = args[2];
        BerkeleyDBFactory factory = new BerkeleyDBFactory();
        BerkeleyDBStore store = null;
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
                    CursorIterator cursor = store.getMismatches();
                    int count = 0;
                    outputStream.write("mismatch_id\tref_qual_call\tcons_qual_call\tmax_mapping_qual\tref_max_seq_qual\tref_ave_seq_qual\tcons_max_seq_qual\tcons_ave_seq_qual" + "\tzygosity\tbase_count_forward\tbase_count_reverse\tbase_count\tcontig\tstart\tstop\tref_base\tcons_base\tcalled_base\tread_count" + "\ttype\tread_bases\tbase_qual\tcall_one\tcall_two\treads_support_call_one\treads_support_call_two\treads_support_call_three\n");
                    while (cursor.hasNext()) {
                        count++;
                        if (count % 1000 == 0) {
                            System.out.print(count + "\r");
                        }
                        Variant m = (Variant) cursor.next();
                        outputStream.write("Variant: " + m.getId() + "\t" + m.getReferenceCallQuality() + "\t" + m.getConsensusCallQuality() + "\t" + m.getMaximumMappingQuality() + "\t" + m.getReferenceMaxSeqQuality() + "\t" + m.getReferenceAveSeqQuality() + "\t" + m.getConsensusMaxSeqQuality() + "\t" + m.getConsensusAveSeqQuality() + "\t" + m.getZygosity() + "\t" + m.getCalledBaseCountForward() + "\t" + m.getCalledBaseCountReverse() + "\t" + m.getCalledBaseCount() + "\t" + m.getContig() + "\t" + m.getStartPosition() + "\t" + m.getStopPosition() + "\t" + m.getReferenceBase() + "\t" + m.getConsensusBase() + "\t" + m.getCalledBase() + "\t" + m.getReadCount() + "\t" + m.getType() + "\t" + m.getReadBases() + "\t" + m.getBaseQualities() + "\t" + m.getCallOne() + "\t" + m.getCallTwo() + "\t" + m.getReadsSupportingCallOne() + "\t" + m.getReadsSupportingCallTwo() + "\t" + m.getReadsSupportingCallThree() + "\n");
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
