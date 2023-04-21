    public void parseGeneTable() {
        System.out.println("\n*******************************************************************************");
        System.out.println("Parsing gene table for gene regions and exons...");
        File annotation = new File(resultsDirectory, "Annotation");
        annotation.mkdir();
        filteredGeneTableFile = new File(annotation, "geneModels.ucsc");
        geneRegionFile = new File(annotation, "geneRegions.bed");
        geneExonFile = new File(annotation, "geneExons.bed");
        geneIntronFile = new File(annotation, "geneIntrons.bed");
        if (filteredGeneTableFile.exists() && geneRegionFile.exists() && geneExonFile.exists()) {
            System.out.println("\tWARNING: Annotation folder exists, skipping parsing.  Delete " + annotation + " if you would like to reprocess and restart, otherwise using files within.");
            return;
        }
        UCSCGeneModelTableReader reader = new UCSCGeneModelTableReader(geneTableFile, 0);
        if (filterGeneTable) {
            System.out.print("\tRemoving overlapping exons");
            reader.removeOverlappingExons();
        }
        System.out.println("\tWriting gene regions, exons, introns, and unique gene models...");
        reader.writeGeneTableToFile(filteredGeneTableFile);
        Coordinate[] geneRegions = reader.fetchGeneRegions();
        Coordinate.writeToFile(geneRegions, geneRegionFile);
        Coordinate[] exons = reader.fetchExons();
        Coordinate.writeToFile(exons, geneExonFile);
        reader.swapIntronsForExons();
        Coordinate[] introns = reader.fetchExons();
        Coordinate.writeToFile(introns, geneIntronFile);
    }
