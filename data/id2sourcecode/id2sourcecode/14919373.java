            public void run() {
                InformaCGE theCGE;
                try {
                    theCGE = (InformaCGE) mapChannel2CGE(informaItem.getChannel());
                } catch (IllegalArgumentException e) {
                    log.fine("Item Retrieved via Hibernate for which there is no CGE." + informaItem);
                    return;
                }
                theCGE.addItem(informaItem);
                ArticleListModel aListModl = ArticleListModel.SINGLETON;
                ChannelGuideEntry selCGE = GlobalModel.SINGLETON.getSelectedCGE();
                log.finer("itemAdded: cge = " + theCGE + ", sel CGE = " + selCGE);
                if (selCGE == theCGE && theCGE.getArticleCount().intValue() == 1) GlobalController.SINGLETON.selectArticle(0);
                theCGE.reSortItems();
                theCGE.reloadCachedValues();
            }
