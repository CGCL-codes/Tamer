        public void actionPerformed(ActionEvent e) {
            try {
                Model rssModel = new ModelMem();
                Resource channel = new ResourceImpl(pluginUI.getChannelURI());
                rssModel.add(new StatementImpl(channel, RDF.type, RSS.channel));
                rssModel.add(new StatementImpl(channel, RSS.link, new LiteralImpl(pluginUI.getChannelLink())));
                rssModel.add(new StatementImpl(channel, RSS.description, new LiteralImpl(pluginUI.getChannelDescription())));
                rssModel.add(new StatementImpl(channel, RSS.title, new LiteralImpl(pluginUI.getChannelTitle())));
                rssModel.add(new StatementImpl(channel, DC.language, new LiteralImpl(pluginUI.getChannelLanguage())));
                Resource anon = new ResourceImpl(new AnonId());
                rssModel.add(new StatementImpl(channel, RSS.items, anon));
                rssModel.add(new StatementImpl(anon, RDF.type, RDF.Seq));
                int itemCnt = 0;
                DOMParser parser = new DOMParser();
                for (int i = 0; i < tblModel.getRowCount(); i++) {
                    Boolean bool = (Boolean) tblModel.getValueAt(i, 0);
                    if (bool.booleanValue()) {
                        URI uri = (URI) tblModel.getValueAt(i, 1);
                        ItemInfo info = (ItemInfo) itemInfoMap.get(uri);
                        parser.parse(uri.toString());
                        storeItemInfo(parser.getDocument().getDocumentElement(), info);
                        Resource item = new ResourceImpl(info.getURI().toString());
                        rssModel.add(new StatementImpl(anon, RDF.li(itemCnt++), item));
                        rssModel.add(new StatementImpl(item, RDF.type, RSS.item));
                        rssModel.add(new StatementImpl(item, RSS.link, new LiteralImpl(info.getLink())));
                        rssModel.add(new StatementImpl(item, RSS.description, new LiteralImpl(info.getDescription())));
                        rssModel.add(new StatementImpl(item, RSS.title, new LiteralImpl(info.getTitle())));
                    }
                }
                replaceRDFModel(rssModel);
            } catch (RDFException rdfex) {
                rdfex.printStackTrace();
            } catch (SAXException rdfex) {
            } catch (IOException rdfex) {
            }
            frame.setVisible(false);
        }
