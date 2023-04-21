    protected void setSource(PlanPoint newSource, boolean overwrite) throws PointsAlreadyConnectedException {
        if (!overwrite && newSource != source && newSource.getNextEdge() != null) throw new PointsAlreadyConnectedException(newSource, ZLocalization.getSingleton().getString("ds.z.SourceAlreadyConnectedException"));
        if (source != null) {
            source.setNextEdge(null);
        }
        source = newSource;
        newSource.setNextEdge(this);
    }
