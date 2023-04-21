    @Test
    public void testChannelNameChange() {
        Show show1 = ShowBuilder.example();
        Show show2 = ShowBuilder.example();
        getContext().setShow(show1);
        int e1 = inGroupTableListener.getEventCount();
        int e2 = notInGroupTableListener.getEventCount();
        getContext().getShow().getChannels().get(0).setName("A");
        assertEquals(inGroupTableListener.getEventCount(), e1 + 1);
        assertEquals(notInGroupTableListener.getEventCount(), e2 + 1);
        getContext().setShow(show2);
        e1 = inGroupTableListener.getEventCount();
        e2 = notInGroupTableListener.getEventCount();
        getContext().getShow().getChannels().get(0).setName("B");
        assertEquals(inGroupTableListener.getEventCount(), e1 + 1);
        assertEquals(notInGroupTableListener.getEventCount(), e2 + 1);
    }
