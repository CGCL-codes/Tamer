    public void reconfig(QSP qsp, Node node) throws ConfigurationException {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        LogEvent evt = new LogEvent(qsp, "re-config-channel", name);
        try {
            ISOChannel channel = getChannel(name);
            byte[] digest = (byte[]) NameRegistrar.get(name + ".digest");
            byte[] previousDigest = new byte[digest.length];
            System.arraycopy(digest, 0, previousDigest, 0, digest.length);
            Properties props = ConfigUtil.addAttributes(node, attributeNames, null, evt);
            digest = (byte[]) props.get(ConfigUtil.DIGEST_PROPERTY);
            NameRegistrar.register(name + ".digest", digest);
            if (channel instanceof LogSource) ((LogSource) channel).setLogger(ConfigLogger.getLogger(node), ConfigLogger.getRealm(node));
            Configuration cfg = new SimpleConfiguration(ConfigUtil.addProperties(node, props, evt));
            if (channel instanceof ReConfigurable) {
                ((Configurable) channel).setConfiguration(cfg);
            }
            ISOPackager p = channel.getPackager();
            if (p instanceof ReConfigurable) {
                ((Configurable) p).setConfiguration(cfg);
            }
            if (Arrays.equals(previousDigest, digest)) {
                evt.addMessage("<unchanged/>");
                return;
            }
            evt.addMessage("<modified/>");
            if (cfg.get("timeout", null) != null) ConfigUtil.invoke(channel, "setTimeout", new Integer(cfg.getInt("timeout")));
            ConfigUtil.invoke(channel, "setHeader", cfg.get("header", null));
        } catch (NameRegistrar.NotFoundException e) {
            evt.addMessage(e);
        } finally {
            Logger.log(evt);
        }
    }
