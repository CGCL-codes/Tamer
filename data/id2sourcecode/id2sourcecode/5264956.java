    @Test
    public void actionAllNotInGroup() {
        model.getActionNotInGroupAll().action();
        int channelCount = getContext().getShow().getChannels().size();
        ListSelectionModel selectionModel = model.getNotInGroupSelectionModel();
        assertEquals(selectionModel.getMinSelectionIndex(), 0);
        assertEquals(selectionModel.getMaxSelectionIndex(), channelCount - 1);
    }
