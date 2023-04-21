    private ActionListener getChannelAddHandler(final JTable channelTable) {
        return new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final AcceleratorSeq selectedSequence = DOCUMENT.getSelectedSequence();
                final AcceleratorSeq sequence = selectedSequence != null ? selectedSequence : DOCUMENT.getAccelerator();
                if (sequence != null) {
                    final List<AcceleratorNode> nodes = sequence.getAllInclusiveNodes(true);
                    final NodeChannelSelector channelRefSelector = NodeChannelSelector.getInstanceFromNodes(nodes, DOCUMENT.getAcceleratorWindow(), "Select Channels");
                    final List<NodeChannelRef> channelRefs = channelRefSelector.showDialog();
                    if (channelRefs != null && channelRefs.size() > 0) {
                        final int selectedRow = channelTable.getSelectedRow();
                        if (selectedRow < 0) {
                            MODEL.addChannelRefs(channelRefs);
                        } else {
                            MODEL.addChannelRefs(selectedRow, channelRefs);
                        }
                        CHANNEL_TABLE_MODEL.fireTableDataChanged();
                    }
                } else {
                    DOCUMENT.getAcceleratorWindow().displayError("PV Selection Error", "You must first select an accelerator sequence from which the PVs will be supplied.");
                }
            }
        };
    }
