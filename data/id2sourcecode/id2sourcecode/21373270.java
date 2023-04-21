    @Test
    public void channelNameChange() {
        final Holder<TableModelEvent> holder = new Holder<TableModelEvent>();
        ShowContext context = new ShowContext();
        Show show = ShowBuilder.example();
        context.setShow(show);
        PatchChannelTableModel model = new PatchChannelTableModel(context);
        model.addTableModelListener(new TableModelListener() {

            public void tableChanged(final TableModelEvent e) {
                holder.setValue(e);
            }
        });
        Channel channel = context.getShow().getChannels().get(5);
        channel.setName("changed");
        TableModelEvent e = holder.getValue();
        assertEquals(e.getColumn(), 1);
        assertEquals(e.getFirstRow(), 5);
        assertEquals(e.getLastRow(), 5);
        assertEquals(e.getType(), TableModelEvent.UPDATE);
    }
