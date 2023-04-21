    public ContextCondition reduce(BisulfiteVariantCallContext value, ContextCondition sum) {
        sum.nBasesVisited++;
        if (value == null) return sum;
        System.err.println(value.vc.getGenotypes().size() + "\t" + value.vc.getGenotypes().getSampleNames());
        sum.nBasesCallable++;
        sum.nBasesCalledConfidently += value.confidentlyCalled ? 1 : 0;
        CytosineTypeStatus ctsFirst = value.cts.get(value.cts.keySet().toArray()[0]);
        sum.nCytosineBasesCalledConfidently += ctsFirst.isC ? 1 : 0;
        if (BAC.sequencingMode == MethylSNPModel.BM) {
            sum.nCpgBasesCalledConfidently += ctsFirst.isCpg ? 1 : 0;
            sum.nCphBasesCalledConfidently += ctsFirst.isCph ? 1 : 0;
            sum.sumMethyCpgBasesCalledConfidently += ctsFirst.isCpg ? ctsFirst.cpgMethyLevel : 0;
            sum.sumMethyCphBasesCalledConfidently += ctsFirst.isCph ? ctsFirst.cphMethyLevel : 0;
        } else if (BAC.sequencingMode == MethylSNPModel.GM) {
            sum.nGchBasesCalledConfidently += ctsFirst.isGch ? 1 : 0;
            sum.nCchBasesCalledConfidently += ctsFirst.isCch ? 1 : 0;
            sum.nWchBasesCalledConfidently += ctsFirst.isWch ? 1 : 0;
            sum.nGcgBasesCalledConfidently += ctsFirst.isGcg ? 1 : 0;
            sum.nCcgBasesCalledConfidently += ctsFirst.isCcg ? 1 : 0;
            sum.nWcgBasesCalledConfidently += ctsFirst.isWcg ? 1 : 0;
            sum.sumMethyGchBasesCalledConfidently += ctsFirst.isGch ? ctsFirst.gchMethyLevel : 0;
            sum.sumMethyCchBasesCalledConfidently += ctsFirst.isCch ? ctsFirst.cchMethyLevel : 0;
            sum.sumMethyWchBasesCalledConfidently += ctsFirst.isWch ? ctsFirst.wchMethyLevel : 0;
            sum.sumMethyGcgBasesCalledConfidently += ctsFirst.isGcg ? ctsFirst.gcgMethyLevel : 0;
            sum.sumMethyCcgBasesCalledConfidently += ctsFirst.isCcg ? ctsFirst.ccgMethyLevel : 0;
            sum.sumMethyWcgBasesCalledConfidently += ctsFirst.isWcg ? ctsFirst.wcgMethyLevel : 0;
        }
        sum.sumMethyCytosineBasesCalledConfidently += ctsFirst.isC ? ctsFirst.cytosineMethyLevel : 0;
        if (!BAC.forceOtherCytosine.isEmpty() || !BAC.autoEstimateOtherCytosine.isEmpty()) {
            for (String key : sum.otherCytosine.keySet()) {
                if (ctsFirst.cytosineListMap.containsKey(key)) {
                    Double[] cytosineStatus = ctsFirst.cytosineListMap.get(key);
                    Double[] values = sum.otherCytosine.get(key);
                    values[0] += Double.compare(cytosineStatus[3], 1.0) == 0 ? 1 : 0;
                    values[1] += Double.compare(cytosineStatus[3], 1.0) == 0 ? cytosineStatus[2] : 0;
                    sum.otherCytosine.put(key, values);
                }
            }
        }
        if (value.vc == null) return sum;
        try {
            sum.nCallsMade++;
            if (BAC.sequencingMode == MethylSNPModel.GM) {
                if (BAC.fnocrd != null && ((autoEstimateC && secondIteration) || (!autoEstimateC && !secondIteration))) {
                    boolean positiveStrand = true;
                    String sampleContext = "";
                    for (String key : ctsFirst.cytosineListMap.keySet()) {
                        if (key.equalsIgnoreCase("C-1")) {
                            continue;
                        }
                        Double[] cytosineStatus = ctsFirst.cytosineListMap.get(key);
                        if (Double.compare(cytosineStatus[3], 1.0) == 0) {
                            if (cytosineStatus[0] > cytosineStatus[1]) {
                                positiveStrand = true;
                            } else {
                                positiveStrand = false;
                            }
                            String[] tmpKey = key.split("-");
                            if (sampleContext.equals("")) {
                                sampleContext = tmpKey[0];
                            } else {
                                sampleContext = sampleContext + "," + tmpKey[0];
                            }
                        }
                    }
                    if (!sampleContext.equals("")) {
                        readsDetailReportForNOMeSeq(value.rawContext, positiveStrand, getToolkit().getArguments().numberOfThreads > 1, sampleContext, value.refBase);
                    }
                }
            } else {
                if (ctsFirst.isCpg) {
                    if (BAC.fnocrd != null && ((autoEstimateC && secondIteration) || (!autoEstimateC && !secondIteration))) {
                        if (ctsFirst.cytosineListMap.get("CG-1")[0] > ctsFirst.cytosineListMap.get("CG-1")[1]) {
                            readsDetailReport(value.rawContext, true, getToolkit().getArguments().numberOfThreads > 1, value.refBase);
                        } else {
                            readsDetailReport(value.rawContext, false, getToolkit().getArguments().numberOfThreads > 1, value.refBase);
                        }
                    }
                }
            }
            if (autoEstimateC) {
                if (secondIteration) {
                    if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_ALL_CYTOSINES) {
                        if (ctsFirst.isC) {
                            if (getToolkit().getArguments().numberOfThreads > 1) {
                                multiThreadWriter.add(value.vc, value.refBase.getBase());
                                COUNT_CACHE_FOR_OUTPUT_VCF++;
                                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                    multiThreadWriter.writerFlush();
                                    System.gc();
                                }
                            } else {
                                writer.add(value.vc);
                            }
                        }
                    } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_ALL_CPG) {
                        if (ctsFirst.isCpg) {
                            if (getToolkit().getArguments().numberOfThreads > 1) {
                                multiThreadWriter.add(value.vc, value.refBase.getBase());
                                COUNT_CACHE_FOR_OUTPUT_VCF++;
                                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                    multiThreadWriter.writerFlush();
                                    System.gc();
                                }
                            } else {
                                writer.add(value.vc);
                            }
                        }
                    } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_VARIANTS_ONLY) {
                        if (value.isVariant()) {
                            if (getToolkit().getArguments().numberOfThreads > 1) {
                                multiThreadWriter.add(value.vc, value.refBase.getBase());
                                COUNT_CACHE_FOR_OUTPUT_VCF++;
                                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                    multiThreadWriter.writerFlush();
                                    System.gc();
                                }
                            } else {
                                writer.add(value.vc);
                            }
                        }
                    } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_HET_SNPS_ONLY) {
                        if (value.isHetSnp()) {
                            if (getToolkit().getArguments().numberOfThreads > 1) {
                                multiThreadWriter.add(value.vc, value.refBase.getBase());
                                COUNT_CACHE_FOR_OUTPUT_VCF++;
                                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                    multiThreadWriter.writerFlush();
                                    System.gc();
                                }
                            } else {
                                writer.add(value.vc);
                            }
                        }
                    } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.DEFAULT_FOR_TCGA) {
                        if (ctsFirst.isCpg) {
                            if (getToolkit().getArguments().numberOfThreads > 1) {
                                multiThreadWriter.add(value.vc, value.refBase.getBase());
                                COUNT_CACHE_FOR_OUTPUT_VCF++;
                                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                    multiThreadWriter.writerFlush();
                                    System.gc();
                                }
                            } else {
                                writer.add(value.vc);
                            }
                        }
                        if (value.isVariant()) {
                            if (getToolkit().getArguments().numberOfThreads > 1) {
                                multiAdditionalWriterForDefaultTcgaMode.add(value.vc, value.refBase.getBase());
                                COUNT_CACHE_FOR_OUTPUT_VCF++;
                                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                    multiAdditionalWriterForDefaultTcgaMode.writerFlush();
                                    System.gc();
                                }
                            } else {
                                additionalWriterForDefaultTcgaMode.add(value.vc);
                            }
                        }
                    } else {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiThreadWriter.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiThreadWriter.writerFlush();
                                System.gc();
                            }
                            if (BAC.ovd) {
                                verboseWriter.add(value.vc);
                            }
                        } else {
                            writer.add(value.vc);
                        }
                    }
                }
            } else {
                if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_ALL_CYTOSINES) {
                    if (ctsFirst.isC) {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiThreadWriter.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiThreadWriter.writerFlush();
                                System.gc();
                            }
                        } else {
                            writer.add(value.vc);
                        }
                    }
                } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_ALL_CPG) {
                    if (ctsFirst.isCpg) {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiThreadWriter.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiThreadWriter.writerFlush();
                                System.gc();
                            }
                        } else {
                            writer.add(value.vc);
                        }
                    }
                } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_VARIANTS_ONLY) {
                    if (value.isVariant()) {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiThreadWriter.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiThreadWriter.writerFlush();
                                System.gc();
                            }
                        } else {
                            writer.add(value.vc);
                        }
                    }
                } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.EMIT_HET_SNPS_ONLY) {
                    if (value.isHetSnp()) {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiThreadWriter.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiThreadWriter.writerFlush();
                                System.gc();
                            }
                        } else {
                            writer.add(value.vc);
                        }
                    }
                } else if (BAC.OutputMode == BisulfiteGenotyperEngine.OUTPUT_MODE.DEFAULT_FOR_TCGA) {
                    if (ctsFirst.isCpg) {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiThreadWriter.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiThreadWriter.writerFlush();
                                System.gc();
                            }
                        } else {
                            writer.add(value.vc);
                        }
                    }
                    if (value.isVariant()) {
                        if (getToolkit().getArguments().numberOfThreads > 1) {
                            multiAdditionalWriterForDefaultTcgaMode.add(value.vc, value.refBase.getBase());
                            COUNT_CACHE_FOR_OUTPUT_VCF++;
                            if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                                multiAdditionalWriterForDefaultTcgaMode.writerFlush();
                                System.gc();
                            }
                        } else {
                            additionalWriterForDefaultTcgaMode.add(value.vc);
                        }
                    }
                } else {
                    if (getToolkit().getArguments().numberOfThreads > 1) {
                        multiThreadWriter.add(value.vc, value.refBase.getBase());
                        COUNT_CACHE_FOR_OUTPUT_VCF++;
                        if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                            multiThreadWriter.writerFlush();
                            System.gc();
                        }
                    } else {
                        writer.add(value.vc);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "; this is often caused by using the --assume_single_sample_reads argument with the wrong sample name");
        }
        return sum;
    }
