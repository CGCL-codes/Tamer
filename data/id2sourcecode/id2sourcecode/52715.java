    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("CreateSyntheticGenome was not called properly. It should be called as:");
            System.err.println("\tjava CreateSyntheticGenome fasta_input_file slo_output_file mutations_file [read_length]");
        }
        String inputFile = args[0];
        String outputFile = args[1];
        String mutationFile = args[2];
        int readLength = 36;
        if (args.length > 3) {
            readLength = Integer.parseInt(args[3]);
        }
        int numberOfMutations = fileLineLength(mutationFile);
        HashMap[] mutationArray = new HashMap[numberOfMutations];
        populateMutationHash(mutationFile, mutationArray);
        for (int i = 1; i < mutationArray.length; i++) {
            String mLen1 = mutationArray[i - 1].get("mutation").toString();
            int mPos1 = Integer.parseInt(mutationArray[i - 1].get("position").toString());
            int mPos2 = Integer.parseInt(mutationArray[i].get("position").toString());
            if (mPos1 + readLength + mLen1.length() >= mPos2 - readLength) {
                System.err.println("ERROR: Mutation position + length of mutation must be more than readLength (" + readLength + ") bases apart from the next mutation position minus readLength.");
                System.err.println(mPos2 + " is too close to " + mPos1);
                System.exit(-1);
            }
        }
        Sequence fastaSequence = prepareFastaRead(inputFile);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            System.exit(-1);
        }
        try {
            out.write("#QuerySequenceID\tQuerySeq\tTargetLocation\tTargetStrand\tTargetSequence\tQueryQualityScore\tQueryPrefixTags\tQuerySuffixTags\tMultiReads\tReadNum\tQueryUnconvertedCsFirstRead\tQueryUnconvertedCsSecondRead\n");
        } catch (IOException e) {
            System.exit(-1);
        }
        for (int i = 0; i < mutationArray.length; i++) {
            int mPosition = Integer.parseInt(mutationArray[i].get("position").toString());
            String mType = mutationArray[i].get("type").toString();
            String mOriginal = mutationArray[i].get("original").toString();
            String mMutation = mutationArray[i].get("mutation").toString();
            int readOffset = mPosition - readLength;
            int totalReadLength = 2 * readLength - 1;
            if (readOffset < 0) {
                totalReadLength += readOffset;
                readOffset = 0;
            }
            String seqRead = fastaSequence.subStr(readOffset + 1, readOffset + totalReadLength);
            seqRead = seqRead.toUpperCase();
            String referenceSeq = seqRead;
            String mutatedSeq = seqRead;
            int changePos = totalReadLength - readLength;
            if (mType.equals("i")) {
            } else if ((mType.equals("v") || mType.equals("d")) && mOriginal.equals(seqRead.substring(changePos, changePos + mOriginal.length()))) {
                if (mType.equals("v")) {
                    mutatedSeq = seqRead.substring(0, changePos) + mMutation + seqRead.substring(changePos + 1, totalReadLength);
                } else if (mType.equals("d")) {
                    totalReadLength += mMutation.length() - 1;
                    seqRead = fastaSequence.subStr(readOffset + 1, readOffset + totalReadLength);
                    seqRead = seqRead.toUpperCase();
                    referenceSeq = seqRead;
                    mutatedSeq = seqRead.substring(0, changePos) + mMutation + seqRead.substring(changePos + 1, totalReadLength);
                } else if (mType.equals("i")) {
                    totalReadLength += mMutation.length() - 1;
                    seqRead = fastaSequence.subStr(readOffset + 1, readOffset + totalReadLength);
                    seqRead = seqRead.toUpperCase();
                    mutatedSeq = seqRead.substring(1, changePos + 1) + mMutation + seqRead.substring(changePos + 1, totalReadLength);
                    referenceSeq = seqRead.substring(1, changePos + 1) + mOriginal + seqRead.substring(changePos + 1, totalReadLength);
                    readOffset++;
                } else {
                    System.err.println("Something is wrong, mType came up as " + mType + ".");
                    System.exit(1);
                }
            } else {
                System.err.println("Something is wrong. The mutation was supposed to change a \'" + mOriginal + "\' but instead, that position is a \'" + seqRead.substring(changePos, changePos + 1) + "\'.");
                System.exit(1);
            }
            for (int j = 0; j <= totalReadLength - readLength; j++) {
                try {
                    out.write("1:1:1:" + ((i + 1) * 1000 + j) + "\t" + mutatedSeq.substring(j, j + readLength) + "\tchr22:" + (readOffset + j) + "\tF\t" + referenceSeq.substring(j, j + readLength) + "\t0" + "\n");
                    out.write("1:1:1:" + ((i + 1) * 1000 + j) + "\t" + mutatedSeq.substring(j, j + readLength) + "\tchr22:" + (readOffset + j) + "\tR\t" + reverseString(referenceSeq.substring(j, j + readLength)) + "\t0" + "\n");
                } catch (IOException e) {
                    System.exit(-1);
                }
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            System.exit(-1);
        }
        System.exit(0);
    }
