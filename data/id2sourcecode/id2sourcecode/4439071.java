        public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
            Point location = dropTargetDropEvent.getLocation();
            TreePath path = getPathForLocation(location.x, location.y);
            DefaultMutableTreeNode node;
            if (path == null) {
                node = currentPanelTab.getLastSelectedNode();
            } else {
                node = (DefaultMutableTreeNode) path.getLastPathComponent();
            }
            if (node.isLeaf() && !node.isRoot() && !((File) node.getUserObject()).isDirectory()) {
                node = (DefaultMutableTreeNode) node.getParent();
                System.out.println("Taking parent of leaf...");
            }
            try {
                Transferable tr = dropTargetDropEvent.getTransferable();
                if ((tr.getTransferDataFlavors()[0]).equals(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR)) {
                    dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    Object userObject = tr.getTransferData(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR);
                    rmvSrcElement();
                    addElement(node, userObject);
                    dropTargetDropEvent.dropComplete(true);
                } else if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List ListedeFile = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                    Iterator iterator = ListedeFile.iterator();
                    File[] fileTab = new File[ListedeFile.size()];
                    int i = 0;
                    while (iterator.hasNext()) {
                        fileTab[i++] = (File) iterator.next();
                    }
                    currentPanelTab.ajouterFichiers(fileTab, node);
                    dropTargetDropEvent.dropComplete(true);
                } else {
                    System.err.println("Rejected");
                    dropTargetDropEvent.rejectDrop();
                }
            } catch (IOException io) {
                io.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            }
        }
