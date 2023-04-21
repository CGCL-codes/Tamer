    @Test(timeout = 10000)
    public void testLogin() throws Exception {
        System.out.println("---- testLogin() - start");
        client.connect(Inet4Address.getByName("localhost"), getPort());
        client.login(USER, PASSWORD);
        Assert.assertEquals(0, getCommandLog().size());
        System.out.println("---- testLogin() - done");
    }
