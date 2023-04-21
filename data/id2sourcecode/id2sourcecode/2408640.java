    @Test
    public void testLoad() throws Exception {
        JAXBContext context = JAXBContext.newInstance("org.charvolant.tmsnet.resources.networks");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        NetworkIdentification info;
        ChannelIdentification channel;
        this.map = (NetworkMap) unmarshaller.unmarshal(this.getClass().getResource("map1.xml"));
        Assert.assertEquals("1.0", this.map.getVersion().toString());
        Assert.assertEquals(new Date(0), this.map.getUpdated());
        Assert.assertEquals(2, this.map.getNetworks().size());
        info = this.map.getNetworks().get(4119);
        Assert.assertNotNull(info);
        Assert.assertEquals("Test", info.getName());
        Assert.assertEquals(this.map.getLargeIcon(), info.getLargeIcon());
        Assert.assertEquals(this.map.getSmallIcon(), info.getSmallIcon());
        channel = info.getChannel(2);
        Assert.assertEquals(info, channel.getParent());
        channel = info.getChannel(20);
        Assert.assertEquals(info, channel.getParent());
        info = this.map.getNetworks().get(4120);
        Assert.assertNotNull(info);
        Assert.assertEquals("AnotherTest", info.getName());
        Assert.assertFalse(this.map.getLargeIcon().equals(info.getLargeIcon()));
        Assert.assertFalse(this.map.getSmallIcon().equals(info.getSmallIcon()));
    }
