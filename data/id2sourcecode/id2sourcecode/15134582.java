    private void outputVCFInDifferentOutmode(BisulfiteVariantCallContext value) {
        if (!value.shouldEmit) return;
        if (BAC.OutputMode == OUTPUT_MODE.EMIT_ALL_CYTOSINES) {
            if (value.isC) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.EMIT_ALL_CPG) {
            if (value.isCpg) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.EMIT_VARIANTS_ONLY) {
            if (value.getVariantContext().isVariant()) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.EMIT_HET_SNPS_ONLY) {
            if (value.isHetSnp()) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.DEFAULT_FOR_TCGA) {
            if (value.isCpg) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
            if (value.getVariantContext().isVariant()) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiAdditionalWriterForDefaultTcgaMode.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiAdditionalWriterForDefaultTcgaMode.writerFlush();
                        System.gc();
                    }
                } else {
                    additionalWriterForDefaultTcgaMode.add(value.getVariantContext());
                }
            }
        } else {
            if (getToolkit().getArguments().numberOfThreads > 1) {
                multiThreadWriter.add(value.getVariantContext());
                COUNT_CACHE_FOR_OUTPUT_VCF++;
                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                    multiThreadWriter.writerFlush();
                    System.gc();
                }
                if (BAC.ovd) {
                    verboseWriter.add(value.getVariantContext());
                }
            } else {
                writer.add(value.getVariantContext());
            }
        }
    }
