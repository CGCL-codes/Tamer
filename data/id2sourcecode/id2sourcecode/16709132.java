    @Test(timeout = 60 * 1000)
    public void testHandleBadRequests() throws Throwable {
        for (Executive e : execs) {
            e.start();
        }
        resultLatch = new CountDownLatch(10);
        ProcessDefinition[] pds = new ProcessDefinition[(int) resultLatch.getCount()];
        for (int i = 0; i < pds.length; i++) {
            pds[i] = new ProcessDefinition(new ProcessId(dispatcher.getChannel().getLocalAddress(), "id" + i));
            callableMap.put(pds[i].getProcessId(), new MockBadCallable());
        }
        RspList[] lists = new RspList[pds.length];
        for (int i = 0; i < pds.length; i++) {
            MethodCall method = new MethodCall(JatherHandlerAdapter.RECEIVE_REQUEST, new Object[] { pds[i] });
            lists[i] = dispatcher.callRemoteMethods(null, method, GroupRequest.GET_ALL, 0);
        }
        for (int i = 0; i < lists.length; i++) {
            assertEquals("Make sure all execs were called", execs.size() + 1, lists[i].values().size());
            for (Rsp r : lists[i].values()) {
                assertTrue("Make sure all calls were received", r.wasReceived());
            }
        }
        resultLatch.await();
        for (ProcessDefinition pd : pds) {
            assertNotNull("Make sure the process result is collected:" + pd + ":" + resultMap, resultMap.get(pd.getProcessId()));
            assertNotNull("Make sure the process exception is present", resultMap.get(pd.getProcessId()).getCallableException());
        }
    }
