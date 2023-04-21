    private void handleDropMetaClass(DropTargetEvent evt, MetaClass mclass) {
        PIMDiagram diagram = getPIMDiagram();
        if (mclass != null) {
            ClassNode clsNode = new ClassNode();
            clsNode.init(mclass);
            Point loc = this.getGraphicalViewer().getControl().toControl(new Point(evt.x, evt.y));
            clsNode.setLocation(loc.x, loc.y);
            clsNode.setSize(0, 0);
            AddCommand addcmd = new AddCommand();
            addcmd.setChild(clsNode);
            addcmd.setParent(diagram);
            this.getCommandStack().execute(addcmd);
            Command cmd = PIMEditPolicy.createRefreshRelationCommand(clsNode, diagram);
            if (cmd != null && cmd.canExecute()) {
                this.getCommandStack().execute(cmd);
            }
        }
    }
