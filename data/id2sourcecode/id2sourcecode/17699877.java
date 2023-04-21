    public void performRequest(Request req) {
        if (req.getType().equals(REQ_OPEN)) {
            AddStubbedTablesOperation op = new AddStubbedTablesOperation(getDTableStub(), (GraphicalViewer) getViewer());
            op.execute(this);
        }
    }
