    public GraphicalDetailedEditor(Composite parent, GraphicalDetailedView detailedView) {
        try {
            setEditDomain(new DefaultEditDomain(this));
            this.detailedView = detailedView;
            Activator.addReference(ID, this);
            init(this.detailedView.getSite());
            this.createGraphicalViewer(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
