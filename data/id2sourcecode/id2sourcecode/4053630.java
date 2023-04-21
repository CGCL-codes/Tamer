    private String getChannelName(Long id) {
        for (Iterator it = channel.getTree().iterator(); it.hasNext(); ) {
            TreeItemUI item = (TreeItemUI) it.next();
            ChannelDto temp = (ChannelDto) item.getTreeItem().getUserObject();
            if (temp.getId().equals(id)) {
                StringBuffer sb = new StringBuffer();
                channel.getAllChannelName(sb, item.getTreeItem());
                return sb.toString();
            }
        }
        return null;
    }
