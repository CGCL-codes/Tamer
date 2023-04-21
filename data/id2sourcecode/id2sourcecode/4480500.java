	@Override
	public void savePluginConfig(final MPanel p) {
		if (!(p instanceof FeedsConfigPanel))
			throw new WTFError("Expected FeedsConfigPanel");

		FeedsConfigPanel panel = (FeedsConfigPanel)p;
		Config config = Config.getDefault();
		
		// general
		config.write(getGlobalEntry("fetchOnStartup"), panel.fetchOnStartup.isSelected());
		config.write(getGlobalEntry("useIntervalFetching"), panel.useIntervalFetching.isSelected());
		config.write(getGlobalEntry("fetchInterval"), panel.minutes.getNumber());
		
		// archive
		config.write(getGlobalEntry("archivePolicy"), panel.archive.getPolicy().name());
		config.write(getGlobalEntry("removeArticlesAfter"), panel.archive.getDays());
		
		// view
		FeedViewer.saveFont(getGlobalEntry("font"), panel.font.getValue());
		FeedViewer.applyFont();

		_instance.setUnreadColor(panel.unreadColor.getValue());
		config.write(getGlobalEntry("unread"), _instance.getUnreadColor());

		_instance.reloadSettings(this);

		// update colors
		new Tree.Scanner(_instance) {
			@Override
			public void processItem(final MetaInfo item) {
				if (item instanceof FeedsFS.ArticleMetaInfo)
					item.refresh(false);
			}
		};
		Tree.getInstance().repaint();
		for (Editor<?> i : Tabs.getInstance()) {
			if (i instanceof FeedViewer)
				i.repaint();
		}
	}
