    @Override
    public Object executeTask() {
        try {
            FileOutputStream file_output = new FileOutputStream(outFile);
            DataOutputStream data_out = new DataOutputStream(new BufferedOutputStream(file_output));
            data_out.writeInt(aceFile.getContigCount());
            data_out.writeInt(aceFile.getReadCount());
            data_out.writeInt(aceFile.getMedianCoverage());
            data_out.writeDouble(aceFile.getMeanCoverage());
            Scaffold scaffold = aceFile.getScaffolds();
            data_out.writeInt(scaffold.size());
            for (int i = 0; i < scaffold.size(); i++) {
                data_out.writeInt(scaffold.get(i).size());
                for (int j = 0; j < scaffold.get(i).size(); j++) {
                    data_out.writeInt(scaffold.get(i).get(j).intValue());
                }
            }
            Contig[] contigs = aceFile.getContigs();
            for (int c = 0; c < contigs.length; c++) {
                firePropertyChange("label", "", "Writing Contig '" + contigs[c].getName() + "'...");
                data_out.writeUTF(contigs[c].getName());
                data_out.writeInt(contigs[c].getPaddedBaseCount());
                data_out.writeInt(contigs[c].getUnpaddedBaseCount());
                data_out.writeInt(contigs[c].getReadCount());
                data_out.writeInt(contigs[c].getMinIndex());
                data_out.writeInt(contigs[c].getMaxIndex());
                data_out.writeInt(contigs[c].getLeftCoverage());
                data_out.writeInt(contigs[c].getRightCoverage());
                data_out.writeDouble(contigs[c].getMeanCoverage());
                data_out.writeInt(contigs[c].getMedianCoverage());
                data_out.writeInt(contigs[c].getIndexOfFirstRightOverlappingRead());
                data_out.writeInt(contigs[c].getBaseSegmentCount());
                BaseSegment[] baseSegments = contigs[c].getBaseSegments();
                for (int i = 0; i < baseSegments.length; i++) {
                    data_out.writeUTF(baseSegments[i].getReadName());
                    data_out.writeInt(baseSegments[i].getPaddedStart());
                    data_out.writeInt(baseSegments[i].getPaddedStop());
                }
                data_out.writeBoolean(contigs[c].isComplemented());
                data_out.write(contigs[c].getPaddedSequence());
                int[] values = contigs[c].getBaseQualities();
                for (int i = 0; i < values.length; i++) {
                    data_out.writeByte(values[i]);
                }
                Read[] reads = contigs[c].getReads();
                for (int i = 0; i < reads.length; i++) {
                    readCountCurrent++;
                    data_out.writeUTF(reads[i].getName());
                    data_out.writeBoolean(reads[i].isComplemented());
                    data_out.writeInt(reads[i].getPaddedStartPosition());
                    data_out.writeInt(reads[i].getPaddedBaseCount());
                    data_out.write(reads[i].getPaddedSequence());
                    data_out.writeInt(reads[i].getQualityClippingStart());
                    data_out.writeInt(reads[i].getQualityClippingStop());
                    data_out.writeInt(reads[i].getAlignClippingStart());
                    data_out.writeInt(reads[i].getAlignClippingStop());
                    data_out.writeUTF(reads[i].getDescription());
                }
                data_out.writeInt(contigRepresentations[c].getBounds().x);
                data_out.writeInt(contigRepresentations[c].getBounds().y);
            }
            data_out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Error r) {
            r.printStackTrace();
        }
        return null;
    }
