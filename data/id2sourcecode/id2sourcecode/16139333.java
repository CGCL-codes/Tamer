    public DeleteAction(ScrollingGraphicalViewer view) {
        super("Delete");
        setToolTipText("Delete");
        setImageDescriptor(ImageDescriptor.createFromFile(ChisioMain.class, "icon/delete.gif"));
        this.viewer = view;
    }
