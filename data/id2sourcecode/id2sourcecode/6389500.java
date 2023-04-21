    void actionMarkCategoryRead(Category selectedCategory, boolean updateSysTrayStatus) {
        if (selectedCategory == null) return;
        Enumeration subCategories = selectedCategory.getSubCategories().elements();
        while (subCategories.hasMoreElements()) actionMarkCategoryRead((Category) subCategories.nextElement(), false);
        Enumeration allFavoritesIt = selectedCategory.getFavorites().elements();
        while (allFavoritesIt.hasMoreElements()) {
            Favorite rssOwlFavorite = (Favorite) allFavoritesIt.nextElement();
            actionMarkFavoriteRead(rssOwlFavorite, false);
        }
        CTabItem tabItem = rssOwlNewsTabFolder.getTabItem(selectedCategory);
        if (tabItem != null) {
            Table newsTable = ((TabItemData) tabItem.getData()).getNewsHeaderTable();
            if (WidgetShop.isset(newsTable)) NewsTable.markAllRead(newsTable);
            Channel aggregationChannel = ((TabItemData) tabItem.getData()).getChannel();
            Enumeration newsItems = aggregationChannel.getItems().elements();
            while (newsItems.hasMoreElements()) {
                NewsItem newsItem = (NewsItem) newsItems.nextElement();
                newsItem.setRead(true);
            }
            rssOwlNewsTabFolder.updateTabItemStatus(tabItem);
        }
        if (GlobalSettings.useSystemTray() && GlobalSettings.showSystrayIcon && updateSysTrayStatus) {
            boolean treeHasUnreadFavs = rssOwlFavoritesTree.getTreeHasUnreadFavs();
            rssOwlGui.getRSSOwlSystray().setTrayItemState(treeHasUnreadFavs);
        }
    }
