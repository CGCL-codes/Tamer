    @Test
    public void actionNotInAnyGroup() {
        model.getNotInAnyGroup().setValue(true);
        addGroup();
        selectGroup(0);
        addChannels(0, 2);
        addGroup();
        selectGroup(1);
        addChannels(0, 2);
        Channel[] channels1 = getGroup(0).getChannels();
        Channel[] channels2 = getGroup(1).getChannels();
        assertEquals(channels1.length, 3);
        assertEquals(channels2.length, 3);
        assertEquals(channels1[0].getName(), "Channel 1");
        assertEquals(channels1[1].getName(), "Channel 2");
        assertEquals(channels1[2].getName(), "Channel 3");
        assertEquals(channels2[0].getName(), "Channel 4");
        assertEquals(channels2[1].getName(), "Channel 5");
        assertEquals(channels2[2].getName(), "Channel 6");
        int channelCount = getContext().getShow().getChannels().size();
        model.getNotInAnyGroup().setValue(false);
        assertEquals(model.getNotInGroupTableModel().getRowCount(), channelCount - 3);
        int before = notInGroupTableListener.events.size();
        model.getActionNotInAnyGroup().action();
        assertEquals(notInGroupTableListener.events.size(), before + 1);
        model.getNotInAnyGroup().setValue(true);
        assertEquals(model.getNotInGroupTableModel().getRowCount(), channelCount - 6);
        before = notInGroupTableListener.events.size();
        model.getActionNotInAnyGroup().action();
        assertEquals(notInGroupTableListener.events.size(), before + 1);
    }
