    public boolean parseWorkingSAMFile() {
        try {
            BufferedReader in = IO.fetchBufferedReader(workingFile);
            String line;
            int counter = 0;
            String currentChromStrand = "";
            DataOutputStream dos = null;
            int numBadLines = 0;
            while ((line = in.readLine()) != null) {
                if (++counter == 25000) {
                    System.out.print(".");
                    counter = 0;
                }
                line = line.trim();
                if (line.length() == 0 || line.startsWith("@")) continue;
                SamAlignment sa;
                try {
                    sa = new SamAlignment(line, false);
                } catch (Exception e) {
                    System.out.println("\nSkipping malformed sam alignment ->\n" + line + "\n" + e.getMessage());
                    if (numBadLines++ > 1000) Misc.printErrAndExit("\nAboring: too many malformed SAM alignments.\n");
                    continue;
                }
                if (sa.isUnmapped()) {
                    numberAlignmentsUnmapped++;
                    continue;
                }
                if (sa.failedQC()) {
                    numberAlignmentsFailingQC++;
                    continue;
                }
                if (sa.getReferenceSequence().startsWith(phiX) || sa.getReferenceSequence().startsWith(adapter)) {
                    numberControlAlignments++;
                    continue;
                }
                if (sa.getAlignmentScore() > maximumAlignmentScore) {
                    numberAlignmentsFailingAlignmentScore++;
                    continue;
                }
                if (sa.getMappingQuality() < minimumPosteriorProbability) {
                    numberAlignmentsFailingQualityScore++;
                    continue;
                }
                if (uniquesOnly && sa.isADuplicate()) {
                    numberAlignmentsFailingDuplicateCheck++;
                    continue;
                }
                numberPassingAlignments++;
                String firstSecond = "";
                if (sa.isFirstPair()) firstSecond = "/1";
                if (sa.isSecondPair()) firstSecond = "/2";
                String readID = sa.getName() + firstSecond;
                String chromosomeStrand = null;
                if (reverseSecondPairsStrand && sa.isSecondPair()) {
                    if (sa.isReverseStrand()) chromosomeStrand = sa.getReferenceSequence() + "+"; else chromosomeStrand = sa.getReferenceSequence() + "-";
                } else {
                    if (sa.isReverseStrand()) chromosomeStrand = sa.getReferenceSequence() + "-"; else chromosomeStrand = sa.getReferenceSequence() + "+";
                }
                int position = sa.getPosition();
                sa.trimMaskingOfReadToFitAlignment();
                String isGA;
                if (sa.isPartOfAPairedAlignment()) {
                    if (sa.isFirstPair() && sa.isReverseStrand() == false) isGA = "f"; else if (sa.isSecondPair() && sa.isReverseStrand()) isGA = "f"; else isGA = "t";
                } else if (sa.isReverseStrand()) isGA = "t"; else isGA = "f";
                if (currentChromStrand.equals(chromosomeStrand) == false) {
                    currentChromStrand = chromosomeStrand;
                    if (chromOut.containsKey(currentChromStrand)) dos = chromOut.get(currentChromStrand); else {
                        File f = new File(saveDirectory, currentChromStrand);
                        parsedBinaryDataFiles.add(f);
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
                        chromOut.put(currentChromStrand, dos);
                    }
                }
                dos.writeUTF(readID);
                dos.writeInt(position);
                dos.writeUTF(sa.getSequence());
                dos.writeUTF(sa.getQualities());
                dos.writeUTF(isGA + sa.getCigar());
                if (sa.isFirstPair() && sa.isPartOfAPairedAlignment()) {
                    if (sa.getMateReferenceSequence().equals("=") || sa.getMateReferenceSequence().equals(sa.getReferenceSequence())) {
                        int leftPos = position;
                        int rightPos = sa.getMatePosition();
                        int l;
                        int r;
                        if (leftPos > rightPos) {
                            l = rightPos;
                            r = leftPos;
                        } else {
                            l = leftPos;
                            r = rightPos;
                        }
                        int sequenceLength = sa.getSequence().length();
                        int stopL = l + sequenceLength;
                        long diff = stopL - r;
                        if (diff >= 0) bpPairedOverlappingSequence += diff;
                        bpPairedSequence += (2 * sequenceLength);
                    }
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.err.println("\nError parsing Novoalign file or writing split binary chromosome files.\nToo many open files? Too many chromosomes? " + "If so then login as root and set the default higher using the ulimit command (e.g. ulimit -n 10000)\n");
            e.printStackTrace();
            return false;
        }
        return true;
    }
