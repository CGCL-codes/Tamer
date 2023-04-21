    @Test
    public void actionNoneNotInGroup() {
        int channelCount = getContext().getShow().getChannels().size();
        selectChannelsNotInGroup(0, channelCount - 1);
        model.getActionNotInGroupNone().action();
        ListSelectionModel selectionModel = model.getNotInGroupSelectionModel();
        assertEquals(selectionModel.getMinSelectionIndex(), -1);
        assertEquals(selectionModel.getMaxSelectionIndex(), -1);
    }
