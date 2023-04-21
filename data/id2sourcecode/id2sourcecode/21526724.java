    public LocalGraphicalDetailedEditor(Composite parent, PilotStatusView detailedView) {
        try {
            setEditDomain(new DefaultEditDomain(this));
            this.detailedView = detailedView;
            Activator.addReference(ID, this);
            init(this.detailedView.getSite());
            createGraphicalViewer(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
