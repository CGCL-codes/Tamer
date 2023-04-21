    @Override
    public void doTag(final XMLOutput output) throws Exception {
        if ((myName == null) || (myName.length() == 0)) {
            throw new MissingAttributeException("name");
        }
        Channel channel;
        if (myProvider != null) {
            if (myValue != null) {
                throw new JellyException("Cannot specify both value and " + "provider attributes.");
            }
            String oldName = myNameInProvider;
            if ((oldName == null) || (oldName.length() == 0)) {
                oldName = myName;
            }
            try {
                channel = myProvider.getChannel(oldName);
            } catch (IllegalArgumentException e) {
                throw new JellyException("Provider does not define channel '" + oldName + "'.");
            }
        } else {
            channel = new BasicChannel(myValue);
        }
        PanelTag panelTag = (PanelTag) findAncestorWithClass(PanelTag.class);
        if (panelTag == null) {
            throw new JellyException("This tag must be nested within a " + "<panel> tag");
        }
        GenericPanel panel = (GenericPanel) panelTag.getComponent();
        panel.defineChannel(myName, channel);
        invokeBody(output);
        if (StringUtils.isNotEmpty(myVar)) {
            context.setVariable(myVar, channel);
        }
    }
