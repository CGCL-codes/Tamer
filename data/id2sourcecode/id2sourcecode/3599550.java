    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        GregorianCalendar start = getStartDate(req);
        Long starttime = new Long(start.getTimeInMillis());
        int numhrs = Integer.parseInt(GetOption(req, "grid_num_hours", "4"));
        GregorianCalendar end = (GregorianCalendar) start.clone();
        end.add(Calendar.HOUR_OF_DAY, numhrs);
        Long endtime = new Long(end.getTimeInMillis());
        int numchans = Integer.parseInt(GetOption(req, "grid_num_chans", "10"));
        Vector<?> channels = getChannels(req, numchans);
        htmlHeaders(resp);
        noCacheHeaders(resp);
        PrintWriter out = getGzippedWriter(req, resp);
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>TV Guide</title>");
            out.println("</head>");
            out.println("<body>");
            printTitle(out, "TV guide");
            out.println("<div id=\"content\">");
            SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM d");
            int cellsize = Integer.parseInt(GetOption(req, "grid_cell_size", "240"));
            boolean showDesc = GetOption(req, "grid_show_desc", "false").equalsIgnoreCase("true");
            Object RecSchedList = SageApi.Api("GetScheduledRecordings");
            printDayHourChannelSelectors(out, req, numhrs);
            out.println("<table cellspacing=\"0\" class=\"epggrid\">");
            out.println("<tr class=\"timrow\">");
            out.println("   <td class=\"datecell\"><div>\r\n" + "      <a href=\"" + buildLink(req, -1, 0, 0) + "\" title=\"Previous Day\"><img class=\"prevday\" src=\"left.gif\" alt=\"Arrow Left\" /></a>\r\n" + "      " + fmt.format(new Date(starttime.longValue())) + "      <a href=\"" + buildLink(req, 1, 0, 0) + "\" title=\"Next Day\"><img class=\"nextday\"  alt=\"Arrow Right\" src=\"right.gif\"/></a>\r\n" + "   </div></td>");
            GregorianCalendar hours = (GregorianCalendar) start.clone();
            DateFormat timefmt = DateFormat.getTimeInstance(DateFormat.SHORT);
            for (int i = 0; i < numhrs; i++) {
                out.println("   <td class=\"timecell\"><div  style=\"width: " + Integer.toString(cellsize - 4) + "px;\">");
                if (i == 0) {
                    out.println("      <a href=\"" + buildLink(req, 0, -numhrs, 0) + "\" title=\"Earlier\"><img class=\"earlier\" alt=\"Arrow Left\" src=\"left.gif\"/></a>");
                }
                if (i == numhrs - 1) out.println("      <a href=\"" + buildLink(req, 0, numhrs, 0) + "\" title=\"Later\"><img class=\"later\" alt=\"Arrow Right\" src=\"right.gif\"/></a>");
                out.println(timefmt.format(hours.getTime()));
                out.println("   </div></td>");
                hours.add(Calendar.HOUR_OF_DAY, 1);
            }
            out.println("</tr></table>");
            String[] chlist = req.getParameterValues("Channel");
            boolean usechannellogos = GetOption(req, "UseChannelLogos", "true").equalsIgnoreCase("true");
            boolean markHDTV = getMarkHDTV(req);
            boolean markFirstRuns = getMarkFirstRuns(req);
            boolean showEpisodeID = GetOption(req, "ShowEpisodeID", "false").equalsIgnoreCase("true");
            for (int i = 0; i < channels.size(); i++) {
                Object channel = channels.get(i);
                out.println("<table cellspacing=\"0\" class=\"epggrid\"><tr class=\"channelrow\">");
                out.println("   <td class=\"channelid\"><div>");
                if (i == 0 && !(chlist != null && chlist.length > 0)) out.println("      <a href=\"" + buildLink(req, 0, 0, -channels.size()) + "\" title=\"page up\"><img class=\"pageup\" alt=\"Arrow Up\"  src=\"up.gif\"/></a>");
                if (i == channels.size() - 1 && !(chlist != null && chlist.length > 0)) out.println("      <a href=\"" + buildLink(req, 0, 0, channels.size()) + "\" title=\"page down\"><img class=\"pagedown\" alt=\"Arrow Down\" src=\"down.gif\"/></a>");
                String chID = SageApi.Api("GetStationID", new Object[] { channel }).toString();
                out.print("      <a href=\"EpgChannel?&amp;startchan=" + chID + "\" title=\"Channel Guide\">");
                out.print(SageApi.Api("GetChannelNumber", new Object[] { channel }).toString() + " - ");
                String chname = SageApi.Api("GetChannelName", new Object[] { channel }).toString();
                if (usechannellogos && null != SageApi.Api("GetChannelLogo", channel)) {
                    out.println("      <img src=\"ChannelLogo?ChannelID=" + chID + "&type=Med&index=1&fallback=true\" alt=\"" + Translate.encode(chname) + " logo\" title=\"" + Translate.encode(chname) + "\"/>");
                } else {
                    out.println(Translate.encode(chname));
                }
                out.print("      </a>");
                out.println("</div></td>");
                Object airings = SageApi.Api("GetAiringsOnChannelAtTime", new Object[] { channel, starttime, endtime, Boolean.FALSE });
                Object allConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
                Object unresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
                long skippedDuration = 0;
                for (int j = 0; j < SageApi.Size(airings); j++) {
                    Airing airing = new Airing(SageApi.GetElement(airings, j));
                    long cellstart = airing.getStartDate().getTime();
                    if (cellstart < starttime.longValue()) cellstart = starttime.longValue();
                    long cellend = airing.getEndDate().getTime();
                    if (cellend > endtime.longValue()) cellend = endtime.longValue();
                    long thiscellsize = (((skippedDuration + cellend - cellstart) / 1000 / 60) * cellsize / 60);
                    String ep = airing.getEpisode();
                    if (ep != null && ep.trim().length() == 0) ep = null;
                    if (showEpisodeID && (airing.idType == Airing.ID_TYPE_AIRING || (airing.idType == Airing.ID_TYPE_MEDIAFILE && SageApi.booleanApi("IsTVFile", new Object[] { airing.sageAiring })))) {
                        String epId = (String) SageApi.Api("GetShowExternalID", airing.sageAiring);
                        int epIdx = epId.length() - 4;
                        if (epId != null && epId.length() >= 12 && !epId.substring(epIdx).matches("^0*$")) if (ep != null) ep = epId.substring(epIdx) + " - " + ep; else ep = epId.substring(epIdx);
                    }
                    String titletext = airing.getTitle();
                    String alttext = Translate.encode(titletext);
                    if (titletext.trim().length() == 0) titletext = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; else titletext = Translate.encode(titletext);
                    if (ep != null && !ep.equals("")) {
                        ep = Translate.encode(ep);
                        alttext = titletext + "\r\n" + ep;
                        titletext += "<br/>" + ep;
                    }
                    String desc = "";
                    if (showDesc) {
                        desc = SageApi.StringApi("GetShowDescription", new Object[] { airing.sageAiring });
                        if (desc != null && desc.length() > 0) {
                            if (desc.length() > 150) desc = desc.substring(0, 150) + "...";
                        }
                    }
                    if (thiscellsize <= 0) {
                        skippedDuration += cellend - cellstart;
                    } else {
                        out.println("   <td class=\"epgcell\">");
                        out.println("     <table class=\"" + airing.getBgClassName(true) + " " + airing.getBorderClassName() + "\" " + "style=\"width:" + Long.toString(thiscellsize) + "px;\" cellspacing=\"0\"><tr>\r\n");
                        if (markHDTV || markFirstRuns) {
                            out.println("     <td class=\"epgmarker\">");
                            out.print("     <img src=\"MarkerHDTVDot.gif\" class=\"hd\" title=\"High Definition\"");
                            if (!airing.getHDTV() || !markHDTV) {
                                out.print(" style=\"visibility: hidden;\"");
                            }
                            out.println("></img>");
                            out.print("     <img src=\"MarkerFirstRunDot.gif\" class=\"firstrun\" title=\"First Run\"");
                            if (!airing.getFirstRun() || !markFirstRuns) {
                                out.print(" style=\"visibility: hidden;\"");
                            }
                            out.println("></img>");
                            out.println("     </td>");
                        }
                        out.println("     <td \r\n" + "title=\"" + alttext + "\r\n" + timefmt.format(airing.getStartDate()) + " - " + timefmt.format(airing.getEndDate()) + "\r\n" + desc + "\"" + ">");
                        if (airing.getWatched()) out.print("     <div class=\"watched\""); else out.print("     <div class=\"\"");
                        if (markHDTV || markFirstRuns) {
                            out.println(" style=\"width:" + Long.toString(Math.max(thiscellsize - 18, 0)) + "px;\">");
                        } else {
                            out.println(" style=\"width:" + Long.toString(Math.max(thiscellsize - 8, 0)) + "px;\">");
                        }
                        out.print("<a " + "title=\"" + alttext + "\r\n" + timefmt.format(airing.getStartDate()) + " - " + timefmt.format(airing.getEndDate()) + "\" href=\"DetailedInfo?" + airing.getIdArg() + "\">");
                        if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { RecSchedList, airing.sageAiring }))) {
                            out.print("<img src=\"recording.gif\" class=\"RecordingIndicator\" alt=\"scheduled to record\"/>");
                        } else {
                            if (SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring }) && SageApi.Api("GetMediaFileForAiring", airing.sageAiring) == null) {
                                out.print("<img src=\"conflicticon.gif\" class=\"UnresolvedConflictIndicator\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/>");
                            } else if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { allConflicts, airing.sageAiring }))) {
                                if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { unresolvedConflicts, airing.sageAiring }))) {
                                    out.print("<img src=\"conflicticon.gif\" class=\"UnresolvedConflictIndicator\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/>");
                                } else {
                                    out.print("<img src=\"resolvedconflicticon.gif\" class=\"ResolvedConflictIndicator\" alt=\"Resolved Conflict\" title=\"Resolved Conflict\"/>");
                                }
                            }
                        }
                        out.print(titletext + "\r\n");
                        out.println("   </a></div>");
                        out.println("   </td></tr></table></td>");
                    }
                }
                out.println("</tr></table>");
            }
            out.println("<div class=\"exphideall\">\r\n" + "<a onclick=\"javascript:showOptions()\" href=\"#Options\">[Show Options]</a></div>");
            out.println("<div id=\"options\" class=\"options\">\r\n" + "<h2><a name=\"Options\">EPG Grid Options:</a></h2>\r\n" + "<form method='get' action='SetOptions'>\r\n" + "<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>\r\n" + "<dl><dt>Cell width (pixels/hr)</dt><dd>");
            PrintOptionsDropdown(req, out, "grid_cell_size", Integer.toString(cellsize), CELL_SIZE_OPTS);
            out.println("</dd><dt>Number of hours to display</dt><dd>");
            PrintOptionsDropdown(req, out, "grid_num_hours", Integer.toString(numhrs), NUM_HRS_OPTS);
            out.println("</dd><dt>Number of channels to display</dt><dd>");
            PrintOptionsDropdown(req, out, "grid_num_chans", Integer.toString(numchans), NUM_CHANS_OPTS);
            out.println("</dd><dt>Channel&nbsp;Logos:&nbsp;</dt><dd>");
            PrintOptionsDropdown(req, out, "UseChannelLogos", "true", ENABLE_DISABLE_OPTS);
            out.println("</dd><dt>Show description in title-text</dt><dd>");
            PrintOptionsDropdown(req, out, "grid_show_desc", "false", ENABLE_DISABLE_OPTS);
            out.println("</dd><dt>Show&nbsp;HDTV&nbsp;Marker</dt><dd>");
            PrintOptionsDropdown(req, out, "epg_mark_hdtv", "##AsSageTV##", EPG_MARKER_OPTS);
            out.println("</dd><dt>Show&nbsp;First&nbsp;Run&nbsp;Marker</dt><dd>");
            PrintOptionsDropdown(req, out, "epg_mark_first_runs", "##AsSageTV##", EPG_MARKER_OPTS);
            out.println("</dd><dt>Include&nbsp;EpisodeID:&nbsp;</dt><dd>");
            PrintOptionsDropdown(req, out, "ShowEpisodeID", "false", ENABLE_DISABLE_OPTS);
            out.println("</dd></dl><noscript><input type=\"submit\" value=\"SetOptions\"/></noscript></form>\r\n" + "<form method='get' action='" + req.getRequestURI() + "'>\r\n" + "<dl><dt>Custom Channel Grid (Use CTRL+click to select multiple channels):\r\n" + "</dt><dd>");
            out.println("<select name=\"Channels\" multiple=\"multiple\" size=\"10\">");
            String[] Channels = req.getParameterValues("Channels");
            List<String> Channels_l = null;
            if (Channels != null) Channels_l = Arrays.asList(Channels); else Channels_l = new Vector<String>();
            Object channellist = SageApi.Api("GetAllChannels");
            channellist = SageApi.Api("Sort", new Object[] { channellist, Boolean.FALSE, "GetChannelNumber" });
            for (int i = 0; i < SageApi.Size(channellist); i++) {
                Object chan = SageApi.GetElement(channellist, i);
                if (SageApi.booleanApi("IsChannelViewable", new Object[] { chan })) {
                    String channame = SageApi.StringApi("GetChannelName", new Object[] { chan });
                    String channumber = SageApi.StringApi("GetChannelNumber", new Object[] { chan });
                    String chanId = Integer.toString(SageApi.IntApi("GetStationID", new Object[] { chan }));
                    out.print("   <option value=\"" + chanId + "\"");
                    if (Channels != null && Channels.length > 0 && Channels_l.contains(chanId)) out.print(" selected=\"selected\"");
                    out.println(">" + Translate.encode(channumber + " -- " + channame) + "</option>");
                }
            }
            out.println("</select></dd></dl>\r\n" + "<input type=\"submit\" value=\"Show Custom Channel Grid\"/>\r\n" + "</form>");
            out.println("</div>\r\n" + "<script type=\"text/javascript\">hideOptions();</script>");
            out.println("");
            printFooter(req, out);
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
        } catch (Throwable e) {
            if (!resp.isCommitted()) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("text/html");
            }
            out.println();
            out.println();
            out.println("<body><pre>");
            out.println("Exception while processing servlet:\r\n" + e.toString());
            e.printStackTrace(out);
            out.println("</pre>");
            out.close();
            log("Exception while processing servlet", e);
        }
    }
