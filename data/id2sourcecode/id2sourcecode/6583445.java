    public boolean write(String filename, CurationSet cs) {
        logger.debug("ChadoAdHocFlatFileWriter: writing CurationSet " + cs + " to file " + filename);
        File outFile = new File(filename);
        if (outFile.exists()) {
            logger.error("ChadoAdHocFlatFileWriter: file " + filename + " already exists - write failed");
            return false;
        }
        try {
            FileWriter writer = new FileWriter(outFile);
            PrintWriter pwriter = new PrintWriter(writer);
            writeCurationSet(pwriter, cs);
            pwriter.close();
        } catch (IOException ioe) {
            logger.error("IOException writing CurationSet to " + filename, ioe);
            return false;
        }
        return true;
    }
