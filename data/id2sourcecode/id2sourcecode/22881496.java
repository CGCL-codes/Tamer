    public void loadFromDisk(File inFile, AbstractExecution progress, double percentage) throws Exception {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(inFile));
            m_numberOfVertices = in.readInt();
            m_pathBetweeness = (double[][]) in.readObject();
            updateLoadProgress(progress, percentage);
            m_distanceMatrix = (double[][]) in.readObject();
            updateLoadProgress(progress, percentage);
            m_sigma = (double[][]) in.readObject();
            m_deltaDot = (double[][]) in.readObject();
            m_communicationWeights = (AbsTrafficMatrix) in.readObject();
            m_totalCommunicationWeight = in.readDouble();
            updateLoadProgress(progress, percentage);
            m_routingTable = (FastList[][]) in.readObject();
            updateLoadProgress(progress, percentage);
            SerializableGraphRepresentation serGraph = (SerializableGraphRepresentation) in.readObject();
            m_graph = serGraph.getGraph();
        } catch (ClassNotFoundException ex) {
            logger.writeSystem("A ClassNotFoundException has occured while trying to read the DataWorkshop from file " + inFile.getName(), "DataWorkshop", "loadFromDisk", ex);
            throw new Exception("A ClassNotFoundException has occured while trying to read the DataWorkshop from file " + inFile.getName() + "\n" + ex.getMessage());
        } catch (IOException ex) {
            logger.writeSystem("An IOException has occured while trying to read the DataWorkshop from file " + inFile.getName(), "DataWorkshop", "loadFromDisk", ex);
            throw new Exception("An IOException has occured while trying to read the DataWorkshop from file " + inFile.getName() + "\n" + ex.getMessage() + "\n" + ex.getStackTrace());
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
                logger.writeSystem("An IOException has occured while trying to close the input stream after reading the file: " + inFile.getName(), "DataWorkshop", "loadFromDisk", ex);
                throw new Exception("An IOException has occured while trying to close the input stream after reading the file: " + inFile.getName() + "\n" + ex.getMessage() + "\n" + ex.getStackTrace());
            }
        }
    }
