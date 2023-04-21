	void loadFromMetaInfo(final MetaInfo parent, final MetaInfo metaInfo) { // package
		WebBrowser browser = getWebBrowser();

		if (!(metaInfo instanceof FeedsFS.ArticleMetaInfo))
			return;

		// auto close notifications for this feed
		MNotification notification = MNotification.getInstance();
		for (MNotification.Message i : notification) {
			if (i.getAction() instanceof FeedThread.NotificationAction) {
				FeedThread.NotificationAction action = (FeedThread.NotificationAction)i.getAction();
				if (action.getParent() == parent) {
					notification.hideMessage(i);
				}
			}
		}

		Config feedConfig = parent.getConfig();

		boolean blockImages = feedConfig.read("x.blockImages", false);
		Object imageFactoryProperty = browser.getProperty(WebBrowser.IMAGE_FACTORY_PROPERTY);
		if (imageFactoryProperty instanceof FeedUtils.BlockImageFactory) {
			FeedUtils.BlockImageFactory bif = (FeedUtils.BlockImageFactory)imageFactoryProperty;
			bif.setPolicy(
				blockImages
				? FeedUtils.BlockImageFactory.Policy.BLOCK_ALL_IMAGES
				: FeedUtils.BlockImageFactory.Policy.BLOCK_TRACKING_IMAGES
			);
		}

		loadCompleteStory = false;/*
			MWebBrowserPanel.getUseInternalBrowser() &&
			parent.getProperty("feed.loadCompleteStory", false);!!!*/
		FeedsFS.ArticleMetaInfo article = (FeedsFS.ArticleMetaInfo)metaInfo;
		String id = article.getID();
		String url = feedConfig.read("x.url", null);
		
		Archive.ItemInfo info;
		try {
			info = Archive.getInstance().get(url, id, true);
		}
		catch (ArchiveException exception) {
			MLogger.exception(exception);
			messageLabel.setErrorMessage(exception.getMessage());
			messageLabel.setVisible(true);
			
			return;
		}
		
		ArchiveChannel channel = info.getChannel();
		feedURL = url;
		
		// get stats
		readItems = 0;
		totalItems = 0;
		List<ArchiveItem> allItems = channel.getItems();
		if (!TK.isEmpty(allItems)) {
			totalItems = allItems.size();
			for (AbstractItem i : allItems) {
				if (i.isRead())
					readItems++;
			}
		}

		ArchiveItem item = info.getItem();

		articleList.update(parent, allItems, item);

		// comments link
		if (item.isCommentsLinkPresent()) {
			commentsAction.setEnabled(true);
			commentsAction.setToolTipText(UI.getLinkToolTipText(item.getCommentsLink()));
			commentsAction.setURI(item.getCommentsLink());
		}
		else {
			commentsAction.setEnabled(false);
		}

		// link
		if (item.isLinkPresent() && !TK.isEmpty(item.getLink())) {
			try {
				URL hostURL = new URL(item.getLink());
				String newHost = hostURL.getHost();
				// do not fetch favicon again if host is the same
				if (TK.isChange(newHost, oldHost)) {
					if (favicon != null) {
						favicon.cancelRequest();
						favicon.setImageObserver(null);
						favicon = null;
					}

					oldHost = newHost;
					if (blockImages) {
						hostLabel.setIcon(null);
					}
					else {
						favicon = new Favicon(hostLabel, hostURL);
						hostLabel.setIcon(favicon);
					}
				}
				hostLabel.setText(newHost);
				hostLabel.setVisible(true);
			}
			catch (MalformedURLException exception) {
				MLogger.exception(exception);
				hostLabel.setVisible(false);
			}
			
			openAction.setEnabled(true);
			openAction.setToolTipText(UI.getLinkToolTipText(item.getLink()));
			openAction.setURI(item.getLink());

			// disable "Comments" if links are identical
			if (commentsAction.isEnabled() && item.getLink().equals(item.getCommentsLink()))
				commentsAction.setEnabled(false);
		}
		else {
			hostLabel.setVisible(false);

			openAction.setEnabled(false);
			openAction.setToolTipText(null);
		}
		
		String channelTitle = parent.toString();
		if (channel.isTitlePresent()) {
			String t = channel.getTitle();
			if (!t.equals(channelTitle))
				channelTitle += " (" + t + ")";
		}

		// hide unused Back and Stop buttons
		if (browser instanceof SwingWebBrowser) {
			ActionGroup actionGroup = browser.getActionGroup();
			Action a = actionGroup.getAction(WebBrowser.BACK_ACTION);
			if (a != null)
				a.putValue(MAction.VISIBLE_KEY, false); // always hidden

			a = actionGroup.getAction(WebBrowser.FORWARD_ACTION);
			if (a != null)
				a.putValue(MAction.VISIBLE_KEY, false); // always hidden
		}

/* TODO: 2.0: set base URL
		try {
			if (channel.isLinkPresent())
				HTMLDocument.class.cast(text.getDocument()).setBase(new URL(channel.getLink()));
		}
		catch (MalformedURLException exception) {
			MLogger.exception(exception);
		}
*/
		// setup base URL for relative links
		URI baseURI = null;
		try {
			String base = channel.getBase();
			baseURI = (base == null) ? null : (new URI(base));
		}
		catch (URISyntaxException exception) {
			MLogger.exception(exception);
		}

		// reset message
		messageLabel.setVisible(false);

		if (loadCompleteStory && item.isLinkPresent()) {
			try {
				browser.setProperty(WebBrowser.HONOR_DISPLAY_PROPERTIES_PROPERTY, false);
				browser.setDocumentLocation(new URI(item.getLink()));
			}
			catch (Exception exception) {
				MLogger.exception(exception);

				loadItem(channelTitle, item, baseURI);
			}
		}
		else {
			loadItem(channelTitle, item, baseURI);
		}
		
		// enclosures
		enclosures.clear();
		if (item.hasEnclosure()) {
			enclosures.addAll(item.getEnclosure());
			enclosuresButton.setEnabled(true);
		}
		else {
			enclosuresButton.setEnabled(false);
		}
	}
