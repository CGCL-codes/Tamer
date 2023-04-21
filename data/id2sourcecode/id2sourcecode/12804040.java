    public static double[][] loadWeightsFromFile(String commWeightsFileName, int numOfVertices) {
        double[][] communicationWeights = new double[numOfVertices][numOfVertices];
        File file = new File(commWeightsFileName);
        ;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            communicationWeights = (double[][]) in.readObject();
        } catch (ClassNotFoundException ex) {
            LoggingManager.getInstance().writeSystem("A ClassNotFoundException has occured while trying to read the weights from file " + file.getName() + ":\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "analyzeFile", ex);
        } catch (FileNotFoundException ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "loadWeights", ex);
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem("An exception has occured while reading the weights. Check correctness of the .wc file format.\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "loadWeights", ex);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
                LoggingManager.getInstance().writeSystem("An exception has occured while closing FileInputStream to: " + file.getAbsoluteFile() + "\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "loadWeights", ex);
            }
        }
        return communicationWeights;
    }
