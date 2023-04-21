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
            System.out.println("\tAnnotation folder exists.  Delete to reprocess.");
            return;
        }
        UCSCGeneModelTableReader reader = new UCSCGeneModelTableReader(geneTableFile, 0);
        if (filterGeneTable) {
            System.out.print("\tRemoving overlapping exons");
            String deletedGenes = reader.removeOverlappingExons();
            if (deletedGenes.length() != 0) System.out.println("\t\tWARNING: the following genes had more than 1/2 of their exonic bps removed -> " + deletedGenes);
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
