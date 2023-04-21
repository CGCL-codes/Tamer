    @Test(timeout = 10000)
    public void testInvalidUsername() throws Throwable {
        System.out.println("---- testInvalidUsername() - start");
        client.connect(Inet4Address.getByName("localhost"), getPort());
        try {
            client.login("blah", PASSWORD);
            Assert.fail("Exception not detected");
        } catch (StoreException e) {
        }
        Assert.assertEquals(0, getCommandLog().size());
        client.disconnect();
        System.out.println("---- testInvalidUsername() - done");
    }
