    @Test
    public void testSetValue() {
        FixtureDefinition definition = new FixtureDefinition();
        definition.addAttribute("name", "1");
        fixtureHolder.setValue(definition);
        model.setValueAt("name2", 0, AttributesTableModel.COLUMN_NAME);
        assertEquals(definition.getAttribute(0).getName(), "name2");
        model.setValueAt("2", 0, AttributesTableModel.COLUMN_CHANNELS);
        assertEquals(definition.getAttribute(0).getChannels(), "2");
    }
