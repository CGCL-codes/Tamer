    @Test
    public void testLoad1() throws Exception {
        JAXBContext context = JAXBContext.newInstance("org.charvolant.tmsnet.resources.networks");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        this.network = (NetworkIdentification) unmarshaller.unmarshal(this.getClass().getResource("network1.xml"));
        Assert.assertEquals("Test", this.network.getName());
        Assert.assertEquals("large.png", this.network.getLargeIcon().toString());
        Assert.assertEquals("small.png", this.network.getSmallIcon().toString());
        Assert.assertEquals(4119, this.network.getOriginalNetworkId());
        Assert.assertEquals("Please", this.network.getOperator());
        Assert.assertTrue(this.network.getChannels().isEmpty());
    }
