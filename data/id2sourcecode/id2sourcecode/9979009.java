    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Airing airing = null;
        try {
            airing = new Airing(req);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("text/html");
            noCacheHeaders(resp);
            PrintWriter out = getGzippedWriter(req, resp);
            out.println("<head><title>" + "Detailed Information" + "</title></head>");
            out.println("<body>");
            printTitle(out, "Error");
            out.println("<div id=\"content\">");
            out.println("<h3>" + e.getMessage() + "</h3>");
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
            return;
        }
        String xml = req.getParameter("xml");
        if (xml != null && xml.equalsIgnoreCase("yes")) {
            String filename = null;
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                File[] files = (File[]) SageApi.Api("GetSegmentFiles", airing.sageAiring);
                if (files != null && files.length > 0) {
                    filename = files[0].getName() + ".xml";
                }
            }
            if (filename == null) {
                filename = airing.getTitle().trim().replaceAll("[^A-Za-z0-9]", "") + "-" + airing.getEpisode().trim().replaceAll("[^A-Za-z0-9]", "") + "-" + airing.id + "-0.xml";
            }
            SendXmlResult(req, resp, airing.sageAiring, filename);
            return;
        }
        htmlHeaders(resp);
        noCacheHeaders(resp);
        PrintWriter out = getGzippedWriter(req, resp);
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>" + "Detailed Information for " + Translate.encode(airing.getTitle()) + "</title>");
            out.println("</head>");
            out.println("<body>");
            printTitleWithXml(out, "Detailed Information", req);
            out.println("<div id=\"content\">");
            out.println("<div id=\"airdetailedinfo\">");
            String s;
            boolean isMusicFile = false;
            boolean isPictureFile = false;
            boolean isTVFile = false;
            boolean isLibraryFile = false;
            boolean isVideoFile = false;
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                isMusicFile = SageApi.booleanApi("IsMusicFile", new Object[] { airing.sageAiring });
                isPictureFile = SageApi.booleanApi("IsPictureFile", new Object[] { airing.sageAiring });
                isTVFile = SageApi.booleanApi("IsTVFile", new Object[] { airing.sageAiring });
                isLibraryFile = SageApi.booleanApi("IsLibraryFile", new Object[] { airing.sageAiring });
                isVideoFile = SageApi.booleanApi("IsVideoFile", new Object[] { airing.sageAiring });
            }
            Object album = null;
            boolean hasAlbumArt = false;
            if (isMusicFile) {
                album = SageApi.Api("GetAlbumForFile", new Object[] { airing.sageAiring });
                if (album != null) {
                    try {
                        hasAlbumArt = SageApi.booleanApi("HasAlbumArt", new Object[] { album });
                    } catch (Exception e) {
                    }
                }
            }
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                if (isMusicFile) {
                    out.println("<h3>");
                    if (null != album && hasAlbumArt) {
                        out.print("<img class=\"infochannellogo\" src=\"MediaFileThumbnail?small=yes&");
                        s = (String) SageApi.Api("GetAlbumName", album);
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
                    } else if (SageApi.booleanApi("HasAnyThumbnail", new Object[] { airing.sageAiring })) {
                        out.println("<img class=\"infothumb\" src=\"MediaFileThumbnail?MediaFileId=" + airing.id + "\" alt=\"MediaFile Thumbnail\"/>");
                    }
                    out.println("Album: " + Translate.encode(airing.getTitle()));
                    s = (String) SageApi.Api("GetPeopleInShow", airing.sageAiring);
                    if (s != null && s.length() > 0) {
                        out.println(" by " + Translate.encode(s));
                    }
                    out.println("</h3>");
                    s = airing.getEpisode();
                    if (s != null && !s.equals("")) out.println("<p>" + "Track: " + Translate.encode(s) + "</p>");
                } else if (isPictureFile) {
                    File files[] = (File[]) SageApi.Api("GetSegmentFiles", new Object[] { airing.sageAiring });
                    boolean useThumb = (files == null || files.length == 0 || !files[0].canRead() || SAGE_MAJOR_VERSION < 6.0);
                    out.println("<h3>");
                    if (useThumb && SageApi.booleanApi("HasAnyThumbnail", new Object[] { airing.sageAiring })) {
                        out.println("<img class=\"infothumb\" src=\"MediaFileThumbnail?MediaFileId=" + airing.id + "\" alt=\"MediaFile Thumbnail\"/>");
                    }
                    out.println("Picture: " + Translate.encode(airing.getTitle()) + "</h3>");
                    if (!useThumb) {
                        int size = 800;
                        try {
                            String strSize = GetOption(req, "preview_image_width", "800");
                            size = Integer.parseInt(strSize);
                        } catch (NumberFormatException e) {
                            out.println("<script>DeleteOptionsCookie('preview_image_width')</script>");
                        }
                        out.println("<a href=\"" + getPublicPath(req) + "/ResizedImage?MediaFileId=" + airing.id + "&amp;width=" + size + "\" target=\"sageMediaPlayer\">\r\n" + "<img class=\"preview\" src=\"" + getPublicPath(req) + "/ResizedImage?MediaFileId=" + airing.id + "&amp;width=" + size + "\" alt=\"Preview Image\"/></a>");
                    }
                } else {
                    out.println("<h3>");
                    if (isTVFile) {
                        Object channel = SageApi.Api("GetChannel", airing.sageAiring);
                        if (channel != null && null != SageApi.Api("GetChannelLogo", channel)) {
                            String chID = SageApi.Api("GetStationID", new Object[] { channel }).toString();
                            out.println("<img class=\"infochannellogo\" src=\"ChannelLogo?ChannelID=" + chID + "&type=Large&index=1&fallback=true\" alt=\"" + Translate.encode(airing.getChannelName()) + " logo\" title=\"" + Translate.encode(airing.getChannelName()) + "\"/>");
                        }
                    } else {
                        if (SageApi.booleanApi("HasAnyThumbnail", new Object[] { airing.sageAiring })) {
                            out.println("<img class=\"infothumb\" src=\"MediaFileThumbnail?MediaFileId=" + airing.id + "\" alt=\"MediaFile Thumbnail\"/>");
                        }
                    }
                    out.println(Translate.encode(airing.getTitle()) + "</h3>");
                    s = airing.getEpisode();
                    if (s != null && !s.equals("")) out.println("<p>" + "Episode: " + Translate.encode(s) + "</p>");
                }
            } else {
                out.println("<h3>");
                Object channel = SageApi.Api("GetChannel", airing.sageAiring);
                if (channel != null && null != SageApi.Api("GetChannelLogo", channel)) {
                    String chID = SageApi.Api("GetStationID", new Object[] { channel }).toString();
                    out.println("<img class=\"infochannellogo\" src=\"ChannelLogo?ChannelID=" + chID + "&type=Large&index=1&fallback=true\" alt=\"" + Translate.encode(airing.getChannelName()) + " logo\" title=\"" + Translate.encode(airing.getChannelName()) + "\"/>");
                }
                out.println(Translate.encode(airing.getTitle()) + "</h3>");
                s = airing.getEpisode();
                if (s != null && !s.equals("")) out.println("<p>" + "Episode: " + Translate.encode(s) + "</p>");
            }
            Object RecSchedList = SageApi.Api("GetScheduledRecordings");
            Object allConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
            Object unresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
            boolean showmarkers = GetOption(req, "ShowMarkers", "true").equalsIgnoreCase("true");
            if (showmarkers && (airing.idType == Airing.ID_TYPE_AIRING || isTVFile)) {
                out.println("<p>");
                if (airing.idType == Airing.ID_TYPE_MEDIAFILE || (RecSchedList != null && 1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { RecSchedList, airing.sageAiring })))) {
                    if (SageApi.booleanApi("IsFavorite", new Object[] { airing.sageAiring }) && !SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring })) {
                        Object favorite = SageApi.Api("GetFavoriteForAiring", airing.sageAiring);
                        if (SageApi.booleanApi("IsFirstRunsOnly", new Object[] { favorite })) out.print("<img src=\"RecordFavFirst.gif\" alt=\"Favorite - First Runs Only\"/>"); else if (SageApi.booleanApi("IsReRunsOnly", new Object[] { favorite })) out.print("<img src=\"RecordFavRerun.gif\" alt=\"Favorite - Reruns Only\"/>"); else out.print("<img src=\"RecordFavAll.gif\" alt=\"Favorite - First Runs and Reruns\"/>");
                    } else if (SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring })) {
                        out.print("<img src=\"RecordMR.gif\" alt=\"Manual Recording\"/>");
                    }
                } else if ((allConflicts != null) && (unresolvedConflicts != null)) {
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
                if (SageApi.booleanApi("IsShowFirstRun", new Object[] { airing.sageAiring })) out.print("<img src=\"MarkerFirstRun.gif\" alt=\"First Run\"/>");
                if (SageApi.booleanApi("IsWatched", new Object[] { airing.sageAiring })) out.print("<img src=\"MarkerWatched.gif\" alt=\"Watched\"/>");
                if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                    String extraInf = SageApi.StringApi("GetExtraAiringDetails", new Object[] { airing.sageAiring });
                    if (extraInf != null && extraInf.indexOf("HDTV") >= 0) out.print("<img src=\"MarkerHDTV.gif\" alt=\"HDTV marker\"/>");
                }
                if (isLibraryFile) out.print("<img src=\"MarkerArchived.gif\" alt=\"Archived File\"/>");
                if (SageApi.booleanApi("IsDontLike", new Object[] { airing.sageAiring })) out.print("<img src=\"MarkerDontLike.gif\" alt=\"Dont Like\"/>");
                if (!AiringAPI.IsNotManualOrFavorite(airing.sageAiring) && AiringAPI.GetScheduleEndTime(airing.sageAiring) >= System.currentTimeMillis() && PluginUtils.isServerPluginInstalled("sre", "4\\..+") && (!Global.IsClient() || PluginUtils.isClientPluginInstalled("sre", "4\\..+"))) {
                    com.google.code.sagetvaddons.sre.engine.DataStore ds = com.google.code.sagetvaddons.sre.engine.DataStore.getInstance();
                    com.google.code.sagetvaddons.sre.engine.MonitorStatus status = ds.getMonitorStatusByObj(airing.sageAiring);
                    out.print(String.format("<img src=\"sre4/%s.png\" alt=\"%s\" />", status.toString(), com.google.code.sagetvaddons.sre.engine.MonitorStatus.getToolTip(status.toString())));
                }
                out.println("</p>");
            }
            Object rating = SageApi.StringApi("GetParentalRating", new Object[] { airing.sageAiring });
            Object rated = SageApi.StringApi("GetShowRated", new Object[] { airing.sageAiring });
            boolean showratings = GetOption(req, "ShowRatings", "true").equalsIgnoreCase("true");
            if (showratings && (airing.idType == Airing.ID_TYPE_AIRING || isTVFile)) {
                if ((SageApi.Size(rating) > 0) || (SageApi.Size(rated) > 0)) {
                    out.println("<p>");
                }
                if (SageApi.Size(rating) > 0) {
                    out.println("<img src=\"Rating_" + rating + ".gif\" alt=\"" + rating + "\"/>");
                }
                if (SageApi.Size(rated) > 0) {
                    out.println("<img src=\"Rating_" + rated + ".gif\" alt=\"" + rated + "\"/>");
                }
                if ((SageApi.Size(rating) > 0) || (SageApi.Size(rated) > 0)) {
                    out.println("</p>");
                }
            }
            Date start = airing.getStartDate();
            Date end = airing.getEndDate();
            Date now = new Date();
            long startpadmins = 0;
            long endpadmins = 0;
            if (isPictureFile) {
                out.println("<p>" + "Dated : ");
                out.println(DateFormat.getDateInstance().format(start) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(start));
            } else {
                if (end.getTime() > now.getTime()) out.println("<p>" + "Airing: "); else out.println("<p>" + "Aired: ");
                out.println(DateFormat.getDateInstance().format(start) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(start) + " - " + DateFormat.getTimeInstance(DateFormat.SHORT).format(end));
                if (airing.idType == Airing.ID_TYPE_MEDIAFILE && isLibraryFile && isTVFile) out.println(" - Archived");
                out.println("</p>");
                long duration = airing.getDuration();
                if (duration > 3600000) {
                    long mins = airing.getDuration() / 60000;
                    out.println("<p>" + "Duration: " + mins / 60 + "h" + " " + mins % 60 + "m" + "</p>");
                } else out.println("<p>" + "Duration: " + airing.getDuration() / 60000 + " " + "m" + "</p>");
                if (airing.idType == Airing.ID_TYPE_AIRING || isTVFile) out.print("<p>Channel: " + Translate.encode(airing.getChannel()) + "</p>");
                if (SageApi.Size(rating) > 0) {
                    out.println("<p>" + "Rating: " + rating + "</p>");
                }
                if (SageApi.Size(rated) > 0) {
                    out.print("<p>" + "Rated: " + rated);
                    rated = SageApi.Api("GetShowExpandedRatings", airing.sageAiring);
                    if (SageApi.Size(rated) > 0) {
                        out.print(" for " + rated.toString());
                    }
                    out.println("</p>");
                }
                if (airing.getEndDate().getTime() > now.getTime() && SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring })) {
                    long airstart = start.getTime();
                    long airend = end.getTime();
                    long schedstart = ((Long) SageApi.Api("GetScheduleStartTime", new Object[] { airing.sageAiring })).longValue();
                    long schedend = ((Long) SageApi.Api("GetScheduleEndTime", new Object[] { airing.sageAiring })).longValue();
                    startpadmins = -(airstart - schedstart) / 60000;
                    endpadmins = -(airend - schedend) / 60000;
                    if (startpadmins != 0 || endpadmins != 0) {
                        Date schedstartdate = new Date(schedstart);
                        Date schedenddate = new Date(schedend);
                        out.print("<p>" + "Scheduled to record from ");
                        out.print(DateFormat.getTimeInstance(DateFormat.SHORT).format(schedstartdate));
                        if (startpadmins != 0) if (startpadmins > 0) out.print(" (+" + Long.toString(startpadmins) + "m" + ")"); else out.print(" (" + Long.toString(startpadmins) + "m" + ")");
                        out.print(" to ");
                        out.print(DateFormat.getTimeInstance(DateFormat.SHORT).format(schedenddate));
                        if (endpadmins != 0) if (endpadmins > 0) out.print(" (+" + Long.toString(endpadmins) + "m" + ")"); else out.print(" (" + Long.toString(endpadmins) + "m" + ")");
                        out.println("</p>");
                    }
                }
            }
            s = SageApi.StringApi("GetShowDescription", new Object[] { airing.sageAiring });
            if (s != null && s.length() > 0) {
                s = Translate.encode(s);
                s = s.replaceAll("\r\n", "\n");
                s = s.replaceAll("\n\n+", "\n\n");
                s = s.trim().replaceAll("[\r\n]", "<br/>\r\n");
                out.println("<p>" + "Description: " + s + "</p>");
            }
            s = SageApi.StringApi("GetShowCategory", new Object[] { airing.sageAiring });
            if (s == null) {
                s = "";
            }
            String s1 = SageApi.StringApi("GetShowSubCategory", new Object[] { airing.sageAiring });
            if (s1 != null && s1.length() > 0) {
                s = s + "/" + s1;
            }
            Long oad = (Long) SageApi.Api("GetOriginalAiringDate", new Object[] { airing.sageAiring });
            if (oad != null && oad.longValue() != 0) {
                if (SageApi.booleanApi("IsShowFirstRun", new Object[] { airing.sageAiring })) {
                    s = s + " - First Run";
                } else {
                    s = s + " - Rerun";
                }
            }
            s1 = SageApi.StringApi("GetShowYear", new Object[] { airing.sageAiring });
            if (s1 != null && s1.length() > 0) s = s + " - " + s1;
            if (s != null && s.length() > 0) {
                out.println("<p>" + "Category: " + Translate.encode(s) + "</p>");
            }
            Long showDuration = (Long) SageApi.Api("GetShowDuration", new Object[] { airing.sageAiring });
            if ((showDuration != null) && (showDuration.longValue() > 0)) {
                String printDuration = SageApi.StringApi("PrintDuration", new Object[] { showDuration });
                out.println("<p>" + "Run Time: " + Translate.encode(printDuration) + "</p>");
            }
            if (oad != null && oad.longValue() != 0) {
                Date originalAirDate = new Date(oad.longValue());
                DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
                String formattedOriginalAirDate = df.format(originalAirDate);
                out.println("<p>" + "Original Air Date: " + Translate.encode(formattedOriginalAirDate) + "</p>");
            }
            Integer seasonNumber = (Integer) SageApi.Api("GetShowSeasonNumber", new Object[] { airing.sageAiring });
            Integer episodeNumber = (Integer) SageApi.Api("GetShowEpisodeNumber", new Object[] { airing.sageAiring });
            if (((seasonNumber != null) && (seasonNumber > 0)) || ((episodeNumber != null) && (episodeNumber > 0))) {
                out.print("<p>");
                if ((seasonNumber > 0)) {
                    out.print("Season " + seasonNumber);
                }
                if ((seasonNumber > 0) && (episodeNumber > 0)) {
                    out.print(" ");
                }
                if ((episodeNumber > 0)) {
                    out.print("Episode " + episodeNumber);
                }
                out.println("</p>");
            }
            getAndDisplay(out, "", "GetExtraAiringDetails", airing);
            getAndDisplay(out, "", "GetShowMisc", airing);
            getAndDisplay(out, "Language: ", "GetShowLanguage", airing);
            getAndDisplay(out, "Show ID: ", "GetShowExternalID", airing);
            if (!AiringAPI.IsNotManualOrFavorite(airing.sageAiring) && PluginUtils.isServerPluginInstalled("sre", "4\\..+") && (!Global.IsClient() || PluginUtils.isClientPluginInstalled("sre", "4\\..+"))) {
                String showId = ShowAPI.GetShowExternalID(airing.sageAiring);
                if (showId != null && showId.length() > 0) {
                    com.google.code.sagetvaddons.sre.engine.DataStore ds = com.google.code.sagetvaddons.sre.engine.DataStore.getInstance();
                    out.print("<p>SRE Status: " + com.google.code.sagetvaddons.sre.engine.MonitorStatus.getToolTip(ds.getMonitorStatusByObj(airing.sageAiring).toString()) + String.format(" &lt;<a href=\"sre4.groovy?a=edit&id=%1$d\">Edit</a> | <a href=\"sre4.groovy?a=delete&id=%1$d\">Delete</a>&gt;</p>", AiringAPI.GetAiringID(airing.sageAiring)));
                    com.google.code.sagetvaddons.sre.engine.AiringOverride o = ds.getOverrideByObj(airing.sageAiring);
                    if (o != null) out.print(String.format("<p>SRE Override:<ul><li><b>Title:</b> %s</li><li><b>Episode:</b> %s</li></ul></p>", o.getTitle(), o.getSubtitle()));
                }
            }
            getAndDisplayRole(out, "Starring: ", "Actor;LeadActor;Actress;LeadActress", airing);
            getAndDisplayRole(out, "Co-Starring: ", "Supporting Actor;Supporting Actress", airing);
            getAndDisplayRole(out, "Guest Stars: ", "Guest;Guest Star", airing);
            getAndDisplayRole(out, "Director: ", "Director", airing);
            getAndDisplayRole(out, "Producer: ", "Producer", airing);
            getAndDisplayRole(out, "Writer: ", "Writer", airing);
            getAndDisplayRole(out, "Choreographer: ", "Choreographer", airing);
            getAndDisplayRole(out, "Sports Figure: ", "Sports Figure", airing);
            getAndDisplayRole(out, "Coach: ", "Coach", airing);
            getAndDisplayRole(out, "Host: ", "Host", airing);
            getAndDisplayRole(out, "Executive Producer: ", "Executive Producer", airing);
            getAndDisplayRole(out, "Artist: ", "Artist", airing);
            boolean isdvd = SageApi.booleanApi("IsDVD", new Object[] { airing.sageAiring });
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                if (!isdvd) {
                    getAndDisplay(out, "Encoded by: ", "GetMediaFileEncoding", airing);
                    try {
                        getAndDisplay(out, "File Format: ", "GetMediaFileFormatDescription", airing);
                    } catch (InvocationTargetException e) {
                    }
                }
                File files[] = (File[]) SageApi.Api("GetSegmentFiles", new Object[] { airing.sageAiring });
                if (files != null) {
                    out.println("<p>Files:");
                    for (int i = 0; i < files.length; i++) {
                        out.println("<br/>");
                        if (!isdvd) out.print("<a href=\"" + getPublicPath(req) + "/MediaFile?MediaFileId=" + airing.id + "&amp;Segment=" + i + "\">");
                        out.print(Translate.encode(files[i].getAbsolutePath()));
                        if (!isdvd) out.println("</a>");
                        if (!isdvd) printFileLinks(out, files[i].getAbsolutePath());
                    }
                    out.println("</p>");
                }
                out.println("<p>File Playlists: " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=wvx&amp;fntype=filepath&amp;MediaFileId=" + airing.id + "\">[wvx]</a> " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=m3u&amp;fntype=filepath&amp;MediaFileId=" + airing.id + "\">[m3u]</a> " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=pls&amp;fntype=filepath&amp;MediaFileId=" + airing.id + "\">[pls]</a> " + "<br/>");
                Long size = (Long) SageApi.Api("GetSize", new Object[] { airing.sageAiring });
                if (size != null) {
                    DecimalFormat fmt = new DecimalFormat("0.00");
                    if (size.longValue() > 100000000l) out.println("<p>" + "Size: " + fmt.format(size.doubleValue() / 1000000000.0) + "GB </p>"); else out.println("<p>" + "Size: " + fmt.format(size.doubleValue() / 1000000.0) + "MB </p>");
                }
                out.println("<p>" + "Internal details: " + "MediaFileID=" + Integer.toString(airing.id));
                out.println(", AiringID=" + SageApi.Api("GetAiringID", SageApi.Api("GetMediaFileAiring", airing.sageAiring)).toString() + "</p>");
                if (PluginUtils.isServerPluginInstalled("mc2xmlepg") && License.isLicensed("mc2xmlepg").isLicensed()) Mc2XmlEpgUtils.writeInputForm(out, airing.sageAiring);
                if (!SageApi.booleanApi("IsClient", null) && isVideoFile) {
                    out.println("<p><a href=\"EditShowInfo?MediaFileId=" + Integer.toString(airing.id) + "\">[Edit Show Info]</a> ");
                    out.println("<p><a href=\"XmlImporter?MediaFileId=" + Integer.toString(airing.id) + "\">[Import XML Show Info]</a></p>");
                }
            } else {
                Object encoders = SageApi.Api("GetActiveCaptureDevices");
                for (int i = 0; i < SageApi.Size(encoders); i++) {
                    Object encoder = SageApi.GetElement(encoders, i);
                    Object airings = SageApi.Api("GetScheduledRecordingsForDeviceForTime", new Object[] { encoder, new Long(airing.getStartMillis()), new Long(airing.getEndMillis()) });
                    if (airings != null) {
                        for (int j = 0; j < SageApi.Size(airings); j++) {
                            Airing encoderAiring = new Airing(SageApi.GetElement(airings, j));
                            if (airing.id == encoderAiring.id) {
                                printIfNotBlank(out, "Scheduled Encoder: ", encoder.toString());
                                break;
                            }
                        }
                    }
                }
                out.println("<p>" + "Internal details: " + "AiringID=" + Integer.toString(airing.id) + "</p>");
                if (PluginUtils.isServerPluginInstalled("mc2xmlepg") && License.isLicensed("mc2xmlepg").isLicensed()) Mc2XmlEpgUtils.writeInputForm(out, airing.sageAiring);
            }
            out.println("<div id='options' class='options'>");
            if (isTVFile || airing.idType == Airing.ID_TYPE_AIRING) {
                out.println("<h3><a name=\"options\">" + "Recording Options:" + "</a></h3>\r\n");
                if (airing.getEndDate().getTime() > System.currentTimeMillis()) {
                    if (SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring })) {
                        out.println("   <form method=\"post\" action=\"ManualRecord\">\r\n" + "      <input type=\"hidden\" name=\"command\" value=\"CancelRecord\"/>");
                        if (airing.idType == Airing.ID_TYPE_AIRING) out.println("      <input type=\"hidden\" name=\"AiringId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                        if (airing.idType == Airing.ID_TYPE_MEDIAFILE) out.println("      <input type=\"hidden\" name=\"MediaFileId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                        if (!airing.getTitle().startsWith("Timed Record")) out.println("      <input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>");
                        out.println("      <input type=\"submit\" value=\"" + "Cancel Recording" + "\"/>\r\n" + "</form><p/>\r\n" + "   <form method=\"post\" action=\"ManualRecord\">\r\n" + "      <input type=\"hidden\" name=\"command\" value=\"SetRecPad\"/>");
                        if (airing.idType == Airing.ID_TYPE_AIRING) out.println("      <input type=\"hidden\" name=\"AiringId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                        if (airing.idType == Airing.ID_TYPE_MEDIAFILE) out.println("      <input type=\"hidden\" name=\"MediaFileId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                        out.println("      <input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>");
                        if (airing.getStartDate().getTime() > now.getTime()) {
                            out.println("      " + "Start Padding:" + " <input type=\"text\" size=\"3\" name=\"startpad\" value=\"" + Long.toString(Math.abs(startpadmins)) + "\"/>" + "mins ");
                            out.println("      <select name=\"StartPadOffsetType\">");
                            out.print("        <option value=\"earlier\"");
                            if (startpadmins <= 0) out.print(" selected=\"selected\"");
                            out.println(">" + "Earlier" + "</option>");
                            out.print("        <option value=\"later\"");
                            if (startpadmins > 0) out.print(" selected=\"selected\"");
                            out.println(">" + "Later" + "</option>");
                            out.println("      </select><br/>");
                        }
                        out.println("      End Padding: <input type=\"text\" size=\"3\" name=\"endpad\" value=\"" + Long.toString(Math.abs(endpadmins)) + "\"/>" + "mins");
                        out.println("      <select name=\"EndPadOffsetType\">");
                        out.print("        <option value=\"earlier\"");
                        if (endpadmins < 0) out.print(" selected=\"selected\"");
                        out.println(">" + "Earlier" + "</option>");
                        out.print("        <option value=\"later\"");
                        if (endpadmins >= 0) out.print(" selected=\"selected\"");
                        out.println(">" + "Later" + "</option>");
                        out.println("      </select><br/>");
                        out.println("      <input type=\"submit\" value=\"" + "Set Padding" + "\"/>\r\n" + "   </form><p/>\r\n" + "   <form method=\"post\" action=\"ManualRecord\">\r\n" + "      <input type=\"hidden\" name=\"command\" value=\"SetRecQual\"/>");
                        if (airing.idType == Airing.ID_TYPE_AIRING) out.println("      <input type=\"hidden\" name=\"AiringId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                        if (airing.idType == Airing.ID_TYPE_MEDIAFILE) out.println("      <input type=\"hidden\" name=\"MediaFileId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                        out.println("      <input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>");
                        if (airing.getStartDate().getTime() > now.getTime()) {
                            RecordingQuality[] recordingQualities = RecordingQuality.getRecordingQualities();
                            RecordingQuality defaultQuality = RecordingQuality.getDefaultRecordingQuality();
                            String quality = SageApi.StringApi("GetRecordingQuality", new Object[] { airing.sageAiring });
                            out.println("      <select name=\"quality\">");
                            out.print("        <option value=\"Default\"");
                            if ((quality == null) || (quality.equals("")) || (quality.equalsIgnoreCase("Default"))) {
                                out.print(" selected=\"selected\" ");
                            }
                            out.println(">" + Translate.encode("Default: " + defaultQuality.getDescription()) + "</option>");
                            for (int i = 0; i < SageApi.Size(recordingQualities); i++) {
                                RecordingQuality thisq = recordingQualities[i];
                                out.print("        <option value=\"" + thisq.getName() + "\"");
                                if (thisq.getName().equals(quality)) out.print(" selected=\"selected\" ");
                                out.println(">" + Translate.encode(thisq.getDescription()) + "</option>");
                            }
                            out.println("      </select>\r\n" + "<input type=\"submit\" value=\"" + "Set Quality" + "\"/>\r\n" + "</form>");
                        }
                    }
                } else {
                    if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                        if (!SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring })) {
                            out.println("   <form method=\"post\" action=\"ManualRecord\">\r\n" + "      <input type=\"hidden\" name=\"command\" value=\"Record\"/>");
                            out.println("      <input type=\"hidden\" name=\"MediaFileId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                            out.println("      <input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>");
                            out.println("      <input type=\"submit\" value=\"" + "Add Manual Recording Status" + "\"/>\r\n" + "</form><p/>\r\n");
                        } else {
                            out.println("   <form method=\"post\" action=\"ManualRecord\">\r\n" + "      <input type=\"hidden\" name=\"command\" value=\"CancelRecord\"/>");
                            out.println("      <input type=\"hidden\" name=\"MediaFileId\" value=\"" + Integer.toString(airing.id) + "\"/>");
                            out.println("      <input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>");
                            out.println("      <input type=\"submit\" value=\"" + "Remove Manual Recording Status" + "\"/>\r\n" + "</form><p/>\r\n");
                        }
                    }
                }
            } else if (isPictureFile) {
                out.println("<h3><a name=\"options\">" + "Image Preview Options:" + "</a></h3>\r\n");
                out.println("<form method='get' action='SetOptions'>\r\n" + "<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>\r\n" + "<dl><dt>Preview&nbsp;Image&nbsp;Width:&nbsp;</dt><dd>");
                PrintOptionsDropdown(req, out, "preview_image_width", "800", IMAGE_SIZE_OPTS);
            }
            out.println("</div>");
            out.println("<script type=\"text/javascript\">hideOptions();</script>");
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE && isVideoFile || isdvd) {
                String title = null;
                title = airing.getTitle();
                String ep = airing.getEpisode();
                if (ep != null) title = title + " - " + ep;
                File files[] = (File[]) SageApi.Api("GetSegmentFiles", new Object[] { airing.sageAiring });
                out.println("<div class=\"options\" id=\"streamingOptions\">");
                if (!isdvd) {
                    out.println("<h3><a name=\"streamingOptions\">Stream Original Video</a></h3>\n" + "(for LAN usage or for low-bandwidth files)\n" + "<ul>" + "<li><a href=\"player.html" + "?fntype=url&amp;title=" + URLEncoder.encode(title, charset) + "&amp;filename=" + URLEncoder.encode(files[0].getName(), charset) + "&amp;MediaFileId=" + airing.id + "\" target=\"sageMediaPlayer\">\r\n" + "      " + "[Play in Web-player]<br/>" + "\r\n" + "   </a></li>\r\n");
                    out.println("<li>Play in External Player: " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=wvx&amp;fntype=url&amp;MediaFileId=" + airing.id + "\">[wvx]</a> " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=m3u&amp;fntype=url&amp;MediaFileId=" + airing.id + "\">[m3u]</a> " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=pls&amp;fntype=url&amp;MediaFileId=" + airing.id + "\">[pls]</a></li>");
                }
                out.print("</li></ul>");
                if (!isdvd && VlcTranscodeMgr.getInstance().isOrbInstalled()) {
                    String orbFilename = URLEncoder.encode(files[0].getName().replaceAll("\\.[^.]*$", ""), charset);
                    out.println("<h3>Orb streaming</h3>\n" + "(Note: requires that you are <a target=\"orb\" href=\"https://mycast.orb.com/orb/html/index.html\">" + "Logged into ORB" + "</a>)\n" + "<ul><li>Streaming Playlist: (works if filename is unique):<br/>" + "<a href=\"http://mycast.orb.com/orb/data/stream.asx?q=path.filename%3d%22" + orbFilename + "%22\">[asx]</a> \n" + "<a href=\"http://mycast.orb.com/orb/data/stream.rm?q=path.filename%3d%22" + orbFilename + "%22\">[rm]</a> \n" + "<a href=\"http://mycast.orb.com/orb/data/stream.3gb?q=path.filename%3d%22" + orbFilename + "%22\">[3gb]</a> \n" + "<a href=\"http://mycast.orb.com/orb/data/stream.swf?q=path.filename%3d%22" + orbFilename + "%22\">[swf]</a> \n" + "</li><li><a target=\"orb\" href=\"https://mycast.orb.com/orb/html/video/search.html?title=video+Results&amp;mediaType=video&amp;goBack=0&amp;start=0&amp;search=Search&amp;query=" + orbFilename + "\">" + "Search for file in ORB</a><br/>(requires that you are using the " + "<a target=\"orb\" href=\"https://mycast.orb.com/orb/html/index.html?format=hometheatre\">Home Theatre Interface</a> or " + "<a target=\"orb\" href=\"https://mycast.orb.com/orb/html/index.html?format=large\">Mobile interface</a> to ORB, not the " + "<a target=\"orb\" href=\"https://mycast.orb.com/orb/html/index.html?format=pc\">PC interface</a>)" + "\n</li></ul>");
                }
                if (VlcTranscodeMgr.getInstance().isVlcInstalled()) {
                    out.println("<h3>Stream Transcoded Video</h3>\n" + "(<a href=\"speedtest.html\" target=\"speedtest\">Test your connection speed</a>)\n" + "<ul>");
                    Map<String, List<String[]>> transcodeFormatsMap = VlcTranscodeMgr.getInstance().getTranscodeFormatMap();
                    java.util.Set<String> formats = transcodeFormatsMap.keySet();
                    for (java.util.Iterator<String> it = formats.iterator(); it.hasNext(); ) {
                        String format = it.next();
                        out.println("<li><h4>" + Translate.encode(format) + "</h4><ul>");
                        List<String[]> modes = transcodeFormatsMap.get(format);
                        for (Iterator<String[]> modeIt = modes.iterator(); modeIt.hasNext(); ) {
                            String[] modePair = (String[]) modeIt.next();
                            String mode = modePair[0];
                            String args = modePair[1];
                            Matcher muxMatch = Pattern.compile("mux=([^&]*)").matcher(args);
                            String filenameHint = "";
                            if (muxMatch.find() && muxMatch.group(1) != null && muxMatch.group(1).trim().length() > 0 && VlcTranscodeMgr.getInstance().getFileExt(muxMatch.group(1).trim()) != null) {
                                filenameHint = "&amp;filename=TranscodedVideo" + VlcTranscodeMgr.getInstance().getFileExt(muxMatch.group(1));
                            }
                            args = args.replace("=", "_eq_").replace("&", "_amp_");
                            args = java.net.URLEncoder.encode(args, charset);
                            out.println("<li><b>" + Translate.encode(mode) + "</b>\n<ul>" + "<li><a href=\"player.html" + "?fntype=url&amp;title=" + URLEncoder.encode(title, charset) + "&amp;filename=" + URLEncoder.encode(files[0].getName(), charset) + "&amp;MediaFileId=" + airing.id + filenameHint + "&amp;TranscodeOpts=" + args + "\" target=\"sageMediaPlayer\">\r\n" + "      " + "[Play in Web-player]<br/>" + "\r\n" + "   </a></li>\r\n" + "<li>Play in External Player: " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=wvx&amp;fntype=url&amp;MediaFileId=" + airing.id + "&amp;TranscodeOpts=" + args + "\">[wvx]</a> " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=m3u&amp;fntype=url&amp;MediaFileId=" + airing.id + "&amp;TranscodeOpts=" + args + "\">[m3u]</a> " + "<a href=\"" + getPublicPath(req) + "/PlaylistGenerator?Command=generate&amp;pltype=pls&amp;fntype=url&amp;MediaFileId=" + airing.id + "&amp;TranscodeOpts=" + args + "\">[pls]</a></li> ");
                            out.println("</ul></li>");
                        }
                        out.println("</ul></li>");
                    }
                    out.println("<li><h4>Custom Transcode Mode</h4>\n" + "<form method=\"get\" action=\"" + getPublicPath(req) + "/PlaylistGenerator\">\n" + "<input type=\"hidden\" name=\"Command\" value=\"generate\"/>\n" + "<input type=\"hidden\" name=\"fntype\" value=\"url\"/>\n" + "<input type=\"hidden\" name=\"MediaFileId\" value=\"" + airing.id + "\"/>\n" + "<ul>\n" + "<li>Encoding parameters: <input type=\"text\" name=\"TranscodeOpts\" value=\"mode=vlc&vc=mp4v&vb=512&ac=mp3&ab=96&scale=0.5&mux=ts&deint=1\"/>\n" + "<li>Playlist Format: <select name=\"pltype\"><option value='wvx'>wvx</option><option value='m3u' selected=\"selected\">m3u</option><option value=\"pls\">pls</option></select>\n" + "<li><input type=\"submit\" value=\"Generate Playlist\"/>\n" + "</ul></form>");
                    out.println("</li></ul>");
                } else {
                    out.println("Cannot stream transcoded video -- vlc not found");
                }
                out.println("</div>");
                out.println("<script type=\"text/javascript\">document.getElementById('streamingOptions').style.display='none';</script>");
            }
            out.println("</div>");
            out.println("<div id=\"commands\">");
            out.println("<ul>");
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                out.println("<li><a href=\"props_edit.groovy?type=mf&id=" + MediaFileAPI.GetMediaFileID(airing.sageAiring) + "\">Edit Metadata</a></li>\n");
                if (isMusicFile) {
                    String title = null;
                    String track = airing.getEpisode();
                    String artist = (String) SageApi.Api("GetPeopleInShow", airing.sageAiring);
                    if (track != null && track.length() > 0 && artist != null && artist.length() > 0) title = track + " by " + artist; else title = airing.getTitle();
                    File files[] = (File[]) SageApi.Api("GetSegmentFiles", new Object[] { airing.sageAiring });
                    if (files != null && files.length > 0) {
                        out.print("<li>\r\n" + "   <a href=\"player.html" + "?fntype=url&amp;title=" + URLEncoder.encode(title, charset) + "&amp;filename=" + URLEncoder.encode(files[0].getName(), charset) + "&amp;MediaFileId=" + airing.id + "\" target=\"sageMediaPlayer\">\r\n" + "      " + "Listen (Streamed)" + "\r\n" + "   </a>\r\n" + "</li>");
                        out.print("<li>\r\n" + "   <a href=\"player.html" + "?fntype=filepath&amp;title=" + URLEncoder.encode(title, charset) + "&amp;filename=" + URLEncoder.encode(files[0].getName(), charset) + "&amp;MediaFileId=" + airing.id + "\" target=\"sageMediaPlayer\">\r\n" + "      " + "Listen (File)" + "\r\n" + "   </a>\r\n" + "</li>");
                    }
                } else if (isdvd || isVideoFile) {
                    String title = null;
                    title = airing.getTitle();
                    String ep = airing.getEpisode();
                    if (ep != null) title = title + " - " + ep;
                    File files[] = (File[]) SageApi.Api("GetSegmentFiles", new Object[] { airing.sageAiring });
                    if (isdvd) {
                        if (VlcTranscodeMgr.getInstance().isVlcInstalled()) out.println("<li>\r\n" + "   <a onclick=\"javascript:document.getElementById('streamingOptions').style.display='';\" href=\"#streamingOptions\">\r\n" + "       " + "Watch (Streamed)" + "\r\n" + "   </a>\r\n" + "</li>");
                        out.print("<li>\r\n" + "   <a href=\"player.html" + "?fntype=filepath&amp;title=" + URLEncoder.encode(title, charset) + "&amp;filename=dvd.dvd" + "&amp;MediaFileId=" + airing.id + "\" target=\"sageMediaPlayer\">\r\n" + "      " + "Watch (Local File)" + "\r\n" + "   </a>\r\n" + "</li>");
                    } else {
                        out.println("<li>\r\n" + "   <a onclick=\"javascript:document.getElementById('streamingOptions').style.display='';\" href=\"#streamingOptions\">\r\n" + "       " + "Watch (Streamed)" + "\r\n" + "   </a>\r\n" + "</li>");
                        out.print("<li>\r\n" + "   <a href=\"player.html" + "?fntype=filepath&amp;title=" + URLEncoder.encode(title, charset) + "&amp;filename=" + URLEncoder.encode(files[0].getName(), charset) + "&amp;MediaFileId=" + airing.id + "\" target=\"sageMediaPlayer\">\r\n" + "      " + "Watch (Local File)" + "\r\n" + "   </a>\r\n" + "</li>");
                    }
                } else if (isPictureFile) {
                    out.print("<li>\r\n" + "   <a href=\"" + getPublicPath(req) + "/MediaFile?MediaFileId=" + airing.id + "&amp;Segment=0" + "\" target=\"sageMediaPlayer\">\r\n" + "      View Original\r\n" + "   </a>\r\n" + "</li>");
                    int size = 800;
                    try {
                        String strSize = GetOption(req, "preview_image_width", "800");
                        size = Integer.parseInt(strSize);
                    } catch (NumberFormatException e) {
                        out.println("<script>DeleteOptionsCookie('preview_image_width')</script>");
                    }
                    if (SAGE_MAJOR_VERSION > 5.99) out.print("<li>\r\n" + "   <a href=\"" + getPublicPath(req) + "/ResizedImage?MediaFileId=" + airing.id + "&amp;width=" + size + "\" target=\"sageMediaPlayer\">\r\n" + "      View Preview\r\n" + "   </a>\r\n" + "</li>");
                    out.println("<li>\r\n" + "   <a onclick=\"javascript:showOptions()\" href=\"#options\">\r\n" + "       " + "Preview Options" + "\r\n" + "   </a>\r\n" + "</li>");
                }
            }
            if ((airing.idType == Airing.ID_TYPE_MEDIAFILE && !isPictureFile) || (airing.idType == Airing.ID_TYPE_AIRING && airing.getStartDate().getTime() <= now.getTime() && airing.getEndDate().getTime() >= now.getTime())) {
                String[] UiContexts = GetUIContextNames();
                List<String> UiContextList = new java.util.LinkedList<String>(Arrays.asList(UiContexts));
                if (UiContextList.contains("SAGETV_PROCESS_LOCAL_UI")) {
                    addAction(req, out, "WatchNow&amp;context=SAGETV_PROCESS_LOCAL_UI", "Play in SageTV", airing);
                    UiContextList.remove("SAGETV_PROCESS_LOCAL_UI");
                }
                if (UiContextList.size() > 0) {
                    out.println("<li>Play in Extender:");
                    for (java.util.Iterator<String> it = UiContextList.iterator(); it.hasNext(); ) {
                        String context = it.next();
                        out.print("<br/><form method=\"post\" action=\"MediaFileCommand\">" + "<input type=\"hidden\" name=\"command\" value=\"WatchNow\">" + "<input type=\"hidden\" name=\"context\" value=\"" + Translate.encode(context) + "\"/>");
                        out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + Translate.encode(req.getQueryString()) + "\"/>");
                        out.print("<input type=\"hidden\" name=\"" + airing.getIdArgName() + "\" value=\"" + airing.id + "\"/>");
                        out.print("<input type=\"submit\" value=\"" + Translate.encode(UiContextProperties.getProperty(context, "name")) + "\" onclick=\"return AiringCommand('" + "MediaFileCommand?command=WatchNow&amp;context=" + context + "&amp;" + airing.getIdArg() + "&amp;returnto=" + req.getRequestURI() + "?" + Translate.encode(req.getQueryString()) + "')\"/>");
                        out.println("</form>");
                    }
                    out.println("</li>");
                }
                if (SAGE_MAJOR_VERSION >= 7.0) {
                    String[] connectedClients = (String[]) SageApi.Api("GetConnectedClients");
                    if (connectedClients.length > 0) {
                        out.println("<li>Play in Client:");
                        for (int i = 0; i < connectedClients.length; i++) {
                            String context = connectedClients[i];
                            out.print("<br/><form method=\"post\" action=\"MediaFileCommand\">" + "<input type=\"hidden\" name=\"command\" value=\"WatchNow\">" + "<input type=\"hidden\" name=\"context\" value=\"" + Translate.encode(context) + "\"/>");
                            out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + Translate.encode(req.getQueryString()) + "\"/>");
                            out.print("<input type=\"hidden\" name=\"" + airing.getIdArgName() + "\" value=\"" + airing.id + "\"/>");
                            out.print("<input type=\"submit\" value=\"" + Translate.encode(UiContextProperties.getProperty(context, "name")) + "\" onclick=\"return AiringCommand('" + "MediaFileCommand?command=WatchNow&amp;context=" + context + "&amp;" + airing.getIdArg() + "&amp;returnto=" + req.getRequestURI() + "?" + Translate.encode(req.getQueryString()) + "')\"/>");
                            out.println("</form>");
                        }
                        out.println("</li>");
                    }
                }
            }
            if (airing.idType == Airing.ID_TYPE_AIRING || isTVFile) {
                if (airing.getEndDate().getTime() > System.currentTimeMillis()) {
                    if (SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring })) {
                        out.println("<li>\r\n" + "	<a onclick=\"javascript:showOptions()\" href=\"#options\">\r\n" + "		" + "Record Options" + "\r\n" + "	</a>\r\n" + "</li>");
                    } else {
                        out.print("  <li><form method=\"post\" action=\"ManualRecord\">" + "<input type=\"hidden\" name=\"command\" value=\"Record\"/>");
                        String commandStr;
                        if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                            commandStr = "ManualRecord" + "?command=Record&amp;MediaFileId=" + airing.id;
                            out.print("<input type=\"hidden\" name=\"MediaFileId\" value=\"" + airing.id + "\"/>");
                        } else {
                            commandStr = "ManualRecord" + "?command=Record&amp;AiringId=" + airing.id;
                            out.print("<input type=\"hidden\" name=\"AiringId\" value=\"" + airing.id + "\"/>");
                        }
                        out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + Translate.encode(req.getQueryString()) + "\"/>");
                        out.print("<input type=\"submit\" value=\"Record\" onclick=\"return AiringCommand('" + commandStr + "')\"/>");
                        out.println("</form></li>");
                    }
                } else {
                    if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                        out.println("<li>\r\n" + "   <a onclick=\"javascript:showOptions()\" href=\"#options\">\r\n" + "       " + "Record Options" + "\r\n" + "   </a>\r\n" + "</li>");
                    }
                }
                out.println("<li>");
                out.println("  <a href=\"Search?SearchString=" + URLEncoder.encode(airing.getTitle(), charset) + "&amp;ExactTitle=on\">");
                out.println("      " + "Additional Airings");
                out.println("   </a>" + "</li>");
                Object sageFavorite = SageApi.Api("GetFavoriteForAiring", new Object[] { airing.sageAiring });
                Boolean isFavorite = SageApi.booleanApi("IsFavorite", new Object[] { airing.sageAiring });
                out.println("<li>");
                if (!isFavorite) {
                    out.println("  <a href=\"FavoriteDetails?AddTitle=" + URLEncoder.encode(airing.getTitle(), charset) + "\">");
                    out.println("      Add Favorite");
                } else {
                    Favorite favorite = new Favorite(sageFavorite);
                    out.println("  <a href=\"FavoriteDetails?" + favorite.getIdArg() + "\">");
                    out.println("      Favorite Options");
                }
                out.println("   </a>" + "</li>");
                if (SageApi.booleanApi("IsWatched", new Object[] { airing.sageAiring })) addAction(req, out, "ClearWatched", "Clear Watched", airing); else addAction(req, out, "SetWatched", "Set Watched", airing);
                if (SageApi.booleanApi("IsDontLike", new Object[] { airing.sageAiring })) addAction(req, out, "ClearDontLike", "Clear Don't Like", airing); else addAction(req, out, "SetDontLike", "Set Don't Like", airing);
            }
            if (isMusicFile) {
                out.println("<li>");
                out.println("  <a href=\"Search?SearchString=" + URLEncoder.encode(airing.getTitle(), charset) + "&amp;searchType=MediaFiles&amp;Music=on&amp;ExactTitle=on&amp;search_fields=title&amp;sort1=episode_asc\">");
                out.println("      " + "Tracks on Album");
                out.println("   </a>" + "</li>");
                String artist = (String) SageApi.Api("GetPeopleInShow", airing.sageAiring);
                out.println("<li>");
                out.println("  <a href=\"Search?SearchString=" + URLEncoder.encode(artist, charset) + "&amp;searchType=MediaFiles&amp;Music=on&amp;search_fields=people&amp;sort1=title_asc&amp;sort2=episode_asc\">");
                out.println("      " + "Tracks by Artist");
                out.println("   </a>" + "</li>");
            }
            if (airing.idType == Airing.ID_TYPE_MEDIAFILE) {
                String returnTo = req.getParameter("deleteReturnTo");
                if (returnTo == null || returnTo.trim().length() == 0) {
                    returnTo = req.getHeader("Referer");
                    if (returnTo == null || returnTo.trim().length() == 0 || (returnTo.indexOf(req.getServletPath()) >= 0 && returnTo.indexOf(airing.getIdArg()) >= 0)) {
                        returnTo = "index.html";
                    } else {
                        returnTo = Translate.encode(returnTo.trim());
                    }
                } else {
                    returnTo = Translate.encode(returnTo.trim());
                }
                out.print("  <li><form method=\"get\" action=\"MediaFileCommand\">" + "<input type=\"hidden\" name=\"command\" value=\"DeleteFile\"/>");
                out.print("<input type=\"hidden\" name=\"MediaFileId\" value=\"" + airing.id + "\"/>");
                out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + returnTo + "\"/>");
                out.println("<input type=\"submit\" value=\"Delete Now\" onclick=\"" + "confirmAction('" + "Are you sure you want to delete this file?" + "'," + "'MediaFileCommand?command=DeleteFile&amp;returnto=" + URLEncoder.encode(returnTo, charset) + "&amp;confirm=yes&amp;MediaFileId=" + airing.id + "'); return false;\"/></form></li>");
                if (isTVFile) {
                    out.print("  <li><form method=\"get\" action=\"MediaFileCommand\">" + "<input type=\"hidden\" name=\"command\" value=\"RecordingError\"/>");
                    out.print("<input type=\"hidden\" name=\"MediaFileId\" value=\"" + airing.id + "\"/>");
                    out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + returnTo + "\"/>");
                    out.println("<input type=\"submit\" value=\"Recording Error\" onclick=\"" + "confirmAction('Are you sure you want to delete this file because that is not what was actually recorded?'," + "'MediaFileCommand?command=RecordingError&amp;returnto=" + URLEncoder.encode(returnTo, charset) + "&amp;confirm=yes&amp;MediaFileId=" + airing.id + "'); return false;\"/></form></li>");
                    if (!SageApi.booleanApi("IsFileCurrentlyRecording", new Object[] { airing.sageAiring })) {
                        if (isLibraryFile) {
                            addAction(req, out, "Unarchive", "Unarchive", airing);
                        } else {
                            addAction(req, out, "Archive", "Archive", airing);
                        }
                    }
                }
                if (SAGE_MAJOR_VERSION >= 5.1 && isVideoFile && SageApi.booleanApi("CanFileBeTranscoded", new Object[] { airing.sageAiring })) {
                    out.print("  <li><form method=\"post\" action=\"MediaFileCommand\">" + "<input type=\"hidden\" name=\"command\" value=\"convert\"/>");
                    out.print("<input type=\"hidden\" name=\"MediaFileId\" value=\"" + airing.id + "\"/>");
                    out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + Translate.encode(req.getQueryString()) + "\"/>");
                    out.print("<input type=\"submit\" value=\"Convert\"/>");
                    out.println("</form></li>");
                }
            }
            if (airing.idType == Airing.ID_TYPE_AIRING || isVideoFile) {
                out.println("<li>");
                out.print("  <a target=\"_blank\" href=\"http://www.imdb.com/find?tt=on;mx=20;q=" + URLEncoder.encode(airing.getTitle(), charset) + "\">");
                out.println("      " + "Search IMDB");
                out.println("   </a>\r\n</li>");
                out.println("<li>");
                out.print("  <a target=\"_blank\" href=\"http://www.tv.com/search.php?type=11&amp;stype=program&amp;search=Search&amp;qs=" + URLEncoder.encode(airing.getTitle(), charset) + "\">");
                out.println("      " + "Search Tv.com");
                out.println("   </a>\r\n</li>");
            }
            out.println("</ul>");
            out.println("</div>");
            printFooter(req, out);
            out.println("</div>");
            printMenu(out);
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
