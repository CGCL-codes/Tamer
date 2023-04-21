    public boolean parseWorkingFile() {
        try {
            int badLines = 0;
            BufferedReader in = IO.fetchBufferedReader(workingFile);
            String line;
            String[] tokens = null;
            int counter = 0;
            String currentChromStrand = "";
            DataOutputStream dos = null;
            while ((line = in.readLine()) != null) {
                if (++counter == 25000) {
                    System.out.print(".");
                    counter = 0;
                }
                if (COMMENT.matcher(line).matches()) continue;
                tokens = TAB.split(line);
                if (tokens.length < 14) continue;
                if (tokens[baseCallCommentIndex].equals("QC")) {
                    numberAlignmentsFailingQC++;
                    continue;
                }
                if (tokens[chromosomeIndex].contains(adapter) || tokens[chromosomeIndex].contains(phiX)) {
                    numberControlAlignments++;
                    continue;
                }
                int sequenceLength = tokens[sequenceIndex].length();
                if (sequenceLength != tokens[qualityIndex].length()) {
                    System.err.println("\nSeq length != Qual length, skipping -> " + line);
                    if (badLines++ > 1000) Misc.printErrAndExit("\nToo many malformed lines. Aborting \n");
                    continue;
                }
                if (uniquesOnly && tokens[alignmentStatusIndex].equals("U") == false) {
                    numberAlignmentsFailingDuplicateCheck++;
                    continue;
                }
                float probScore = Float.parseFloat(tokens[posteriorProbabilityIndex]);
                if (probScore < minimumPosteriorProbability) {
                    numberAlignmentsFailingQualityScore++;
                    continue;
                }
                float alignmentScore = Float.parseFloat(tokens[alignmentScoreIndex]);
                if (alignmentScore > maximumAlignmentScore) {
                    numberAlignmentsFailingAlignmentScore++;
                    continue;
                }
                numberPassingAlignments++;
                String chromosome = tokens[chromosomeIndex].substring(1);
                String strand = "-";
                Matcher mat = CTStart.matcher(tokens[baseCallCommentIndex]);
                if (mat.matches()) strand = "+";
                String chromosomeStrand = chromosome + strand;
                if (tokens[strandIndex].equals("R")) {
                    tokens[sequenceIndex] = Seq.reverseComplementDNA(tokens[sequenceIndex]);
                    tokens[qualityIndex] = Misc.reverse(tokens[qualityIndex]);
                }
                if (currentChromStrand.equals(chromosomeStrand) == false) {
                    currentChromStrand = chromosomeStrand;
                    if (chromOut.containsKey(currentChromStrand)) dos = chromOut.get(currentChromStrand); else {
                        File f = new File(saveDirectory, currentChromStrand);
                        parsedBinaryDataFiles.add(f);
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
                        chromOut.put(currentChromStrand, dos);
                    }
                }
                dos.writeUTF(tokens[readIDIndex]);
                int position = Integer.parseInt(tokens[positionIndex]) - 1;
                dos.writeInt(position);
                dos.writeUTF(tokens[sequenceIndex]);
                dos.writeUTF(tokens[qualityIndex]);
                dos.writeUTF(tokens[baseCallCommentIndex]);
                if (tokens[readTypeIndex].equals("L")) {
                    if (tokens[strandIndexMate].equals(".")) continue;
                    if (tokens[chromosomeIndexMate].equals(".") == false) continue;
                    int leftPos = position;
                    int rightPos = Integer.parseInt(tokens[positionIndexMate]) - 1;
                    int l;
                    int r;
                    if (leftPos > rightPos) {
                        l = rightPos;
                        r = leftPos;
                    } else {
                        l = leftPos;
                        r = rightPos;
                    }
                    int stopL = l + sequenceLength;
                    long diff = stopL - r;
                    if (diff >= 0) bpPairedOverlappingSequence += diff;
                    bpPairedSequence += (2 * sequenceLength);
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
