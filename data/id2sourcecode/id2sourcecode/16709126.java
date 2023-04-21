    @Test
    public void testAttributes() throws Exception {
        Executive exec = execs.iterator().next();
        assertEquals("Make sure the default slot count is set", 10, exec.getSlotCount());
        assertNotNull("Make sure the default queue is set", exec.getWorkQueue());
        assertNotNull("Make sure the thread pool is created", exec.getThreadPoolExecutor());
        assertEquals("Make sure the default cluster name is set", "ExecutiveTest" + counter, exec.getClusterName());
        assertNotNull("Make sure the channel factory is created", exec.getChannelFactory());
        URL url = new URL("http://myurl");
        exec.setChannelUrlProps(url);
        assertEquals("Make sure the property url is set", url, exec.getChannelUrlProps());
        String props = "String props";
        exec.setChannelStringProps(props);
        assertEquals("Make sure the property string is set", props, exec.getChannelStringProps());
        assertNotNull("Make sure the channel is created", exec.getChannel());
    }
