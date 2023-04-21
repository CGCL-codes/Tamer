    private synchronized void recoverStableStorage() {
        ObjectInputStream in = null;
        try {
            File f = new File("stableStorage/" + toString() + ".bak");
            if (!f.exists()) {
                writeDebug("No stable storage found");
                return;
            }
            in = new ObjectInputStream(new FileInputStream(f));
            NodeStableStorage stableStorage = (NodeStableStorage) in.readObject();
            minPsns = stableStorage.minPsns;
            maxAcceptedProposals = stableStorage.maxAcceptedProposals;
        } catch (IOException e) {
            writeDebug("Problem reading from stable storage!", true);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            writeDebug("ClassNotFoundException while reading from stable storage!", true);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
            }
        }
    }
