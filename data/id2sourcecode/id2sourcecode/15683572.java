    public void rollbackChangesLocally() {
        if (getChannel() instanceof DataDomain) {
            rollbackChanges();
        } else {
            throw new CayenneRuntimeException("Implementation pending.");
        }
    }
