    void setContent(ShowsQueryResponse resp) {
        HTML link = new HTML("<p><a href=\"" + resp.getRssUrl() + "\">Link to this RSS feed</a></p>");
        String str = "Query found " + resp.size() + " unique show(s).\n\n";
        SageShow[] results = resp.getResults();
        for (SageShow s : results) {
            str = str.concat(s.getTitle());
            String subtitle = s.getSubtitle();
            if (subtitle != null && subtitle.length() > 0) str = str.concat(": \"" + s.getSubtitle() + "\"");
            str = str.concat(" next airing at " + DateTimeFormat.getShortDateTimeFormat().format(s.getStart()) + " on " + s.getChannel() + "\n");
        }
        clear();
        add(link);
        add(new HTML("<pre>" + str + "</pre>"));
    }
