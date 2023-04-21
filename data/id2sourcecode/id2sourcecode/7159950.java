    private void handleDropSubsystem(DropTargetEvent evt, Subsystem msub) {
        PIMDiagram diagram = getPIMDiagram();
        if (msub != null) {
            SubsystemNode subNode = new SubsystemNode();
            subNode.init(msub);
            Point loc = this.getGraphicalViewer().getControl().toControl(new Point(evt.x, evt.y));
            subNode.setLocation(loc.x, loc.y);
            Hashtable subsInDiagram = new Hashtable();
            for (Iterator i = diagram.getChildren().iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (o instanceof SubsystemNode) {
                    SubsystemNode node = (SubsystemNode) o;
                    Subsystem sub = node.getMsubsystem();
                    if (sub != null) subsInDiagram.put(sub, node);
                    if (msub == sub) {
                        evt.detail = DND.DROP_NONE;
                        return;
                    }
                }
            }
            subNode.setSize(0, 0);
            AddCommand cmd = new AddCommand();
            cmd.setChild(subNode);
            cmd.setParent(diagram);
            this.getCommandStack().execute(cmd);
        }
    }
