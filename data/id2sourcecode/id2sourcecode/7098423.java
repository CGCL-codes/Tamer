    @Test(timeout = 10000)
    public void testInvalidPassword() throws Throwable {
        System.out.println("---- testInvalidPassword() - start");
        client.connect(Inet4Address.getByName("localhost"), getPort());
        try {
            client.login(USER, "blah1");
            Assert.fail("Exception not detected");
        } catch (StoreException e) {
        }
        Assert.assertEquals(0, getCommandLog().size());
        client.disconnect();
        System.out.println("---- testInvalidPassword() - done");
    }
