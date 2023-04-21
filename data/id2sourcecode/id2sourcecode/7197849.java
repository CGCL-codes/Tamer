    private void copyCssFile(String cssFileName, File destinationFile) {
        InputStream source = null;
        FileOutputStream destination = null;
        try {
            String filename = "/xslt/" + cssFileName;
            source = ReportConfiguration.class.getResourceAsStream(filename);
            destination = new FileOutputStream(destinationFile);
            int readByte = 0;
            while ((readByte = source.read()) > 0) {
                destination.write(readByte);
            }
            destination.close();
            source.close();
        } catch (Exception e) {
            timeSlotTracker.errorLog(e);
        } finally {
            try {
                if (destination != null) {
                    destination.close();
                }
                if (source != null) {
                    source.close();
                }
            } catch (Exception e) {
                timeSlotTracker.errorLog(e);
            }
        }
    }
