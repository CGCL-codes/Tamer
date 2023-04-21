    public void readsDetailReportForNOMeSeq(BisulfiteVariantCallContext value, boolean posStrand, boolean multiThread, String sampleContext) {
        if (value.rawContext.hasReads()) {
            String ref = "";
            if (posStrand) {
                ref = ref + BaseUtilsMore.convertByteToString(value.ref.getBase());
            } else {
                ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.iupacCodeComplement(value.ref.getBase()));
            }
            GenomeLoc locPre = value.ref.getGenomeLocParser().createGenomeLoc(value.ref.getLocus().getContig(), value.ref.getLocus().getStart() - 1);
            GenomeLoc locPost = value.ref.getGenomeLocParser().createGenomeLoc(value.ref.getLocus().getContig(), value.ref.getLocus().getStart() + 1);
            if (value.ref.getWindow().containsP(locPre)) {
                ReferenceContext tmpRef = new ReferenceContext(value.ref.getGenomeLocParser(), locPre, value.ref.getWindow(), value.ref.getBases());
                if (posStrand) {
                    ref = BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(tmpRef.getBase(), 1)) + ref;
                } else {
                    ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(BaseUtilsMore.iupacCodeComplement(tmpRef.getBase()), 3));
                }
            }
            if (value.ref.getWindow().containsP(locPost)) {
                ReferenceContext tmpRef = new ReferenceContext(value.ref.getGenomeLocParser(), locPost, value.ref.getWindow(), value.ref.getBases());
                if (posStrand) {
                    ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(tmpRef.getBase(), 3));
                } else {
                    ref = BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(BaseUtilsMore.iupacCodeComplement(tmpRef.getBase()), 1)) + ref;
                }
            }
            for (PileupElement p : value.rawContext.getBasePileup()) {
                GATKSAMRecordFilterStorage GATKrecordFilterStor = new GATKSAMRecordFilterStorage((GATKSAMRecord) p.getRead(), BAC, p.getOffset());
                if (!GATKrecordFilterStor.isGoodBase()) {
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
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                ((NOMeSeqReadsWriterImp) readsWriter).add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.A) {
                            methyStatus = 'u';
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
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
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.T) {
                            methyStatus = 'u';
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
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
