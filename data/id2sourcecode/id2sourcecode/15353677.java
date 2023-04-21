    private void AddShowsToChannel(String baseURL, Channel channel, Object airings, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        channel.setPubDate(new Date());
        channel.setTtl(SageApi.GetIntProperty("nielm/webserver/rss_ttl_mins", 360));
        channel.setLastBuildDate(new Date());
        for (int i = 0; i < SageApi.Size(airings); i++) {
            Airing airing = new Airing(SageApi.GetElement(airings, i));
            String title = "\"" + airing.getTitle();
            String episode = airing.getEpisode();
            if (episode != null && episode.length() > 0) title = title + " - " + episode;
            title = title + "\"";
            String link = baseURL + "/DetailedInfo?" + airing.getIdArg();
            Date startDate = airing.getStartDate();
            String dateStr = DateFormat.getDateInstance().format(startDate) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(startDate);
            title = title + " at " + dateStr + " on " + airing.getChannel();
            String description;
            String showdesc = (String) SageApi.Api("GetShowDescription", airing.sageAiring);
            if (showdesc != null && showdesc.length() > 0) description = showdesc; else description = "";
            String categ = (String) SageApi.Api("GetShowCategory", airing.sageAiring);
            if (categ != null && categ.length() > 0) {
                description = description + " (Category: " + categ;
                String subcateg = (String) SageApi.Api("GetShowSubCategory", airing.sageAiring);
                if (subcateg != null && subcateg.length() > 0) description = description + "/" + subcateg;
                description = description + ")";
            }
            Item item = new Item(title, link, description);
            item.setGuid(item.new Guid(link, true));
            item.setPubDate(startDate);
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                File file = (File) SageApi.Api("GetFileForSegment", new Object[] { airing.sageAiring, new Integer(0) });
                if (file != null && file.canRead() && file.length() > 0) {
                    String url = baseURL + "public/MediaFile/" + file.getName() + "?MediaFileId=" + airing.id + "&amp;Segment=0";
                    String mimeType = this.getServletContext().getMimeType(file.getName());
                    if (mimeType == null) {
                        mimeType = "text/plain";
                    }
                    Item.Enclosure enc = item.new Enclosure(url, file.length(), mimeType);
                    item.setEnclosure(enc);
                }
            }
            channel.addItem(item);
        }
    }
