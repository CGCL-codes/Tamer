    public void testAttributeOrder() throws Exception {
        Persister persister = new Persister();
        ServerDeployment deployment = new ServerDeployment();
        Server primary = new Server("host1.domain.com", 3487);
        Server secondary = new Server("host2.domain.com", 3487);
        StringWriter writer = new StringWriter();
        deployment.setPrimary(primary);
        deployment.setSecondary(secondary);
        deployment.setOS(OperatingSystem.LINUX);
        deployment.setProcessor(Processor.INTEL);
        persister.write(deployment, writer);
        System.out.println(writer.toString());
        ServerDeployment recovered = persister.read(ServerDeployment.class, writer.toString());
        assertEquals(recovered.getOS(), deployment.getOS());
        assertEquals(recovered.getProcessor(), deployment.getProcessor());
        assertEquals(recovered.getPrimary().getHost(), deployment.getPrimary().getHost());
        assertEquals(recovered.getPrimary().getPort(), deployment.getPrimary().getPort());
        assertEquals(recovered.getSecondary().getHost(), deployment.getSecondary().getHost());
        assertEquals(recovered.getSecondary().getPort(), deployment.getSecondary().getPort());
        assertElementHasAttribute(writer.toString(), "/serverDeployment/group", "os", "LINUX");
        assertElementHasAttribute(writer.toString(), "/serverDeployment/group", "processor", "INTEL");
        assertElementHasAttribute(writer.toString(), "/serverDeployment/group/server[1]/details/primary", "host", "host1.domain.com");
        assertElementHasAttribute(writer.toString(), "/serverDeployment/group/server[1]/details/primary", "port", "3487");
        assertElementHasAttribute(writer.toString(), "/serverDeployment/group/server[2]/details/secondary", "host", "host2.domain.com");
        assertElementHasAttribute(writer.toString(), "/serverDeployment/group/server[2]/details/secondary", "port", "3487");
        validate(deployment, persister);
    }
