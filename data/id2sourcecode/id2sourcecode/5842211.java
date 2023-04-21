    protected void setPoints(PlanPoint newSource, PlanPoint newTarget, boolean overwrite) throws IllegalArgumentException, PointsAlreadyConnectedException {
        setSource(newSource, overwrite);
        setTarget(newTarget, overwrite);
    }
