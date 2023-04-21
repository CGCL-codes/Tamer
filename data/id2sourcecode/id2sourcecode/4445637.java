    public void sync() throws DBException {
        try {
            FileOutputStream os = new FileOutputStream(dataFile);
            FileChannel fc = os.getChannel();
            dataGuide.write(fc, getBrokerPool().getSymbols());
            os.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new DBException("Error while writing " + dataFile.getAbsolutePath() + ": " + e.getMessage());
        }
    }
