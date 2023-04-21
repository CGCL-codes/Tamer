    public void registerActions() {
        ActionManager.register(SELECT_ACTIVE, selectGuideAction(ChannelGuideSet.ACTIVE));
        ActionManager.register(SELECT_BETS, selectGuideAction(ChannelGuideSet.BESTBETS));
        ActionManager.register(SELECT_FAVORITES, selectGuideAction(ChannelGuideSet.FAVORITE));
        ActionManager.register(SELECT_SUGGESTED, selectGuideAction(ChannelGuideSet.SUGGESTED));
        ActionManager.register(SELECT_GUIDE, selectGuideAction(ChannelGuideSet.GUIDE));
        ActionManager.register(MARK_READ, logAction("Command: Mark as Read"));
        ActionManager.register(MARK_UNREAD, logAction("Command: Mark as Unread"));
        ActionManager.register(SORT_CHANNELS, logAction("Command: Sort Channels"));
        ActionManager.register(SHOW_NEXTUNREAD, logAction("Command: Show next unread"));
        ActionManager.register(MARK_ALLREAD, logAction("Command: Mark all read"));
        ActionManager.register(MARK_FAVORITE, logAction("Command: Mark as Favorite"));
        ActionManager.register(SORT_ARTICLES, logAction("Command: Sort Articles"));
        ActionManager.register(DEL_CHANNEL, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                cmdSimpleDelChannel(GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide(), GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide().selectedCGE());
            }
        });
        ActionManager.register(ADD_CHANNEL, new AbstractAction() {

            ValueHolder name = new ValueHolder();

            ValueHolder URL = new ValueHolder();

            public void actionPerformed(ActionEvent e) {
                new AddChannelDialog(getMainframe(), name, URL).open();
                log.config("Command: Add Channel " + name.getValue() + ", at " + URL.getValue());
                if (name.getValue() != null && URL.getValue() != null) {
                    cmdAddPersistentChannel(GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide(), GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide().selectedCGE(), (String) URL.getValue(), (String) name.getValue());
                }
                ;
            }
        });
        ActionManager.register(APPEND_CHANNEL, new AbstractAction() {

            ValueHolder name = new ValueHolder();

            ValueHolder URL = new ValueHolder();

            public void actionPerformed(ActionEvent e) {
                new AddChannelDialog(getMainframe(), name, URL).open();
                log.config("Command: Add Channel " + name.getValue() + ", at " + URL.getValue());
                if (name.getValue() != null && URL.getValue() != null) {
                    cmdSimpleAddChannel(GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide(), null, (String) URL.getValue(), (String) name.getValue());
                }
                ;
            }
        });
        ActionManager.register(CHANNEL_PROPERTIES, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                new ChannelPropertiesDialog(getMainframe(), GlobalModel.SINGLETON.getChannelGuideSet().selectedGuide().selectedCGE()).open();
            }
        });
        ActionManager.register(ARTICLE_PROPERTIES, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                new ArticlePropertiesDialog(getMainframe(), GlobalModel.SINGLETON.getSelectedArticle()).open();
            }
        });
    }
