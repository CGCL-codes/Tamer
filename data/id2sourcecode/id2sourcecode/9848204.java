    protected Command createLayoutCommand() {
        return new Command() {

            public void execute() {
                editor.setRelayout(true);
                editor.setLayoutMode(layoutMode);
                ((GraphicalEditPart) editor.getGraphicalViewer().getContents()).getFigure().revalidate();
            }
        };
    }
