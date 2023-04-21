    public void loadGeneModels() {
        UCSCGeneModelTableReader reader = null;
        if (refSeqFile != null) {
            reader = new UCSCGeneModelTableReader(refSeqFile, 0);
            if (scoreIntrons) {
                if (verbose) System.out.println("\tParsing/scoring introns instead of exons from gene models.");
                reader.swapIntronsForExons();
            }
            if (removeOverlappingRegions) {
                if (verbose) System.out.print("\tRemoving overlapping regions from gene models");
                String deletedGenes = reader.removeOverlappingExons();
                if (deletedGenes.length() != 0) if (verbose) System.out.println("\t\tWARNING: the following genes had more than 1/2 of their exonic bps removed -> " + deletedGenes);
                File f = new File(saveDirectory, Misc.removeExtension(refSeqFile.getName()) + "_NoOverlappingExons.txt");
                if (verbose) System.out.println("\t\tWrote modified gene table to the save directory.");
                reader.writeGeneTableToFile(f);
            }
            reader.splitByChromosome();
            geneModels = reader.getChromSpecificGeneLines();
            allGeneLines = reader.getGeneLines();
        } else if (bedFile != null) {
            calculateChiSquare = false;
            Bed[] bed = Bed.parseFile(bedFile, 0, 0);
            allGeneLines = new UCSCGeneLine[bed.length];
            boolean addName = bed[0].getName().trim().equals("");
            for (int i = 0; i < bed.length; i++) {
                if (addName) {
                    String name = (i + 1) + "_" + bed[i].getChromosome() + ":" + bed[i].getStart() + "-" + bed[i].getStop();
                    bed[i].setName(name);
                }
                allGeneLines[i] = new UCSCGeneLine(bed[i]);
            }
            reader = new UCSCGeneModelTableReader();
            reader.setGeneLines(allGeneLines);
            reader.splitByChromosome();
            geneModels = reader.getChromSpecificGeneLines();
        }
        if (geneModels == null || allGeneLines == null || allGeneLines.length == 0) Misc.printExit("\nProblem loading your USCS gene model table or bed file? No genes/ regions?\n");
        if (reader.checkStartStopOrder() == false) Misc.printExit("\nOne of your regions's coordinates are reversed. Check that each start is less than the stop.\n");
        if (reader.getGeneLines().length < 100) {
            System.err.println("\nWarning! Too few regions to scan! DRSS needs at least 100 regions to properly estimate FDRs.\n");
        }
        for (int i = 0; i < allGeneLines.length; i++) allGeneLines[i].setScores(new float[] { 0, 0 });
    }
