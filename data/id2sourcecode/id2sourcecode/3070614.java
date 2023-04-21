    @Test
    public void testLoad3() throws Exception {
        JAXBContext context = JAXBContext.newInstance("org.charvolant.tmsnet.resources.networks");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ChannelIdentification info;
        this.network = (NetworkIdentification) unmarshaller.unmarshal(this.getClass().getResource("network3.xml"));
        Assert.assertEquals(2, this.network.getChannels().size());
        info = this.network.getChannels().get(2);
        Assert.assertNotNull(info);
        Assert.assertEquals("Channel1", info.getName());
        Assert.assertEquals(this.network.getLargeIcon(), info.getLargeIcon());
        Assert.assertEquals(this.network.getSmallIcon(), info.getSmallIcon());
        info = this.network.getChannels().get(20);
        Assert.assertNotNull(info);
        Assert.assertEquals("Channel2", info.getName());
        Assert.assertEquals(this.network.getLargeIcon(), info.getLargeIcon());
        Assert.assertFalse(this.network.getSmallIcon().equals(info.getSmallIcon()));
    }
