    private boolean installNewManager(RepositoryManager newManager, String newManagerID) {
        if (newManagerID.equals(managerID)) {
            writeln("Already connected to " + managerID);
            return true;
        }
        try {
            newManager.initialize();
            disconnect(false);
            manager = newManager;
            managerID = newManagerID;
            writeln("Connected to " + managerID);
            return true;
        } catch (RepositoryException e) {
            writeError(e.getMessage());
            logger.error("Failed to install new manager", e);
            return false;
        }
    }
