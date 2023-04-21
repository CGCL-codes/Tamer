    public void setTransactionLog(String log) throws FileNotFoundException {
        fileInputStream = new FileInputStream(log);
        fileChannel = fileInputStream.getChannel();
        logger.info("Using log: " + log);
    }
