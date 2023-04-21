    public void readsDetailReportForNOMeSeq(AlignmentContext rawContext, boolean posStrand, boolean multiThread, String sampleContext, ReferenceContext refContext) {
        if (rawContext.hasReads()) {
            String ref = "";
            if (posStrand) {
                ref = ref + BaseUtilsMore.convertByteToString(refContext.getBase());
            } else {
                ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.iupacCodeComplement(refContext.getBase()));
            }
            GenomeLoc locPre = refContext.getGenomeLocParser().createGenomeLoc(refContext.getLocus().getContig(), refContext.getLocus().getStart() - 1);
            GenomeLoc locPost = refContext.getGenomeLocParser().createGenomeLoc(refContext.getLocus().getContig(), refContext.getLocus().getStart() + 1);
            if (refContext.getWindow().containsP(locPre)) {
                ReferenceContext tmpRef = new ReferenceContext(refContext.getGenomeLocParser(), locPre, refContext.getWindow(), refContext.getBases());
                if (posStrand) {
                    ref = BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(tmpRef.getBase(), 1)) + ref;
                } else {
                    ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(BaseUtilsMore.iupacCodeComplement(tmpRef.getBase()), 3));
                }
            }
            if (refContext.getWindow().containsP(locPost)) {
                ReferenceContext tmpRef = new ReferenceContext(refContext.getGenomeLocParser(), locPost, refContext.getWindow(), refContext.getBases());
                if (posStrand) {
                    ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(tmpRef.getBase(), 3));
                } else {
                    ref = BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(BaseUtilsMore.iupacCodeComplement(tmpRef.getBase()), 1)) + ref;
                }
            }
            for (PileupElement p : rawContext.getBasePileup()) {
                GATKSAMRecordFilterStorage GATKrecordFilterStor = new GATKSAMRecordFilterStorage(p.getRead(), refContext, BAC);
                if (!GATKrecordFilterStor.isGoodBase(p.getOffset())) {
                    continue;
                }
                char strand;
                COUNT_CACHE_FOR_OUTPUT_READS++;
                if (COUNT_CACHE_FOR_OUTPUT_READS % MAXIMUM_CACHE_FOR_OUTPUT_READS == 0) {
                    if (multiThread) {
                        multiThreadCpgReadsWriter.writerFlush();
                    } else {
                        readsWriter.writerFlush();
                    }
                    System.gc();
                }
                if (p.getRead().getReadNegativeStrandFlag()) {
                    strand = '-';
                    if (!posStrand) {
                        char methyStatus;
                        if (p.getBase() == BaseUtilsMore.G) {
                            methyStatus = 'm';
                            NOMeSeqReads cr = new NOMeSeqReads(rawContext.getContig(), rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                ((NOMeSeqReadsWriterImp) readsWriter).add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.A) {
                            methyStatus = 'u';
                            NOMeSeqReads cr = new NOMeSeqReads(rawContext.getContig(), rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        }
                    }
                } else {
                    strand = '+';
                    if (posStrand) {
                        char methyStatus;
                        if (p.getBase() == BaseUtilsMore.C) {
                            methyStatus = 'm';
                            NOMeSeqReads cr = new NOMeSeqReads(rawContext.getContig(), rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.T) {
                            methyStatus = 'u';
                            NOMeSeqReads cr = new NOMeSeqReads(rawContext.getContig(), rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        }
                    }
                }
            }
        }
    }
