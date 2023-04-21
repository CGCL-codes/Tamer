    public static String rssRender(final PageRequestResponse requestResponse) {
        final StringBuilder sb = new StringBuilder();
        try {
            final PageletBean pagelet = PortalUtils.getPageletBean(requestResponse);
            final RssModuleHandle rssModule = (RssModuleHandle) PortalModuleRegistryFactory.getInstance().getModuleHandle(pagelet);
            final RssChannel channel = rssModule.getRssChannel();
            if (channel == null) {
            } else {
                final Collection<RssChannelItem> items = channel.getChannelItems();
                final int l = Math.min(rssModule.getRows(), items.size());
                final Iterator<RssChannelItem> it = items.iterator();
                int j = 0;
                sb.append("<ul class=\"rss\">");
                while (j++ < l) {
                    final RssChannelItem item = it.next();
                    sb.append("<li>");
                    sb.append("<a target=\"_blank\" href=\"").append(item.getLink()).append("\">");
                    sb.append(item.getTitle()).append("</a>");
                    final String desc = item.getDescription();
                    if (rssModule.isShowTip() && StringUtils.hasText(desc)) {
                        sb.append("<div style=\"display: none;\">").append(HTMLUtils.convertHtmlLines(desc)).append("</div>");
                    }
                    final String pubDate = DateUtils.getDifferenceDate(item.getPubDate());
                    if (StringUtils.hasText(pubDate)) {
                        sb.append("<span> - ").append(pubDate).append("</span>");
                    }
                    sb.append("</li>");
                }
                sb.append("</ul><div class=\"rss_more\">");
                sb.append("<a onclick=\"$Actions['rssContentWindow'](_lo_getPagelet(this).params);\">#(RssUtils.0)&raquo;</a>");
                sb.append("</div>");
            }
        } catch (final Exception e) {
            return HTMLUtils.convertHtmlLines(e.toString());
        }
        return sb.toString();
    }
