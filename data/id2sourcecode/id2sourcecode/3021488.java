    public ToggleAction(String name, GraphicalViewer viewer, String type, Visibility visibility) {
        super(name, IAction.AS_CHECK_BOX, viewer);
        this.stack = viewer.getEditDomain().getCommandStack();
        this.type = type;
        this.visibility = visibility;
        setChecked(true);
    }
