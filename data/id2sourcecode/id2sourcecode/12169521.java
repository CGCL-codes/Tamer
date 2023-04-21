    private void savePeakLists(ZipOutputStream zipStream) throws IOException, TransformerConfigurationException, SAXException {
        PeakList peakLists[] = savedProject.getPeakLists();
        for (int i = 0; i < peakLists.length; i++) {
            if (isCanceled()) return;
            logger.info("Saving peak list: " + peakLists[i].getName());
            String peakListSavedName = "Peak list #" + (i + 1) + " " + peakLists[i].getName();
            zipStream.putNextEntry(new ZipEntry(peakListSavedName + ".xml"));
            peakListSaveHandler = new PeakListSaveHandler(zipStream, dataFilesIDMap);
            currentSavedObjectName = peakLists[i].getName();
            peakListSaveHandler.savePeakList(peakLists[i]);
        }
    }
