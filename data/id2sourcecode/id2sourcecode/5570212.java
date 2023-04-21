    @Test
    public void changedChannelName() {
        context.getShow().getChannels().get(0).setName("New");
        assertEquals(model.getValueAt(3, COLUMN_NAME), "New");
        assertEquals(events.get(0).getColumn(), TableModelEvent.ALL_COLUMNS);
    }
