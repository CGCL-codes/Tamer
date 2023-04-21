    protected void setTarget(PlanPoint newTarget, boolean overwrite) throws PointsAlreadyConnectedException {
        if (!overwrite && newTarget != target && newTarget.getPreviousEdge() != null) throw new PointsAlreadyConnectedException(newTarget, ZLocalization.getSingleton().getString("ds.z.TargetAlreadyConnectedException"));
        if (target != null) {
            target.setPreviousEdge(null);
        }
        target = newTarget;
        newTarget.setPreviousEdge(this);
    }
