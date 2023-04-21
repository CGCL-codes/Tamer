    public void actionMarkAllCategoriesRead() {
        Category rootCategory = Category.getRootCategory();
        CTabItem tabItem = rssOwlNewsTabFolder.getTabItem(rootCategory);
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
        Hashtable subCategories = rootCategory.getSubCategories();
        Enumeration elements = subCategories.elements();
        while (elements.hasMoreElements()) actionMarkCategoryRead((Category) elements.nextElement(), false);
        if (GlobalSettings.useSystemTray() && GlobalSettings.showSystrayIcon) rssOwlGui.getRSSOwlSystray().setTrayItemState(false);
    }
