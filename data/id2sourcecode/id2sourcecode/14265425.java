    private String string() {
        PatchChannelTableModel m = model.getChannelTableModel();
        StringBuilder b = new StringBuilder();
        for (int row : selectedRows) {
            Channel channel = m.getChannel(row);
            b.append(channel.getId() + 1);
            b.append('\t');
            b.append(channel.getName());
            b.append('\n');
        }
        return b.toString();
    }
