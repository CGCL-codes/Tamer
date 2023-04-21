    @Test
    public void testGetChannelNumber() {
        FixtureDefinition definition = new FixtureDefinition();
        definition.addAttribute("Intensity", "1");
        fixture = new Fixture(definition, "", 11);
        assertEquals(fixture.getChannelNumber("Intensity"), 11);
        assertEquals(fixture.getChannelNumber("Gobo"), 0);
    }
