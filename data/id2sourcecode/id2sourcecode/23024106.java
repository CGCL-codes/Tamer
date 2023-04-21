    @Override
    public Object executeTask() {
        try {
            BufferedWriter data_out = new BufferedWriter(new FileWriter(outFile));
            data_out.write("AS " + aceFile.getContigCount() + " " + aceFile.getReadCount());
            data_out.newLine();
            data_out.newLine();
            Contig[] contigs = aceFile.getContigs();
            System.out.println("contigcount." + contigs.length);
            for (int c = 0; c < contigs.length; c++) {
                firePropertyChange("label", "", "Writing Contig '" + contigs[c].getName() + "'...");
                data_out.write("CO " + contigs[c].getName() + " " + contigs[c].getPaddedBaseCount() + " " + contigs[c].getReadCount() + " " + contigs[c].getBaseSegmentCount() + " " + (contigs[c].isComplemented() ? "C" : "U"));
                data_out.newLine();
                byte[] sequence = contigs[c].getPaddedSequence();
                for (int i = 1; i <= sequence.length; i++) {
                    data_out.write(sequence[i - 1]);
                    if (i % 50 == 0 && i > 1 && i < sequence.length) {
                        data_out.newLine();
                    }
                }
                data_out.newLine();
                data_out.newLine();
                data_out.write("BQ ");
                data_out.newLine();
                int[] baseQualities = contigs[c].getBaseQualities();
                for (int i = 1; i <= baseQualities.length; i++) {
                    data_out.write(" " + Integer.toString(baseQualities[i - 1]));
                    if (i % 50 == 0 && i > 1 && i < baseQualities.length) {
                        data_out.newLine();
                    }
                }
                data_out.newLine();
                data_out.newLine();
                Read[] reads = contigs[c].getReads();
                for (int i = 0; i < reads.length; i++) {
                    data_out.write("AF " + reads[i].getName() + " " + (reads[i].isComplemented() ? "C" : "U") + " " + reads[i].getPaddedStartPosition());
                    data_out.newLine();
                }
                data_out.newLine();
                BaseSegment[] baseSegments = contigs[c].getBaseSegments();
                for (int i = 0; i < baseSegments.length; i++) {
                    data_out.write("BS " + baseSegments[i].getPaddedStart() + " " + baseSegments[i].getPaddedStop() + " " + baseSegments[i].getReadName());
                    data_out.newLine();
                }
                data_out.newLine();
                for (int i = 0; i < reads.length; i++) {
                    data_out.write("RD " + reads[i].getName() + " " + reads[i].getWholeReadInfoItemCount() + " " + reads[i].getReadTagCount());
                    data_out.newLine();
                    byte[] readSequence = reads[i].getPaddedSequence();
                    for (int j = 1; j <= readSequence.length; j++) {
                        data_out.write(readSequence[j - 1]);
                        if (j % 50 == 0 && j > 1 && j < readSequence.length) {
                            data_out.newLine();
                        }
                    }
                    data_out.newLine();
                    data_out.newLine();
                    data_out.write("QA " + reads[i].getQualityClippingStart() + " " + reads[i].getAlignClippingStop() + " " + reads[i].getAlignClippingStart() + " " + reads[i].getAlignClippingStop());
                    data_out.newLine();
                    data_out.write("DS " + reads[i].getDescription());
                    data_out.newLine();
                    readCountCurrent++;
                }
                data_out.newLine();
            }
            data_out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Error r) {
            r.printStackTrace();
        }
        return null;
    }
