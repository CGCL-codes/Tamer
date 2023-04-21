    public void printAiringTableCell(HttpServletRequest req, PrintWriter out, boolean hascheckbox, boolean usechannellogos, boolean showmarkers, boolean showratings, boolean showepisodeid, boolean showfilesize, Object RecSchedList, Object allConflicts, Object unresolvedConflicts) throws Exception {
        String tdclass = "";
        if (getWatched()) {
            tdclass = "watched";
        }
        out.println("   <div class=\"epgcell\"><table width=\"100%\" class=\"epgcellborder " + getBorderClassName() + " " + getBgClassName(false) + "\"><tr>");
        if (hascheckbox) {
            out.print("      <td class='checkbox'><input type='checkbox' name=\"");
            if (idType == ID_TYPE_AIRING) out.print("AiringId");
            if (idType == ID_TYPE_MEDIAFILE) out.print("MediaFileId");
            out.println("\" value=\"" + Integer.toString(id) + "\"" + " onchange='javascript:checkGroupChecked(this)'/></td>");
        }
        out.println("      <td class=\"titlecell " + tdclass + "\">");
        out.println("<div class=\"" + tdclass + "\">");
        String infoLink;
        if (idType == ID_TYPE_AIRING) infoLink = "<a href=\"DetailedInfo?AiringId=" + Integer.toString(id) + "\">"; else if (idType == ID_TYPE_MEDIAFILE) infoLink = "<a href=\"DetailedInfo?MediaFileId=" + Integer.toString(id) + "\">"; else throw new Exception("Unknown Airing Type: " + idType);
        String title = getTitle().trim();
        if (title.length() == 0) title = "          ";
        if (idType == ID_TYPE_MEDIAFILE) {
            if (SageApi.booleanApi("IsMusicFile", new Object[] { sageAiring })) {
                title = "Album: " + title;
                String s = (String) SageApi.Api("GetPeopleInShow", sageAiring);
                if (s != null && s.length() > 0) {
                    title = title + " by: " + s;
                }
            } else if (SageApi.booleanApi("IsPictureFile", new Object[] { sageAiring })) {
                title = "Picture: " + title;
            }
        }
        out.println(infoLink);
        out.println(Translate.encode(title));
        String seasonAndEpisode = null;
        String ep = getEpisode();
        if (ep != null && ep.trim().length() == 0) ep = null;
        if (showepisodeid && (idType == ID_TYPE_AIRING || (idType == ID_TYPE_MEDIAFILE && SageApi.booleanApi("IsTVFile", new Object[] { sageAiring })))) {
            String epId = (String) SageApi.Api("GetShowExternalID", sageAiring);
            int epIdx = epId.length() - 4;
            if (epId != null && epId.length() >= 12 && !epId.substring(epIdx).matches("^0*$")) if (ep != null) ep = epId.substring(epIdx) + " - " + ep; else ep = epId.substring(epIdx);
        }
        if (ep != null && !ep.equals("")) {
            out.println("         <br/>" + Translate.encode(ep));
        }
        out.println("      </a></div></td>");
        if (idType == ID_TYPE_AIRING || (idType == ID_TYPE_MEDIAFILE && SageApi.booleanApi("IsTVFile", new Object[] { sageAiring }))) {
            Integer seasonNumber = (Integer) SageApi.Api("GetShowSeasonNumber", new Object[] { sageAiring });
            Integer episodeNumber = (Integer) SageApi.Api("GetShowEpisodeNumber", new Object[] { sageAiring });
            if (((seasonNumber != null) && (seasonNumber > 0)) || ((episodeNumber != null) && (episodeNumber > 0))) {
                if ((seasonNumber > 0)) {
                    seasonAndEpisode = "Season " + seasonNumber;
                }
                if ((seasonNumber > 0) && (episodeNumber > 0)) {
                    seasonAndEpisode += "<br />";
                }
                if ((episodeNumber > 0)) {
                    seasonAndEpisode += "Episode " + episodeNumber;
                }
                out.println("      <td class=\"seasonepisodecell\"><div class=\"" + tdclass + "\">" + infoLink + seasonAndEpisode + "</a></div></td>");
            }
        }
        if (showfilesize) {
            String sizeStr = null;
            if (idType == ID_TYPE_MEDIAFILE) {
                Long size = (Long) SageApi.Api("GetSize", sageAiring);
                if (size != null) {
                    DecimalFormat fmt = new DecimalFormat("0.00");
                    if (size.longValue() > 100000000l) sizeStr = fmt.format(size.doubleValue() / 1000000000.0) + "GB"; else sizeStr = fmt.format(size.doubleValue() / 1000000.0) + "MB";
                }
            }
            if (sizeStr != null) out.println("      <td class=\"filesizecell\"><div class=\"" + tdclass + "\">" + infoLink + sizeStr + "</a></div></td>"); else out.println("      <td class=\"filesizecell\"></td>");
        }
        if (showmarkers && (idType == ID_TYPE_AIRING || SageApi.booleanApi("IsTVFile", new Object[] { sageAiring }))) {
            out.println("      <td class=\"markercell\">" + infoLink);
            if (idType == ID_TYPE_MEDIAFILE || (RecSchedList != null && 1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { RecSchedList, this.sageAiring })))) {
                if (SageApi.booleanApi("IsFavorite", new Object[] { sageAiring }) && !SageApi.booleanApi("IsManualRecord", new Object[] { sageAiring })) {
                    Object favorite = SageApi.Api("GetFavoriteForAiring", sageAiring);
                    if (SageApi.booleanApi("IsFirstRunsOnly", new Object[] { favorite })) out.print("<img src=\"RecordFavFirst.gif\" alt=\"Favorite - First Runs Only\"/>"); else if (SageApi.booleanApi("IsReRunsOnly", new Object[] { favorite })) out.print("<img src=\"RecordFavRerun.gif\" alt=\"Favorite - Reruns Only\"/>"); else out.print("<img src=\"RecordFavAll.gif\" alt=\"Favorite - First Runs and Reruns\"/>");
                } else if (SageApi.booleanApi("IsManualRecord", new Object[] { sageAiring })) {
                    out.print("<img src=\"RecordMR.gif\" alt=\"Manual Recording\"/>");
                } else {
                    out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
                }
            } else if ((allConflicts != null) && (unresolvedConflicts != null)) {
                if (SageApi.booleanApi("IsManualRecord", new Object[] { sageAiring }) && SageApi.Api("GetMediaFileForAiring", sageAiring) == null) {
                    out.print("<img src=\"conflicticon.gif\" class=\"UnresolvedConflictIndicator\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/>");
                } else if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { allConflicts, sageAiring }))) {
                    if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { unresolvedConflicts, sageAiring }))) {
                        out.print("<img src=\"conflicticon.gif\" class=\"UnresolvedConflictIndicator\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/>");
                    } else {
                        out.print("<img src=\"resolvedconflicticon.gif\" class=\"ResolvedConflictIndicator\" alt=\"Resolved Conflict\" title=\"Resolved Conflict\"/>");
                    }
                } else {
                    out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
                }
            } else {
                out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
            }
            if (SageApi.booleanApi("IsShowFirstRun", new Object[] { sageAiring })) out.print("<img src=\"MarkerFirstRun.gif\" alt=\"First Run\"/>"); else out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
            if (SageApi.booleanApi("IsWatched", new Object[] { sageAiring })) out.print("<img src=\"MarkerWatched.gif\" alt=\"Watched\"/>"); else out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
            if (!AiringAPI.IsNotManualOrFavorite(sageAiring) && AiringAPI.GetScheduleEndTime(sageAiring) >= System.currentTimeMillis() && PluginUtils.isServerPluginInstalled("sre", "4\\..+") && (!Global.IsClient() || PluginUtils.isClientPluginInstalled("sre", "4\\..+"))) {
                com.google.code.sagetvaddons.sre.engine.DataStore ds = com.google.code.sagetvaddons.sre.engine.DataStore.getInstance();
                com.google.code.sagetvaddons.sre.engine.MonitorStatus status = ds.getMonitorStatusByObj(sageAiring);
                out.print(String.format("<img src=\"sre4/%s.png\" alt=\"%s\" />", status.toString(), com.google.code.sagetvaddons.sre.engine.MonitorStatus.getToolTip(status.toString())));
            }
            out.println("<br/>");
            String extraInf = SageApi.StringApi("GetExtraAiringDetails", new Object[] { sageAiring });
            if (extraInf != null && extraInf.indexOf("HDTV") >= 0) out.print("<img src=\"MarkerHDTV.gif\" alt=\"HDTV marker\"/>"); else out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
            if (idType == ID_TYPE_MEDIAFILE && SageApi.booleanApi("IsLibraryFile", new Object[] { sageAiring })) out.print("<img src=\"MarkerArchived.gif\" alt=\"Archived File\"/>"); else out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
            if (SageApi.booleanApi("IsDontLike", new Object[] { sageAiring })) out.print("<img src=\"MarkerDontLike.gif\" alt=\"Dont Like\"/>"); else out.print("<img src=\"Markerblank.gif\" alt=\"\"/>");
            out.println("      </a></td>");
        }
        if (showratings && (idType == ID_TYPE_AIRING || SageApi.booleanApi("IsTVFile", new Object[] { sageAiring }))) {
            String rating = SageApi.StringApi("GetParentalRating", new Object[] { sageAiring });
            String rated = SageApi.StringApi("GetShowRated", new Object[] { sageAiring });
            out.println("      <td class=\"ratingcell\">");
            out.println("<div class=\"" + tdclass + "\">" + infoLink);
            if ((rating != null) && (rating.trim().length() > 0)) {
                out.print("<img src=\"Rating_" + rating + ".gif\" alt=\"" + rating + "\"/>");
            }
            out.println("      </a></div>");
            out.println("      </td>");
            out.println("      <td class=\"ratedcell\">" + infoLink);
            out.println("<div class=\"" + tdclass + "\">");
            if ((rated != null) && (rated.trim().length() > 0) && (!rated.trim().equals("None"))) {
                out.println("<img src=\"Rating_" + rated + ".gif\" alt=\"" + rated + "\"/>");
            }
            out.println("      </a></div>");
            out.println("      </td>");
        }
        Date date = getStartDate();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
        DateFormat dtf = DateFormat.getTimeInstance(DateFormat.SHORT);
        out.println("      <td class=\"datecell\"><div class=\"" + tdclass + "\">" + infoLink + df.format(date) + "<br/>" + dtf.format(date));
        date = getEndDate();
        out.println("&nbsp;-&nbsp;" + dtf.format(date) + "\r\n" + "</a></div></td>");
        out.println("      <td class=\"channelcell\"><div class=\"" + tdclass + "\">" + infoLink);
        if (idType == ID_TYPE_AIRING || SageApi.booleanApi("IsTVFile", new Object[] { sageAiring })) {
            out.println("        " + Translate.encode(getChannelNum()) + " - ");
            Object channel = SageApi.Api("GetChannel", sageAiring);
            if (usechannellogos && null != SageApi.Api("GetChannelLogo", channel)) {
                String chID = SageApi.Api("GetStationID", new Object[] { channel }).toString();
                out.println("<img class=\"infochannellogo\" src=\"ChannelLogo?ChannelID=" + chID + "&type=Med&index=1&fallback=true\" alt=\"" + Translate.encode(getChannelName()) + " logo\" title=\"" + Translate.encode(getChannelName()) + "\"/>");
            } else {
                out.println(Translate.encode(getChannelName()));
            }
        } else {
            boolean isMusicFile = SageApi.booleanApi("IsMusicFile", new Object[] { sageAiring });
            Object album = null;
            boolean hasAlbumArt = false;
            if (isMusicFile) {
                album = SageApi.Api("GetAlbumForFile", new Object[] { sageAiring });
                if (album != null) {
                    try {
                        hasAlbumArt = SageApi.booleanApi("HasAlbumArt", new Object[] { album });
                    } catch (Exception e) {
                    }
                }
            }
            if (isMusicFile && null != album && hasAlbumArt) {
                out.print("<img class=\"infochannellogo\" src=\"MediaFileThumbnail?small=yes&");
                String s = (String) SageApi.Api("GetAlbumName", album);
                s = (s == null ? "" : s);
                out.print("&albumname=" + URLEncoder.encode(s, req.getCharacterEncoding()));
                s = (String) SageApi.Api("GetAlbumArtist", album);
                s = (s == null ? "" : s);
                out.print("&artist=" + URLEncoder.encode(s, req.getCharacterEncoding()));
                s = (String) SageApi.Api("GetAlbumGenre", album);
                s = (s == null ? "" : s);
                out.print("&genre=" + URLEncoder.encode(s, req.getCharacterEncoding()));
                s = (String) SageApi.Api("GetAlbumYear", album);
                s = (s == null ? "" : s);
                out.print("&year=" + URLEncoder.encode(s, req.getCharacterEncoding()));
                out.print("\" alt=\"\"/>");
            } else if (SageApi.booleanApi("HasAnyThumbnail", new Object[] { sageAiring })) {
                out.println("<img class=\"infochannellogo\" src=\"MediaFileThumbnail?MediaFileId=" + id + "&small=yes\" alt=\"\"/>");
            }
        }
        out.println("      </a></div></td>");
        out.println("   </tr></table></div>");
    }
