    public void printFavoriteTableCell(PrintWriter out, Favorite previousFavorite, Favorite currentFavorite, Favorite nextFavorite, boolean hascheckbox, boolean showfulldescription, boolean showfirstrunsandreruns, boolean showautodelete, boolean showkeepatmost, boolean showpadding, boolean showquality, boolean showratings, boolean showtimeslot, boolean showchannels, boolean usechannellogos, int index) throws Exception {
        String tdclass = "";
        out.println("    <a name=\"FavID" + id + "\"/><table width=\"100%\" class=\"epgcell\">");
        out.println("    <tr>");
        out.println("        <td class=\"showfavorite\">");
        out.println("        <table width=\"100%\" class=\"show_other\">");
        out.println("        <tr>");
        if (hascheckbox) {
            out.print("            <td class=\"checkbox\"><input type=\"checkbox\" name=\"\"");
            out.print("FavoriteId");
            out.println("\" value=\"" + id + "\"/></td>");
        }
        out.println("            <td class=\"titlecell " + tdclass + "\">");
        out.println("            <div class=\"" + tdclass + "\">");
        out.print("                <a href=\"FavoriteDetails?" + getIdArg() + "\">");
        if (index > 0) out.print(index + ".&nbsp;");
        if (showfulldescription) {
            out.print(Translate.encode(getDescription()));
        } else {
            String title = getTitle();
            String category = getCategory();
            String keyword = getKeyword();
            String person = getPerson();
            if ((title != null) && (title.trim().length() > 0)) {
                out.print(Translate.encode(title));
            }
            if ((category != null) && (category.trim().length() > 0)) {
                out.print(Translate.encode(category));
            }
            if ((keyword != null) && (keyword.trim().length() > 0)) {
                out.print(Translate.encode("[" + keyword + "]"));
            }
            if ((person != null) && (person.trim().length() > 0)) {
                out.print(Translate.encode(person));
            }
        }
        out.println("</a>");
        out.println("            </div>");
        out.println("            </td>");
        if (showfirstrunsandreruns) {
            out.println("            <td class=\"favruncell\"><div class=\"" + tdclass + "\">");
            if (isFirstRunsOnly()) {
                out.println("                <img src=\"RecordFavFirst.gif\" alt=\"Favorite - First Runs Only\" title=\"Favorite - First Runs Only\"/>");
            } else if (isReRunsOnly()) {
                out.println("                <img src=\"RecordFavRerun.gif\" alt=\"Favorite - Reruns Only\" title=\"Favorite - Reruns Only\"/>");
            } else {
                out.println("                <img src=\"RecordFavAll.gif\" alt=\"Favorite - First Runs and Reruns\" title=\"Favorite - First Runs and Reruns\"/>");
            }
            out.println("            </div></td>");
        }
        if (showautodelete) {
            out.println("            <td class=\"autodeletecell\"><div class=\"" + tdclass + "\">");
            if (isAutoDelete()) {
                out.println("                <img src=\"MarkerDelete.gif\" alt=\"Auto Delete\" title=\"Auto Delete\"/>");
            }
            out.println("            </div></td>");
        }
        if (showkeepatmost) {
            out.println("            <td class=\"keepatmostcell\"><div class=\"" + tdclass + "\">");
            out.println("                Keep<br/> " + (getKeepAtMost() == 0 ? "All" : Integer.toString(getKeepAtMost())));
            out.println("            </div></td>");
        }
        if (showpadding) {
            long startPadding = getStartPadding() / 60000;
            long stopPadding = getStopPadding() / 60000;
            String startPaddingString = ((startPadding > 0) ? "+" : "") + Long.toString(startPadding);
            String stopPaddingString = ((stopPadding > 0) ? "+" : "") + Long.toString(stopPadding);
            String tooltip = "";
            tooltip = Math.abs(startPadding) + " minute" + (Math.abs(startPadding) != 1 ? "s" : "") + (startPadding < 0 ? " later.  " : " earlier.  ");
            tooltip += Math.abs(stopPadding) + " minute" + (Math.abs(stopPadding) != 1 ? "s" : "") + (stopPadding < 0 ? " earlier." : " later.");
            out.println("            <td class=\"paddingcell\"><div class=\"" + tdclass + "\" title=\"" + Translate.encode(tooltip) + "\">");
            out.println("                Pad<br/> " + Translate.encode(startPaddingString) + "/" + Translate.encode(stopPaddingString));
            out.println("            </div></td>");
        }
        if (showquality) {
            out.println("            <td class=\"qualitycell\"><div class=\"" + tdclass + "\">");
            String qualityName = getQuality();
            RecordingQuality quality = new RecordingQuality(qualityName);
            if ((qualityName == null) || (qualityName.equals("Default")) || (qualityName.trim().length() == 0)) {
                quality = RecordingQuality.getDefaultRecordingQuality();
                qualityName = quality.getName();
                out.println("                <span title=\"" + quality.getName() + " - " + quality.getFormat() + " @ " + quality.getGigabytesPerHour() + " GB/hr\">Default</span>");
            } else {
                out.println("                " + quality.getName() + "<br/>" + quality.getFormat() + " @ " + quality.getGigabytesPerHour() + " GB/hr");
            }
            out.println("            </div></td>");
        }
        if (showratings) {
            String rating = getParentalRating();
            out.println("            <td class=\"ratingcell\"><div class=\"" + tdclass + "\">");
            if ((rating != null) && (rating.length() > 0)) {
                out.println("                <img src=\"Rating_" + rating + ".gif\" alt=\"" + rating + "\" title=\"" + rating + "\"/>");
            }
            out.println("            </div></td>");
            String rated = getRated();
            out.println("            <td class=\"ratedcell\"><div class=\"" + tdclass + "\">");
            if ((rated != null) && (rated.length() > 0)) {
                out.println("                <img src=\"Rating_" + rated + ".gif\" alt=\"" + rated + "\" title=\"" + rated + "\"/>");
            }
            out.println("            </div></td>");
        }
        if (showtimeslot) {
            out.println("            <td class=\"dayhourcell\"><div class=\"" + tdclass + "\">");
            out.println("                " + Translate.encode(getDay()) + "<br/>" + Translate.encode(getTime()));
            out.println("            </div></td>");
        }
        if (showchannels) {
            List<String> favoriteChannelNameList = getChannels();
            out.println("            <td class=\"favchannelcell\"><div class=\"" + tdclass + "\">");
            if ((favoriteChannelNameList != null) && (favoriteChannelNameList.size() == 1)) {
                Object channels = SageApi.Api("GetAllChannels");
                int channelCount = SageApi.Size(channels);
                Object channel = null;
                String channelName = "";
                String channelNumber = "";
                int channelId = 0;
                for (int i = 0; i < channelCount; i++) {
                    Object currentChannel = SageApi.GetElement(channels, i);
                    String currentChannelName = SageApi.StringApi("GetChannelName", new Object[] { currentChannel });
                    if (favoriteChannelNameList.contains(currentChannelName)) {
                        channel = currentChannel;
                        channelName = currentChannelName;
                        channelNumber = SageApi.StringApi("GetChannelNumber", new Object[] { currentChannel });
                        channelId = SageApi.IntApi("GetStationID", new Object[] { currentChannel });
                        break;
                    }
                }
                out.println("                " + Translate.encode(channelNumber) + " - ");
                if (usechannellogos && null != SageApi.Api("GetChannelLogo", channel)) {
                    out.println("                <img class=\"infochannellogo\" src=\"ChannelLogo?ChannelID=" + channelId + "&type=Med&index=1&fallback=true\" alt=\"" + Translate.encode(channelName) + " logo\" title=\"" + Translate.encode(channelName) + "\"/>");
                } else {
                    out.println("                " + Translate.encode(channelName));
                }
            } else if (favoriteChannelNameList.size() > 1) {
                out.println("                " + favoriteChannelNameList.size() + " channels");
            }
            out.println("            </div></td>");
        }
        out.println("            <td class=\"favarrowscell\">");
        if (previousFavorite != null) {
            String commandStr = "FavoriteCommand?command=CreatePriority" + "&amp;LowerId=" + previousFavorite.getID() + "&amp;HigherId=" + currentFavorite.getID() + "&amp;returnto=Favorites";
            out.println("                <a onclick=\"return AiringCommand('" + commandStr + "','FavID" + id + "')\" href=\"" + commandStr + "\">");
            out.print("                    <img alt=\"\" ");
            out.print("style=\"float:left;\" ");
            out.print("width=\"15\" height=\"15\" ");
            out.println("title=\"Increase Favorite Priority\" src=\"up.gif\"/>");
            out.println("                </a>");
        }
        if (nextFavorite != null) {
            String commandStr = "FavoriteCommand?command=CreatePriority" + "&amp;LowerId=" + currentFavorite.getID() + "&amp;HigherId=" + nextFavorite.getID() + "&amp;returnto=Favorites";
            out.println("                <a onclick=\"return AiringCommand('" + commandStr + "','FavID" + id + "')\" href=\"" + commandStr + "\">");
            out.print("                    <img alt=\"\" ");
            out.print("style=\"float:left;\" ");
            out.print("width=\"15\" height=\"15\" ");
            out.println("title=\"Decrease Favorite Priority\" src=\"down.gif\"/>");
            out.println("                </a>");
        }
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        </table>");
        out.println("        </td>");
        out.println("    </tr>");
        out.println("    </table>");
    }
