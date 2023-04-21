    public void open() throws DatabaseConfigurationException {
        dataGuide = new DataGuide();
        if (dataFile.exists()) {
            try {
                long start = System.currentTimeMillis();
                FileInputStream is = new FileInputStream(dataFile);
                FileChannel fc = is.getChannel();
                dataGuide.read(fc, getBrokerPool().getSymbols());
                is.close();
                if (LOG.isDebugEnabled()) LOG.debug("Reading " + dataFile.getName() + " took " + (System.currentTimeMillis() - start) + "ms. Size of " + "the graph: " + dataGuide.getSize());
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                throw new DatabaseConfigurationException("Error while loading " + dataFile.getAbsolutePath() + ": " + e.getMessage(), e);
            }
        }
    }
