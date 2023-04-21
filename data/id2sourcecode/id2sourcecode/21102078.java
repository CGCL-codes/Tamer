    public boolean parseWorkingColorSpaceFile() {
        try {
            BufferedReader in = IO.fetchBufferedReader(workingFile);
            String line;
            String[] tokens = null;
            int counter = 0;
            int chromIndex = 9;
            int positionIndex = 10;
            int oriIndex = 11;
            int probabilityIndex = 8;
            int alignmentScoreIndex = 7;
            int readSequenceIndex = 4;
            int readQualityIndex = 5;
            in = IO.fetchBufferedReader(workingFile);
            counter = 0;
            while ((line = in.readLine()) != null) {
                try {
                    if (++counter == 25000) {
                        if (verbose) System.out.print(".");
                        counter = 0;
                    }
                    if (line.startsWith("#")) continue;
                    tokens = tab.split(line);
                    numberAlignments++;
                    if (tokens.length < 15 || tokens.length > 18 || tokens[chromIndex].contains(adapterName)) {
                        if (printBadLines) System.err.println("Bad line, tokens, skipping -> " + line);
                        continue;
                    }
                    float probScore = Float.parseFloat(tokens[probabilityIndex]);
                    if (probScore < probThreshold) continue;
                    float alignmentScore = Float.parseFloat(tokens[alignmentScoreIndex]);
                    if (alignmentScore > alignmentThreshold) continue;
                    int size = tokens[readSequenceIndex].length();
                    if (size != tokens[readQualityIndex].length()) {
                        if (printBadLines) System.err.println("\nBad line, read/ qual length, skipping -> " + line);
                        continue;
                    }
                    histogram.count(size);
                    String chromosome = tokens[chromIndex].substring(1);
                    numberPassingAlignments++;
                    String strand = "+";
                    if (tokens[oriIndex].equals("R")) strand = "-";
                    int start;
                    int stop;
                    String chrStrand;
                    boolean spliceJunctionPresent;
                    Matcher mat = spliceJunction.matcher(chromosome);
                    if (mat.matches()) {
                        if (skipUnderScoredChromosomes) continue;
                        spliceJunctionPresent = true;
                        chromosome = mat.group(1);
                        start = Integer.parseInt(mat.group(2));
                        stop = Integer.parseInt(mat.group(3));
                        chrStrand = chromosome + strand + "SpliceJunction";
                    } else {
                        spliceJunctionPresent = false;
                        start = Integer.parseInt(tokens[positionIndex]) - 1;
                        stop = start + size;
                        chrStrand = chromosome + strand;
                    }
                    float convertedScore = (float) (1 - Num.antiNeg10log10(probScore));
                    DataOutputStream dos;
                    if (chromOut.containsKey(chrStrand)) dos = chromOut.get(chrStrand); else {
                        File f = new File(tempDirectory, chrStrand);
                        if (spliceJunctionPresent) tempChrSpliceJunctionData.put(chromosome + strand, f); else tempChrData.put(chromosome + strand, f);
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
                        chromOut.put(chrStrand, dos);
                    }
                    dos.writeInt(start);
                    dos.writeInt(stop);
                    dos.writeFloat(convertedScore);
                    dos.writeInt(size + tokens[3].length() + 1);
                    dos.writeBytes(tokens[readSequenceIndex] + ";" + tokens[readQualityIndex]);
                } catch (NumberFormatException ne) {
                    System.err.println("\nBad line skipping -> " + line);
                }
            }
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
