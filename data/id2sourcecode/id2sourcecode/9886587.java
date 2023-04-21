    public MethodTester(String[] args) {
        if (args.length == 0) Misc.printExit("\nEnter a full path file/directory text containing 'xxx.sgr' files" + " and full path file text containing tab delimited positive regions (chrom, start, stop)\n");
        System.out.println("Thresholding at an FDR of " + targetFDR + "%");
        sgrFiles = IO.extractFiles(new File(args[0]), ".sgr");
        Arrays.sort(sgrFiles);
        zeroNaNScores = true;
        positiveRegionsFile = new File(args[1]);
        makePositives(positiveRegionsFile);
        System.out.println("Parsing " + positives.length + " positive regions -> " + positiveRegionsFile);
        String[] summaryLines = new String[sgrFiles.length];
        for (int x = 0; x < sgrFiles.length; x++) {
            System.out.println("Parsing sgr file -> " + sgrFiles[x]);
            sgrLines = parseSgrFile(sgrFiles[x], zeroNaNScores);
            if (sgrLines == null) {
                System.out.println("\nNot a number or infinity found, skipping file.\n");
                summaryLines[x] = sgrFiles[x].getName().substring(0, sgrFiles[x].getName().length() - 4);
            } else {
                for (int i = 0; i < 15; i++) {
                    System.out.println("\tBin and scoring from " + (float) start + " to " + (float) stop);
                    makeBins();
                    blankPositives();
                    binSgrs();
                    calculateBinFDRs();
                    if (resetStartStop() == false) break;
                }
                cutOff = (start + stop) / 2;
                StringBuffer sb = new StringBuffer();
                sb.append(sgrFiles[x].getName().substring(0, sgrFiles[x].getName().length() - 4));
                sb.append("\t");
                sb.append(cutOff);
                for (int i = 0; i < positives.length; i++) {
                    sb.append("\t");
                    sb.append(fractionPositive(positives[i].getScores(), cutOff, positives[i].getTotalNumberWindows()));
                }
                summaryLines[x] = sb.toString();
                start = 0;
                stop = 100;
            }
        }
        System.out.print("\nFile Name\tCutOff");
        for (int i = 0; i < positives.length; i++) {
            System.out.print("\t");
            System.out.print(positives[i].getChromosome() + ":" + positives[i].getStart() + "-" + positives[i].getStop() + " (" + positives[i].getTotalNumberWindows() + ")");
        }
        System.out.println();
        for (int i = 0; i < summaryLines.length; i++) {
            System.out.println(summaryLines[i]);
        }
    }
