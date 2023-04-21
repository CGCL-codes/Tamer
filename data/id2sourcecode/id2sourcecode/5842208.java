    protected void setPoint(PlanPoint p, PlanPoint newPoint, boolean overwrite) throws PointsAlreadyConnectedException {
        if (source.equals(p)) setSource(newPoint, overwrite); else if (target.equals(p)) setTarget(newPoint, overwrite); else throw new IllegalArgumentException(ZLocalization.getSingleton().getString("ds.z.PlanPolygon.PointNotContainedInEdgeException"));
    }
