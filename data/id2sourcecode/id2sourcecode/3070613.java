    @Test
    public void testLoad2() throws Exception {
        JAXBContext context = JAXBContext.newInstance("org.charvolant.tmsnet.resources.networks");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ChannelIdentification info;
        this.network = (NetworkIdentification) unmarshaller.unmarshal(this.getClass().getResource("network2.xml"));
        Assert.assertEquals("Test", this.network.getName());
        Assert.assertEquals("large.png", this.network.getLargeIcon().toString());
        Assert.assertEquals("small.png", this.network.getSmallIcon().toString());
        Assert.assertEquals(4119, this.network.getOriginalNetworkId());
        Assert.assertEquals(Arrays.asList(4120), this.network.getNetworkIds());
        Assert.assertEquals("Please", this.network.getOperator());
        Assert.assertEquals(1, this.network.getChannels().size());
        info = this.network.getChannels().get(2);
        Assert.assertNotNull(info);
        Assert.assertEquals("Channel1", info.getName());
    }
