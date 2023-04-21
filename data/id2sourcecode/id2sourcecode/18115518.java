    public void testLoadConfig_InputStream() throws IOException {
        log.info(" >> testLoadConfig_InputStream");
        Class clazz = this.getClass();
        final String filename = clazz.getSimpleName() + ".pushlog";
        log.info("config file: " + filename);
        InputStream is = clazz.getResourceAsStream(filename);
        Collection<ChannelConfig> config = MultiStreamWatch.loadConfig(is);
        assertTrue(config.size() == 4);
        Map<String, ChannelConfig> name2config = new HashMap<String, ChannelConfig>();
        for (ChannelConfig entry : config) {
            name2config.put(entry.getId(), entry);
        }
        assertTrue(name2config.containsKey("oneMoreLine"));
        ChannelConfig entry = name2config.get("oneMoreLine");
        assertEquals(entry.getChannelOpener().getClass(), FileChannelOpener.class);
        assertEquals(".*", entry.getRegex());
        assertEquals(new File("oneMoreLine.filename"), entry.getFile());
        assertEquals("${0}", entry.getFormat());
        assertTrue(name2config.containsKey("mailSend"));
        entry = name2config.get("mailSend");
        assertEquals(".*(status=send).*", entry.getRegex());
        assertEquals(new File("/var/log/mail"), entry.getFile());
        assertEquals("email(${1})", entry.getFormat());
        assertTrue(name2config.containsKey("mailReceived"));
        entry = name2config.get("mailReceived");
        assertEquals(".*status=send.*", entry.getRegex());
        assertEquals(new File("/var/log/mail"), entry.getFile());
        assertEquals("${0}", entry.getFormat());
        assertTrue(name2config.containsKey("urlStream"));
        entry = name2config.get("urlStream");
        assertEquals(entry.getChannelOpener().getClass(), URLStreamOpener.class);
    }
