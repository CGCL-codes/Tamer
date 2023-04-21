    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value == tree.getModel().getRoot()) {
            return this;
        }
        ChannelTree.Node node = (ChannelTree.Node) value;
        ChannelTree.NodeTypeEnum type = node.getType();
        setText(node.getName());
        if (type == ChannelTree.SERVER) {
            setIcon(SERVER_ICON);
        } else if (type == ChannelTree.FOLDER || type == ChannelTree.SOURCE || type == ChannelTree.PLUGIN) {
            if (expanded) {
                setIcon(FOLDER_OPEN_ICON);
            } else {
                setIcon(FOLDER_ICON);
            }
        } else if (type == ChannelTree.CHANNEL) {
            Channel channel = RBNBController.getInstance().getMetadataManager().getChannel(node.getFullName());
            String mime = channel.getMetadata("mime");
            if (mime.equals("application/octet-stream")) {
                setIcon(DATA_ICON);
            } else if (mime.equals("image/jpeg") || mime.equals("video/jpeg")) {
                setIcon(JPEG_ICON);
            } else if (mime.equals("text/plain")) {
                setIcon(TEXT_ICON);
            } else {
                setIcon(FILE_ICON);
            }
        }
        return this;
    }
