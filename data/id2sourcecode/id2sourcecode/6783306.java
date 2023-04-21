    public ForeignKeyAction(IWorkbenchPart part, Diagram diagram, GraphicalViewer viewer) {
        super(part);
        setId(ACTION_ID);
        setImageDescriptor(RMBenchPlugin.getImageDescriptor(ImageConstants.FK_OUT));
        setText(Messages.ForeignKey_Label);
        setToolTipText(Messages.ForeignKey_Label);
        setDescription(Messages.ForeignKey_Description);
        this.viewer = viewer;
        this.diagram = diagram;
        this.model = diagram.getModel();
    }
