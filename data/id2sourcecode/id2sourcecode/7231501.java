    public void loadGeneModels() {
        UCSCGeneModelTableReader reader = null;
        if (refSeqFile != null) {
            reader = new UCSCGeneModelTableReader(refSeqFile, 0);
            if (removeOverlappingRegions) {
                System.out.print("\tRemoving overlapping regions from gene models");
                String deletedGenes = reader.removeOverlappingExons();
                int numDelGenes = deletedGenes.split(",").length;
                if (deletedGenes.length() != 0) {
                    File deleted = new File(saveDirectory, Misc.removeExtension(refSeqFile.getName()) + "_TrimmedDeletedGenes.txt");
                    System.out.println("\tWARNING: " + numDelGenes + " genes had more than 1/2 of their exonic bps removed. See " + deleted);
                    IO.writeString(deletedGenes, deleted);
                }
                File f = new File(saveDirectory, Misc.removeExtension(refSeqFile.getName()) + "_NoOverlappingExons.txt");
                System.out.println("\tWrote the trimmed gene table to the save directory. Use this table and the -o option to speed up subsequent processing.");
                reader.writeGeneTableToFile(f);
            }
            genes = reader.getGeneLines();
        } else if (bedFile != null) {
            Bed[] bed = Bed.parseFile(bedFile, 0, 0);
            genes = new UCSCGeneLine[bed.length];
            boolean addName = bed[0].getName().trim().equals("");
            for (int i = 0; i < bed.length; i++) {
                if (addName) bed[i].setName((i + 1) + "");
                genes[i] = new UCSCGeneLine(bed[i]);
            }
            reader = new UCSCGeneModelTableReader();
            reader.setGeneLines(genes);
        }
        if (genes == null || genes.length == 0) Misc.printExit("\nProblem loading your USCS gene model table or bed file? No genes/ regions?\n");
        if (reader.checkStartStopOrder() == false) Misc.printExit("\nOne of your regions's coordinates are reversed. Check that each start is less than the stop.\n");
        reader.replaceNameWithDisplayName();
    }
