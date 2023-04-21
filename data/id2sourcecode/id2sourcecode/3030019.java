    @SuppressWarnings("unchecked")
    public FiledState(String persistentFileName) throws IOException {
        this.persistentFileName = persistentFileName;
        File persistentFile = new File(persistentFileName);
        if (!persistentFile.exists()) {
            log.info("Persistent file '" + persistentFile.getAbsolutePath() + "' does not yet exist - creating new.");
        } else {
            ObjectInputStream stream = null;
            stream = new ObjectInputStream(new FileInputStream(persistentFile));
            try {
                content = (HashMap<String, Serializable>) stream.readObject();
                log.debug("Persistent file '" + persistentFile.getAbsolutePath() + "' was properly read.");
            } catch (InvalidClassException ex) {
                log.warn("Resetting stored FiledState, due to " + ex);
            } catch (ClassNotFoundException ex) {
                log.error("Resetting stored FiledState, due to ClassNotFoundException", ex);
            } catch (WriteAbortedException ex) {
                log.error("Resetting stored FiledState, due to WriteAbortedException", ex);
            } finally {
                stream.close();
            }
        }
        if (content == null) {
            content = new HashMap<String, Serializable>();
        }
        writeThread = new WriteThread(DEFAULT_DELAY_MILLIS);
        writeThread.start();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
