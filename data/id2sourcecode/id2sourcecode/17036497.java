    public boolean loadNetwork(AbstractExecution exe) {
        reportSuccess(exe, AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_NONCOMPLETE);
        boolean success = true;
        try {
            File graphFile = new File(ServerConstants.DATA_DIR + m_networkName + _GRAPH);
            if (graphFile.exists()) {
                try {
                    ObjectInputStream in = null;
                    try {
                        in = new ObjectInputStream(new FileInputStream(graphFile));
                        m_serGraph = (SerializableGraphRepresentation) in.readObject();
                    } catch (ClassNotFoundException ex) {
                        LoggingManager.getInstance().writeSystem("A ClassNotFoundException has occured while trying to read the graph from file " + graphFile.getName(), ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, ex);
                        throw new Exception("A ClassNotFoundException has occured while trying to read the graph from file " + graphFile.getName() + "\n" + ex.getMessage());
                    } catch (IOException ex) {
                        LoggingManager.getInstance().writeSystem("An IOException has occured while trying to read the graph from file " + graphFile.getName(), ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, ex);
                        throw new Exception("An IOException has occured while trying to read the graph from file " + graphFile.getName() + "\n" + ex.getMessage() + "\n" + ex.getStackTrace());
                    } finally {
                        try {
                            if (in != null) in.close();
                        } catch (IOException ex) {
                            LoggingManager.getInstance().writeSystem("An IOException has occured while trying to close the input stream after reading the file: " + graphFile.getName(), ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, ex);
                            throw new Exception("An IOException has occured while trying to close the input stream after reading the file: " + graphFile.getName() + "\n" + ex.getMessage() + "\n" + ex.getStackTrace());
                        }
                    }
                    reportSuccess(exe, AbstractExecution.PHASE_SUCCESS, AbstractExecution.PHASE_COMPLETE);
                    success = true;
                } catch (Exception ex) {
                    reportSuccess(exe, AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
                    LoggingManager.getInstance().writeSystem("Couldn't load " + m_networkName + ".graph.", ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, ex);
                    success = false;
                }
            } else {
                reportSuccess(exe, AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
                LoggingManager.getInstance().writeSystem("The file " + ServerConstants.DATA_DIR + m_networkName + ".graph doesn't exist.", ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, null);
                success = false;
            }
        } catch (RuntimeException ex) {
            reportSuccess(exe, AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
            LoggingManager.getInstance().writeSystem("The file " + ServerConstants.DATA_DIR + m_networkName + ".graph doesn't exist.", ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, null);
            success = false;
        }
        LoggingManager.getInstance().writeTrace("Finishing graph loading.", ServerConstants.NETWORK, ServerConstants.LOAD_NETWORK, null);
        return success;
    }
