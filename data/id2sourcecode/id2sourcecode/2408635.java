    public void alignAndSavePositions(byte[][] reads) throws IOException {
        long[] matchRanges1;
        long[] matchRanges2;
        LongArrayList matchRangeList1 = new LongArrayList(50);
        LongArrayList matchRangeList2 = new LongArrayList(50);
        LongArrayList matchesList1 = new LongArrayList(50);
        LongArrayList matchesList2 = new LongArrayList(50);
        long[] matchesArray1;
        long[] matchesArray2;
        FileWriter result = new FileWriter(new File("alignments.txt"));
        ObjectArrayList list = metadata.getStartingPositions().values();
        LongArrayList startingPositions = new LongArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            startingPositions.add((Long) (list.getQuick(i)));
        }
        startingPositions.sort();
        for (int i = 0; i < reads.length; i++) {
            result.write(Utils.decode(reads[i]));
            byte[] reverse = Utils.reverseComplement(reads[i]);
            try {
                matchRangeList1.clear();
                matchRangeList2.clear();
                align(reads[i], matchRangeList1);
                align(reverse, matchRangeList2);
            } catch (Throwable e) {
                System.out.println("problem alignining read");
                e.printStackTrace();
            }
            matchRanges1 = matchRangeList1.elements();
            matchRanges2 = matchRangeList2.elements();
            matchRanges1 = Arrays.copyOf(matchRanges1, matchRangeList1.size());
            matchRanges2 = Arrays.copyOf(matchRanges2, matchRangeList2.size());
            matchesList1.clear();
            matchesList2.clear();
            for (int j = 1; j < matchRanges1.length; j = j + 2) {
                if (matchRanges1[j] == AlignerConstants.INVALID) continue;
                for (long k = matchRanges1[j - 1]; k <= matchRanges1[j]; k++) {
                    matchesList1.add(sa.get(k));
                }
            }
            for (int j = 1; j < matchRanges2.length; j = j + 2) {
                if (matchRanges2[j] == AlignerConstants.INVALID) continue;
                for (long k = matchRanges2[j - 1]; k <= matchRanges2[j]; k++) {
                    matchesList2.add(sa.get(k));
                }
            }
            int size = matchesList1.size();
            int[] pos;
            result.write("\tpositive strand: ");
            for (int j = 0; j < matchesList1.size(); j++) {
                pos = Utils.toChromosomePosition(matchesList1.getQuick(j), startingPositions);
                if (pos[0] == 23) {
                    result.write("X:" + pos[1] + "  ");
                } else if (pos[0] == 24) {
                    result.write("Y:" + pos[1] + "  ");
                } else {
                    result.write(pos[0] + ":" + pos[1] + "  ");
                }
            }
            size = matchesList2.size();
            result.write("\tnegative strand: ");
            for (int j = 0; j < matchesList2.size(); j++) {
                pos = Utils.toChromosomePosition(matchesList2.getQuick(j), startingPositions);
                if (pos[0] == 23) {
                    result.write("X:" + pos[1] + "  ");
                } else if (pos[0] == 24) {
                    result.write("Y:" + pos[1] + "  ");
                } else {
                    result.write(pos[0] + ":" + pos[1] + "  ");
                }
            }
            result.write("\n");
            matched++;
        }
        result.close();
        System.exit(0);
    }
