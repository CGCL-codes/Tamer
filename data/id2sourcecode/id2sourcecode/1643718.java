    public LocalGraphicalDetailedEditor(Composite parent, DraftHexagonView detailedView) {
        try {
            setEditDomain(new DefaultEditDomain(this));
            this.detailedView = detailedView;
            init(this.detailedView.getSite());
            createGraphicalViewer(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
