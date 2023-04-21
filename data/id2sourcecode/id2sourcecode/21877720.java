    public void onList(ListEvent listEvent) {
        System.out.println("onList");
        assertEquals("onList(): channel", "#sun", listEvent.getChannel());
        assertEquals("onList(): count", 17, listEvent.getVisibleNickCount());
        assertEquals("onList(): topic", "Send criticisms of new sun.com website to magellan-questions@sun.com - like http://wwws.sun.com/software/star/gnome/jtf/ is grossly wrong ?", listEvent.getTopic());
        assertTrue("onList(): done", !listEvent.isLast());
    }
