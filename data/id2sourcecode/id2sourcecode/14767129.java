    public void setStatesMachines(StatesMachines statesMachines) {
        this.statesMachines = statesMachines;
        if (getGraphicalViewer() != null) {
            getGraphicalViewer().setContents(this.statesMachines);
        }
        logger.debug("L'editeur � un nouveau mod�le : " + statesMachines);
    }
