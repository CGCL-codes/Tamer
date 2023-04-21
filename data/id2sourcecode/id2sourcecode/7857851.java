    public void onNames(NamesEvent namesEvent) {
        System.out.println("onNames");
        assertEquals("onNames(): channel", "#sharktest", namesEvent.getChannel());
        assertEquals("onNames(): nick[0]", "alphaX234", namesEvent.getNicks()[0]);
        assertEquals("onNames(): nick[1]", "@Scurvy", namesEvent.getNicks()[1]);
        assertTrue("onNames(): done", !namesEvent.isLast());
    }
