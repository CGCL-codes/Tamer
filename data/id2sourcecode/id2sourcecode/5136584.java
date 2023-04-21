    public void testInterpolate() {
        ModelCalculator calculator = new ModelCalculator();
        ArrayList<XSection> xsections = channel.getXsections();
        ArrayList<XSection> interpolatedXSections = calculator.calculateZInterpolatedXSections(channel);
        assertNotNull(interpolatedXSections);
        assertEquals(xsections.size(), interpolatedXSections.size());
        XSection xSection = xsections.get(0);
        XSection ixSection = interpolatedXSections.get(0);
        assertEquals(xSection.getChannelId(), ixSection.getChannelId());
        assertEquals(xSection.getDistance(), ixSection.getDistance());
    }
