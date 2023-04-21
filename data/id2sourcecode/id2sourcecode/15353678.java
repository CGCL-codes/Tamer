    private void AddConflictsToChannel(String baseURL, Channel channel, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        channel.setPubDate(new Date());
        channel.setTtl(SageApi.GetIntProperty("nielm/webserver/rss_ttl_mins", 360));
        channel.setLastBuildDate(new Date());
        Object UnresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
        Object airings = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
        airings = SageApi.Api("Sort", new Object[] { airings, Boolean.FALSE, "GetAiringStartTime" });
        for (int i = 0; i < SageApi.Size(airings); i++) {
            Object SageAiring = SageApi.GetElement(airings, i);
            Airing airing = new Airing(SageAiring);
            String title;
            if (SageApi.Size(SageApi.Api("DataIntersection", new Object[] { UnresolvedConflicts, SageAiring })) > 0) {
                title = "UNRESOLVED: ";
            } else {
                title = "RESOLVED: ";
            }
            title = title + "\"" + airing.getTitle();
            String episode = airing.getEpisode();
            if (episode != null && episode.length() > 0) title = title + " - " + episode;
            title = title + "\"";
            String link = baseURL + "/Conflicts#AiringId" + Integer.toString(airing.id);
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
            item.setGuid(item.new Guid(link));
            item.setPubDate(startDate);
            channel.addItem(item);
        }
    }
