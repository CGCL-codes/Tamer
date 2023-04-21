    private void handleDropPackage(DropTargetEvent evt, Package mpkg) {
        PIMDiagram diagram = getPIMDiagram();
        if (mpkg != null) {
            PackageNode pkgNode = new PackageNode();
            pkgNode.init(mpkg);
            Point loc = this.getGraphicalViewer().getControl().toControl(new Point(evt.x, evt.y));
            pkgNode.setLocation(loc.x, loc.y);
            Hashtable pkgsInDiagram = new Hashtable();
            for (Iterator i = diagram.getChildren().iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (o instanceof PackageNode) {
                    PackageNode node = (PackageNode) o;
                    com.metanology.mde.core.metaModel.Package pkg = node.getMpackage();
                    if (pkg != null) pkgsInDiagram.put(pkg, node);
                    if (mpkg == pkg) {
                        evt.detail = DND.DROP_NONE;
                        return;
                    }
                }
            }
            pkgNode.setSize(0, 0);
            AddCommand command = new AddCommand();
            command.setChild(pkgNode);
            command.setParent(diagram);
            this.getCommandStack().execute(command);
        }
    }
