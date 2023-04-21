    @Test
    public void setChannelName() {
        model.setValueAt("New", 3, COLUMN_NAME);
        assertEquals(context.getShow().getChannels().get(0).getName(), "New");
    }
