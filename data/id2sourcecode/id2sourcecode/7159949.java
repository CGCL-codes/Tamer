    private void handleDropComponent(DropTargetEvent evt, Component mcmp) {
        PIMDiagram diagram = getPIMDiagram();
        if (mcmp != null) {
            ComponentNode cmpNode = new ComponentNode();
            cmpNode.init(mcmp);
            Point loc = this.getGraphicalViewer().getControl().toControl(new Point(evt.x, evt.y));
            cmpNode.setLocation(loc.x, loc.y);
            cmpNode.setSize(0, 0);
            AddCommand addCmd = new AddCommand();
            addCmd.setChild(cmpNode);
            addCmd.setParent(diagram);
            this.getCommandStack().execute(addCmd);
            Command cmd = PIMEditPolicy.createRefreshRelationCommand(cmpNode, diagram);
            if (cmd != null && cmd.canExecute()) {
                this.getCommandStack().execute(cmd);
            }
        }
    }
