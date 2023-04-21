    @SuppressWarnings("unchecked")
    public void httpGet(Invocation inv) throws DesignerException, ProgrammerException, UserException, DeployerException {
        Document doc = MonadUtils.newSourceDocument();
        Element source = doc.getDocumentElement();
        Element channelsElement = (Element) toXML(doc);
        source.appendChild(channelsElement);
        channelsElement.appendChild(supply.getXML(new XmlTree("organization"), doc));
        for (Channel channel : supply.getChannels()) {
            channelsElement.appendChild(channel.toXML(doc));
        }
        inv.sendOk(doc);
    }
