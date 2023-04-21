    @Test
    public void testLoad4() throws Exception {
        JAXBContext context = JAXBContext.newInstance("org.charvolant.tmsnet.resources.networks");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ChannelIdentification info;
        this.network = (NetworkIdentification) unmarshaller.unmarshal(this.getClass().getResource("network4.xml"));
        info = this.network.getChannel(2);
        Assert.assertNotNull(info);
        Assert.assertEquals("Channel1", info.getName());
        Assert.assertEquals(this.network.getLargeIcon(), info.getLargeIcon());
        Assert.assertEquals(this.network.getSmallIcon(), info.getSmallIcon());
        info = this.network.getChannel(20);
        Assert.assertNotNull(info);
        Assert.assertEquals("ChannelE1", info.getName());
        Assert.assertEquals(this.network.getLargeIcon(), info.getLargeIcon());
        Assert.assertEquals(this.network.getSmallIcon(), info.getSmallIcon());
    }
