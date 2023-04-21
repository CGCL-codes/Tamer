    @Override
    public void run() {
        try {
            if (movingAverageSize % 2 == 0 || movingAverageSize <= 0 || movingAverageSize >= 16) {
                String msg = "Moving average size " + movingAverageSize + " is not valid.  Must be odd and between 1 and 15.";
                setProgress(1, "failed: " + msg);
                setCurrentState(FAILED);
                System.out.println("failed: " + msg);
                setMessage(msg);
                return;
            }
            setCurrentState(RUNNING);
            setStage(0);
            double[] bbox = new double[4];
            bbox[0] = region.getBoundingBox()[0][0];
            bbox[1] = region.getBoundingBox()[0][1];
            bbox[2] = region.getBoundingBox()[1][0];
            bbox[3] = region.getBoundingBox()[1][1];
            setProgress(0, "getting species data");
            Records records = new Records(biocacheserviceurl, speciesq, bbox, null);
            int occurrenceCount = records.getRecordsSize();
            int boundingboxcellcount = (int) ((bbox[2] - bbox[0]) * (bbox[3] - bbox[1]) / (gridsize * gridsize));
            System.out.println("SitesBySpecies for " + occurrenceCount + " occurrences in up to " + boundingboxcellcount + " grid cells.");
            String error = null;
            if (boundingboxcellcount > AlaspatialProperties.getAnalysisLimitGridCells()) {
                error = "Too many potential output grid cells.  Decrease area or increase resolution.";
            } else if (occurrenceCount > AlaspatialProperties.getAnalysisLimitOccurrences()) {
                error = "Too many occurrences for the selected species.  " + occurrenceCount + " occurrences found, must be less than " + AlaspatialProperties.getAnalysisLimitOccurrences();
            } else if (occurrenceCount == 0) {
                error = "No occurrences found";
            }
            if (error != null) {
                setProgress(1, "failed: " + error);
                setCurrentState(FAILED);
                System.out.println("SitesBySpecies ERROR: " + error);
                setMessage(error);
                return;
            }
            setStage(1);
            setProgress(0, "building sites by species matrix for " + records.getSpeciesSize() + " species in " + records.getRecordsSize() + " occurrences");
            String envelopeFile = AlaspatialProperties.getAnalysisWorkingDir() + "envelope_" + getName();
            Grid envelopeGrid = null;
            if (envelope != null) {
                GridCutter.makeEnvelope(envelopeFile, AlaspatialProperties.getLayerResolutionDefault(), envelope);
                envelopeGrid = new Grid(envelopeFile);
            }
            if (sitesbyspecies) {
                SitesBySpecies sbs = new SitesBySpecies(0, gridsize, bbox);
                int[] counts = sbs.write(records, currentPath + File.separator, region, envelopeGrid);
                writeMetadata(currentPath + File.separator + "sxs_metadata.html", "Sites by Species", records, bbox, false, false, counts, areasqkm);
            }
            Legend occurrencesLegend = null;
            if (occurrencedensity) {
                setProgress(0.3, "building occurrence density layer");
                OccurrenceDensity od = new OccurrenceDensity(movingAverageSize, gridsize, bbox);
                od.write(records, currentPath + File.separator, "occurrence_density", AlaspatialProperties.getAnalysisThreadCount(), true, true);
                writeProjectionFile(currentPath + File.separator + "occurrence_density.prj");
                String url = AlaspatialProperties.getGeoserverUrl() + "/rest/workspaces/ALA/coveragestores/odensity_" + getName() + "/file.arcgrid?coverageName=odensity_" + getName();
                String extra = "";
                String username = AlaspatialProperties.getGeoserverUsername();
                String password = AlaspatialProperties.getGeoserverPassword();
                String[] infiles = { currentPath + "occurrence_density.asc", currentPath + "occurrence_density.prj" };
                String ascZipFile = currentPath + "occurrence_density.zip";
                Zipper.zipFiles(infiles, ascZipFile);
                System.out.println("Uploading file: " + ascZipFile + " to \n" + url);
                UploadSpatialResource.loadResource(url, extra, username, password, ascZipFile);
                occurrencesLegend = produceSld(currentPath + File.separator + "occurrence_density");
                url = AlaspatialProperties.getGeoserverUrl() + "/rest/styles/";
                UploadSpatialResource.loadCreateStyle(url, extra, username, password, "odensity_" + getName());
                url = AlaspatialProperties.getGeoserverUrl() + "/rest/styles/odensity_" + getName();
                UploadSpatialResource.loadSld(url, extra, username, password, currentPath + File.separator + "occurrence_density.sld");
                String data = "<layer><enabled>true</enabled><defaultStyle><name>odensity_" + getName() + "</name></defaultStyle></layer>";
                url = AlaspatialProperties.getGeoserverUrl() + "/rest/layers/ALA:odensity_" + getName();
                UploadSpatialResource.assignSld(url, extra, username, password, data);
                occurrencesLegend.generateLegend(currentPath + File.separator + "occurrence_density_legend.png");
                writeMetadata(currentPath + File.separator + "odensity_metadata.html", "Occurrence Density", records, bbox, occurrencedensity, false, null, null);
                new File(ascZipFile).delete();
            }
            Legend speciesLegend = null;
            if (speciesdensity) {
                setProgress(0.6, "building species richness layer");
                SpeciesDensity sd = new SpeciesDensity(movingAverageSize, gridsize, bbox);
                sd.write(records, currentPath + File.separator, "species_richness", AlaspatialProperties.getAnalysisThreadCount(), true, true);
                writeProjectionFile(currentPath + File.separator + "species_richness.prj");
                String url = AlaspatialProperties.getGeoserverUrl() + "/rest/workspaces/ALA/coveragestores/srichness_" + getName() + "/file.arcgrid?coverageName=srichness_" + getName();
                String extra = "";
                String username = AlaspatialProperties.getGeoserverUsername();
                String password = AlaspatialProperties.getGeoserverPassword();
                String[] infiles = { currentPath + "species_richness.asc", currentPath + "species_richness.prj" };
                String ascZipFile = currentPath + "species_richness.zip";
                Zipper.zipFiles(infiles, ascZipFile);
                System.out.println("Uploading file: " + ascZipFile + " to \n" + url);
                UploadSpatialResource.loadResource(url, extra, username, password, ascZipFile);
                speciesLegend = produceSld(currentPath + File.separator + "species_richness");
                url = AlaspatialProperties.getGeoserverUrl() + "/rest/styles/";
                UploadSpatialResource.loadCreateStyle(url, extra, username, password, "srichness_" + getName());
                url = AlaspatialProperties.getGeoserverUrl() + "/rest/styles/srichness_" + getName();
                UploadSpatialResource.loadSld(url, extra, username, password, currentPath + File.separator + "species_richness.sld");
                String data = "<layer><enabled>true</enabled><defaultStyle><name>srichness_" + getName() + "</name></defaultStyle></layer>";
                url = AlaspatialProperties.getGeoserverUrl() + "/rest/layers/ALA:srichness_" + getName();
                UploadSpatialResource.assignSld(url, extra, username, password, data);
                speciesLegend.generateLegend(currentPath + File.separator + "species_richness_legend.png");
                writeMetadata(currentPath + File.separator + "srichness_metadata.html", "Species Richness", records, bbox, false, speciesdensity, null, null);
                new File(ascZipFile).delete();
            }
            setProgress(1, "finished");
            CitationService.generateSitesBySpeciesReadme(currentPath + File.separator, sitesbyspecies, occurrencedensity, speciesdensity);
            setCurrentState(SUCCESSFUL);
            System.out.println("finished building sites by species matrix");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed with exception: " + e.getMessage());
            setProgress(1, "failed: " + e.getMessage());
            setCurrentState(FAILED);
            setMessage("Error processing your Sites By Species request. Please try again or if problem persists, contact the Administrator\nPlease quote the Prediction ID: " + getName());
        }
    }
