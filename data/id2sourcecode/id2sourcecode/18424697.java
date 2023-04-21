    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = "-1";
        String description = "";
        String category = "";
        String title = "";
        String keyword = "";
        String person = "";
        boolean firstRunsOnly = false;
        boolean reRunsOnly = false;
        List<String> channelList = new ArrayList<String>();
        boolean autoDelete = true;
        long keepAtMost = 0;
        long startPadding = 0;
        long stopPadding = 0;
        String quality = "";
        boolean isDeleteAfterFavoriteAutomaticConversion = false;
        String favoriteAutomaticConversionFormat = "";
        File favoriteAutomaticConversionDestination = null;
        String parentalRating = "";
        String rated = "";
        String day = "";
        String time = "";
        String favoriteId = req.getParameter("FavoriteId");
        boolean isNew = ((favoriteId == null) || (favoriteId.trim().length() == 0));
        Favorite favorite = null;
        String xml = req.getParameter("xml");
        if (!isNew) {
            try {
                favorite = new Favorite(req);
            } catch (Exception e) {
                log("failed to get Favorite for ID ", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("text/html");
                noCacheHeaders(resp);
                PrintWriter out = getGzippedWriter(req, resp);
                out.println("<head><title>" + "Favorite Information" + "</title></head>");
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
            if (xml != null && xml.equalsIgnoreCase("yes")) {
                SendXmlResult(req, resp, favorite.getSageFavorite(), "favorite_info.xml");
                return;
            }
        }
        htmlHeaders(resp);
        noCacheHeaders(resp);
        PrintWriter out = getGzippedWriter(req, resp);
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            if (isNew) {
                category = null;
                title = null;
                keyword = null;
                person = null;
                category = req.getParameter("AddCategory");
                if (category == null) {
                    title = req.getParameter("AddTitle");
                    if (title == null) {
                        keyword = req.getParameter("AddKeyword");
                        if (keyword == null) {
                            person = req.getParameter("AddPerson");
                            if (person == null) {
                                out.println("<title>New Favorite</title></head>");
                                out.println("<body>");
                                printTitle(out, "Error");
                                out.println("<div id=\"content\">");
                                out.println("<h3>Category, title, keyword, or person required for new favorite.</h3>");
                                out.println("</div>");
                                printMenu(req, out);
                                out.println("</body></html>");
                                out.close();
                                return;
                            }
                        }
                    }
                }
            } else {
                id = new Integer(favorite.getID()).toString();
                description = favorite.getDescription();
                category = favorite.getCategory();
                title = favorite.getTitle();
                keyword = favorite.getKeyword();
                person = favorite.getPerson();
                firstRunsOnly = favorite.isFirstRunsOnly();
                reRunsOnly = favorite.isReRunsOnly();
                channelList = favorite.getChannels();
                autoDelete = favorite.isAutoDelete();
                keepAtMost = favorite.getKeepAtMost();
                startPadding = favorite.getStartPadding();
                stopPadding = favorite.getStopPadding();
                quality = favorite.getQuality();
                isDeleteAfterFavoriteAutomaticConversion = favorite.IsDeleteAfterAutomaticConversion();
                favoriteAutomaticConversionFormat = favorite.GetFavoriteAutomaticConversionFormat();
                favoriteAutomaticConversionDestination = favorite.GetFavoriteAutomaticConversionDestination();
                parentalRating = favorite.getParentalRating();
                rated = favorite.getRated();
                day = favorite.getDay();
                time = favorite.getTime();
            }
            if (isNew) {
                out.println("<title>New Favorite</title>");
            } else {
                out.println("<title>Favorite Details for " + Translate.encode(description) + "</title>");
            }
            out.println("</head>");
            out.println("<body>");
            if (isNew) {
                printTitle(out, "New Favorite");
            } else {
                printTitleWithXml(out, "Favorite Details", req);
            }
            out.println("<div id=\"content\">");
            out.println("<div id=\"airdetailedinfo\">");
            if (isNew) {
                out.println("    <h3>New Favorite</h3>");
            } else {
                out.println("    <h3>Favorite: " + Translate.encode(description) + "</h3>");
            }
            out.println("    <form method=\"post\" action=\"FavoriteCommand\">");
            out.println("        <input type=\"hidden\" name=\"command\" value=\"" + (isNew ? "Add" : "Update") + "\"/>");
            out.println("        <input type=\"hidden\" name=\"FavoriteId\" value=\"" + id + "\"/>");
            out.println("        <input type=\"hidden\" name=\"returnto\" value=\"" + (isNew ? "Favorites" : "FavoriteDetails?" + favorite.getIdArg()) + "\"/>");
            out.println("        <dl>");
            if (((isNew) && (category != null)) || ((!isNew) && (category != null) && (category.trim().length() > 0))) {
                out.print("        <dt><b>Category:</b>");
                if (!isNew) {
                    out.print(" " + Translate.encode(category));
                }
                out.println("</dt>");
                out.println("        <dd>");
                if (isNew) {
                    Object categories = SageApi.Api("GetAllCategories");
                    categories = SageApi.Api("DataUnion", new Object[] { category, categories });
                    categories = SageApi.Api("Sort", new Object[] { categories, Boolean.FALSE, "CaseInsensitive" });
                    out.println("            <select name=\"category\">");
                    for (int i = 0; i < SageApi.Size(categories); i++) {
                        Object thisCategory = SageApi.GetElement(categories, i);
                        out.print("                <option value=\"" + Translate.encode(thisCategory.toString()) + "\"");
                        if (thisCategory.equals(category)) {
                            out.print(" selected=\"selected\" ");
                        }
                        out.println(">" + Translate.encode(thisCategory.toString()) + "</option>");
                    }
                    out.println("            </select>");
                } else {
                    out.println("            <input type=\"hidden\" name=\"category\" value=\"" + Translate.encode(category) + "\"/>&nbsp;");
                }
                out.println("        </dd>");
            }
            if (((isNew) && (title != null)) || ((!isNew) && (title != null) && (title.trim().length() > 0))) {
                out.print("        <dt><b>Title:</b>");
                if (!isNew) {
                    out.print(" " + Translate.encode(title));
                }
                out.println("</dt>");
                out.println("        <dd>");
                if (isNew) {
                    Object titles = SageApi.Api("SearchForTitles", new Object[] { "" });
                    titles = SageApi.Api("DataUnion", new Object[] { title, titles });
                    titles = SageApi.Api("Sort", new Object[] { titles, Boolean.FALSE, "CaseInsensitive" });
                    out.println("            <select name=\"title\">");
                    for (int i = 0; i < SageApi.Size(titles); i++) {
                        Object thisTitle = SageApi.GetElement(titles, i);
                        out.print("                <option value=\"" + Translate.encode(thisTitle.toString()) + "\"");
                        if (thisTitle.equals(title)) {
                            out.print(" selected=\"selected\" ");
                        }
                        out.println(">" + Translate.encode(thisTitle.toString()) + "</option>");
                    }
                    out.println("            </select>");
                } else {
                    out.println("            <input type=\"hidden\" name=\"title\" value=\"" + Translate.encode(title) + "\"/>&nbsp;");
                }
                out.println("        </dd>");
            }
            out.println("        <dt><b>Keyword:</b></dt>");
            out.println("        <dd>");
            out.println("            <input type=\"text\" size=\"30\" name=\"keyword\" value=\"" + (keyword != null ? Translate.encode(keyword) : "") + "\"/>&nbsp;");
            out.println("        </dd>");
            if (((isNew) && (person != null)) || ((!isNew) && (person != null) && (person.trim().length() > 0))) {
                out.print("        <dt><b>Person:</b>");
                if (!isNew) {
                    out.print(" " + Translate.encode(person));
                }
                out.println("</dt>");
                out.println("        <dd>");
                if (isNew) {
                    Object people = SageApi.Api("SearchForPeople", new Object[] { "" });
                    people = SageApi.Api("DataUnion", new Object[] { person, people });
                    people = SageApi.Api("Sort", new Object[] { people, Boolean.FALSE, "CaseInsensitive" });
                    out.println("            <select name=\"person\">");
                    for (int i = 0; i < SageApi.Size(people); i++) {
                        Object thisPerson = SageApi.GetElement(people, i);
                        out.print("                <option value=\"" + Translate.encode(thisPerson.toString()) + "\"");
                        if (thisPerson.equals(person)) {
                            out.print(" selected=\"selected\" ");
                        }
                        out.println(">" + Translate.encode(thisPerson.toString()) + "</option>");
                    }
                    out.println("            </select>");
                } else {
                    out.println("            <input type=\"hidden\" name=\"person\" value=\"" + Translate.encode(person) + "\"/>&nbsp;");
                }
                out.println("        </dd>");
            }
            out.println("        <dt>First Runs and ReRuns:</dt>");
            out.println("        <dd>");
            out.println("            <select name=\"run\">");
            out.print("                <option value=\"First Runs\"");
            if (firstRunsOnly && !reRunsOnly) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">" + Translate.encode("First Runs") + "</option>");
            out.print("                <option value=\"ReRuns\"");
            if (!firstRunsOnly && reRunsOnly) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">" + Translate.encode("ReRuns") + "</option>");
            out.print("                <option value=\"First Runs and ReRuns\"");
            if (!firstRunsOnly && !reRunsOnly) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">" + Translate.encode("First Runs and ReRuns") + "</option>");
            out.println("            </select>");
            out.println("        </dd>");
            Object channels = SageApi.Api("GetAllChannels");
            out.println("        <dt>Channels:</dt>");
            channels = SageApi.Api("Sort", new Object[] { channels, Boolean.FALSE, "ChannelNumber" });
            out.println("        <dd>");
            out.println("            <select name=\"channelID\" multiple=\"multiple\" size=\"10\">");
            out.print("                <option value=\"\"");
            if (channelList.size() == 0) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">Any</option>");
            for (int i = 0; i < SageApi.Size(channels); i++) {
                Object thisChannel = SageApi.GetElement(channels, i);
                if (SageApi.booleanApi("IsChannelViewable", new Object[] { thisChannel })) {
                    Object stationId = SageApi.Api("GetStationID", thisChannel);
                    String channelName = SageApi.StringApi("GetChannelName", new Object[] { thisChannel });
                    String channelNumber = SageApi.StringApi("GetChannelNumber", new Object[] { thisChannel });
                    String channelNetwork = SageApi.StringApi("GetChannelNetwork", new Object[] { thisChannel });
                    out.print("                <option value=\"" + stationId.toString() + "\"");
                    if (channelList.contains(channelName)) {
                        out.print(" selected=\"selected\" ");
                    }
                    out.println(">" + Translate.encode(channelNumber + " -- " + channelName + " -- " + channelNetwork) + "</option>");
                }
            }
            out.println("            </select>");
            out.println("        </dd>");
            out.println("        <dt>Auto Delete:</dt>");
            out.println("        <dd>");
            out.println("            <select name=\"autodelete\">");
            out.print("                <option value=\"true\"");
            if (autoDelete) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">Yes</option>");
            out.print("                <option value=\"false\"");
            if (!autoDelete) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">No</option>");
            out.println("            </select>");
            out.println("        </dd>");
            out.println("        <dt>Keep At Most:</dt>");
            out.println("        <dd>");
            out.println("            <select name=\"keepatmost\">");
            out.print("                <option value=\"0\"");
            if (keepAtMost == 0) {
                out.print(" selected=\"selected\" ");
            }
            out.println(">All</option>");
            int maxKeepAtMost = 15;
            if (SAGE_MAJOR_VERSION >= 7.0) {
                maxKeepAtMost = 63;
            }
            for (int i = 1; i <= maxKeepAtMost; i++) {
                out.print("                <option value=\"" + i + "\"");
                if (keepAtMost == i) {
                    out.print(" selected=\"selected\" ");
                }
                out.println(">" + i + "</option>");
            }
            out.println("            </select>");
            out.println("        </dd>");
            long startpadmins = -startPadding / 60000;
            out.println("        <dt>Start Padding:</dt>");
            out.println("        <dd>");
            out.println("            <input type=\"text\" size=\"3\" name=\"startpad\" value=\"" + Math.abs(startpadmins) + "\"/> mins ");
            out.println("            <select name=\"StartPadOffsetType\">");
            out.print("                <option value=\"Earlier\"");
            if (startpadmins <= 0) {
                out.print(" selected=\"selected\"");
            }
            out.println(">Earlier</option>");
            out.print("                <option value=\"Later\"");
            if (startpadmins > 0) {
                out.print(" selected=\"selected\"");
            }
            out.println(">Later</option>");
            out.println("            </select>");
            out.println("        </dd>");
            long endpadmins = stopPadding / 60000;
            out.println("        <dt>End Padding:</dt>");
            out.println("        <dd>");
            out.println("            <input type=\"text\" size=\"3\" name=\"endpad\" value=\"" + Math.abs(endpadmins) + "\"/> mins ");
            out.println("            <select name=\"EndPadOffsetType\">");
            out.print("                <option value=\"Earlier\"");
            if (endpadmins < 0) {
                out.print(" selected=\"selected\"");
            }
            out.println(">Earlier</option>");
            out.print("                <option value=\"Later\"");
            if (endpadmins >= 0) {
                out.print(" selected=\"selected\"");
            }
            out.println(">Later</option>");
            out.println("            </select>");
            out.println("        </dd>");
            RecordingQuality[] recordingQualities = RecordingQuality.getRecordingQualities();
            RecordingQuality defaultQuality = RecordingQuality.getDefaultRecordingQuality();
            out.println("        <dt>Quality:</dt>");
            out.println("        <dd>");
            out.println("            <select name=\"quality\">");
            out.print("                <option value=\"Default\" ");
            if ((quality == null) || (quality.equals("")) || (quality.equalsIgnoreCase("Default"))) {
                out.print("selected=\"selected\"");
            }
            out.println(">" + Translate.encode("Default: " + defaultQuality.getDescription()) + "</option>");
            for (int i = 0; i < SageApi.Size(recordingQualities); i++) {
                RecordingQuality recordingQuality = recordingQualities[i];
                out.print("                <option value=\"" + recordingQuality.getName() + "\"");
                if (recordingQuality.getName().equals(quality)) {
                    out.print(" selected=\"selected\" ");
                }
                out.println(">" + Translate.encode(recordingQuality.getDescription()) + "</option>");
            }
            out.println("            </select>");
            out.println("        </dd>");
            if (SAGE_MAJOR_VERSION >= 7.0) {
                boolean isAutomaticConversion = ((favoriteAutomaticConversionFormat != null) && (favoriteAutomaticConversionFormat.trim().length() > 0));
                String lastReplaceChoice = (String) SageApi.Api("GetProperty", new Object[] { "transcoder/last_replace_choice", "xKeepBoth" });
                String lastFormatName = (String) SageApi.Api("GetProperty", new Object[] { "transcoder/last_format_name", null });
                String lastFormatQuality = (String) SageApi.Api("GetProperty", new Object[] { "transcoder/last_format_quality/" + lastFormatName, null });
                String lastDestDir = (String) SageApi.Api("GetProperty", new Object[] { "transcoder/last_dest_dir", null });
                if (!isAutomaticConversion) {
                    favoriteAutomaticConversionFormat = lastFormatQuality;
                    isDeleteAfterFavoriteAutomaticConversion = false;
                    if (lastReplaceChoice != null && lastReplaceChoice.equals("xKeepOnlyConversion")) {
                        isDeleteAfterFavoriteAutomaticConversion = true;
                    }
                    favoriteAutomaticConversionDestination = (lastDestDir == null) ? null : new File(lastDestDir);
                }
                out.println("        <dt>Automatic Conversion:</dt>");
                out.println("        <dd>");
                out.println("            <select name=\"autoConvert\">");
                out.print("                <option value=\"yes\" ");
                if (isAutomaticConversion) {
                    out.print("selected=\"selected\"");
                }
                out.println(">Yes</option>");
                out.print("                <option value=\"no\" ");
                if (!isAutomaticConversion) {
                    out.print("selected=\"selected\"");
                }
                out.println(">No</option>");
                out.println("            </select>");
                out.println("            <dl>");
                out.println("            <dt>Format:</dt>");
                out.println("            <dd>");
                out.println("                <select name=\"transcodeMode\">");
                String[] transcodeFormats = (String[]) SageApi.Api("GetTranscodeFormats");
                java.util.Arrays.sort(transcodeFormats);
                String transcodeGroup = "";
                for (int i = 0; i < transcodeFormats.length; i++) {
                    String formatName = transcodeFormats[i];
                    String[] formatArr = formatName.split("-", 2);
                    if (!formatArr[0].equals(transcodeGroup)) {
                        if (transcodeGroup.length() > 0) out.println("                    </optgroup>");
                        out.println("                    <optgroup label=\"" + formatArr[0] + "\">");
                        transcodeGroup = formatArr[0];
                    }
                    out.print("                        <option value=\"" + formatName + "\"");
                    if (((i == 0) && ((favoriteAutomaticConversionFormat == null) || (favoriteAutomaticConversionFormat.trim().equals("")))) || (formatName.equals(favoriteAutomaticConversionFormat))) {
                        out.print("selected=\"selected\"");
                    }
                    out.print(">" + formatName + "</option>");
                }
                if (transcodeGroup.length() > 0) out.println("                    </optgroup>");
                out.println("                </select>");
                out.println("            </dd>");
                out.println("            <dt>Original File:</dt>");
                out.println("            <dd>");
                out.println("                <input type=\"radio\" name=\"deleteOriginal\" value=\"yes\" " + (isDeleteAfterFavoriteAutomaticConversion ? "checked=\"checked\"" : "") + "/> Delete Original File<br/>");
                out.println("                <input id=\"noDeleteOriginal\" type=\"radio\" name=\"deleteOriginal\" value=\"no\" " + (!isDeleteAfterFavoriteAutomaticConversion ? "checked=\"checked\"" : "") + "/> Keep Original File ");
                out.println("            </dd>");
                out.println("            <dt>Destination Folder:</dt>");
                out.println("            <dd>");
                out.println("                <input type=\"radio\" onchange=\"updateDestFolder()\" name=\"origDestDir\" value=\"yes\" " + ((favoriteAutomaticConversionDestination == null) ? "checked=\"checked\"" : "") + "/> Original Folder<br/>");
                out.println("                <input id=\"altdestfolder\" onchange=\"updateDestFolder()\" type=\"radio\" name=\"origDestDir\" value=\"no\" " + ((favoriteAutomaticConversionDestination != null) ? "checked=\"checked\"" : "") + "/> Alternate Destination Folder (must exist on server):<br/> ");
                out.println("                <dl style=\"border: none\">");
                out.println("                <dt></dt>");
                out.println("                <dd>");
                out.print("                    <input id=\"destfoldername\" name=\"destDir\" type=\"text\" disabled=\"true\" style=\"width: 30em;\" ");
                if (favoriteAutomaticConversionDestination != null) {
                    out.print("value=\"" + Translate.encode(favoriteAutomaticConversionDestination.toString()) + "\"");
                } else {
                    out.print("value=\"\"");
                }
                out.println("/>");
                out.println("                </dd>");
                out.println("                </dl>");
                out.println("            </dd>");
                out.println("            </dl>");
                out.println("        </dd>");
                out.println("<script language=\"JavaScript\" type=\"text/javascript\">\n" + "function updateDestFolder()\n" + "{\n" + "   if ( ! document.getElementById('altdestfolder').checked ) {\n" + "      document.getElementById('destfoldername').disabled = true;\n" + "   } else {\n" + "      document.getElementById('destfoldername').disabled = false;\n" + "   }\n" + "}\n" + "updateDestFolder();\n" + "</script>");
            }
            out.println("        <dt>Parental Rating:</dt>");
            out.println("        <dd>");
            out.println("            <select name=\"parentalrating\">");
            out.print("                <option value=\"\" ");
            if ((parentalRating == null) || (parentalRating == "")) {
                out.print("selected=\"selected\"");
            }
            out.println(">Any</option>");
            for (int i = 0; i < Favorite.PARENTAL_RATINGS.length; i++) {
                out.print("                <option value=\"" + Favorite.PARENTAL_RATINGS[i] + "\" ");
                if (Favorite.PARENTAL_RATINGS[i].equals(parentalRating)) {
                    out.print("selected=\"selected\"");
                }
                out.println(">" + Translate.encode(Favorite.PARENTAL_RATINGS[i]) + "</option>");
            }
            out.println("            </select>");
            out.println("        </dd>");
            out.println("        <dt>Rated:</dt>");
            out.println("        <dd>");
            out.println("            <select name=\"rated\">");
            out.print("                <option value=\"\" ");
            if ((rated == null) || (rated == "")) {
                out.print("selected=\"selected\"");
            }
            out.println(">Any</option>");
            for (int i = 0; i < Favorite.RATINGS.length; i++) {
                out.print("                <option value=\"" + Favorite.RATINGS[i] + "\" ");
                if (Favorite.RATINGS[i].equals(rated)) {
                    out.print("selected=\"selected\"");
                }
                out.println(">" + Translate.encode(Favorite.RATINGS[i]) + "</option>");
            }
            out.println("            </select>");
            out.println("        </dd>");
            out.println("        <dt>Time Slot:</dt>");
            out.println("        <dd>");
            out.println("            Day: ");
            out.println("            <select name=\"day\">");
            out.print("                <option value=\"\" ");
            if ((day == null) || (day == "")) {
                out.print("selected=\"selected\"");
            }
            out.println(">Any</option>");
            for (int i = 0; i < Favorite.DAYS.length; i++) {
                out.print("                <option value=\"" + Favorite.DAYS[i] + "\" ");
                if (Favorite.DAYS[i].equals(day)) {
                    out.print("selected=\"selected\"");
                }
                out.println(">" + Translate.encode(Favorite.DAYS[i]) + "</option>");
            }
            out.println("            </select>");
            List<String> timesList = null;
            if (time.indexOf(":") > -1) {
                timesList = Arrays.asList(Favorite.TIMES_24);
            } else {
                timesList = Arrays.asList(Favorite.TIMES_AM_PM);
            }
            out.println("            Time: ");
            out.println("            <select name=\"time\">");
            out.print("                <option value=\"\" ");
            if ((time == null) || (time == "")) {
                out.print("selected=\"selected\"");
            }
            out.println(">Any</option>");
            for (int i = 0; i < timesList.size(); i++) {
                out.print("                <option value=\"" + timesList.get(i).toString() + "\" ");
                if (timesList.get(i).toString().equals(time)) {
                    out.print("selected=\"selected\"");
                }
                out.println(">" + Translate.encode(timesList.get(i).toString()) + "</option>");
            }
            out.println("            </select>");
            out.println("        </dd>");
            Object favoritesList = SageApi.Api("GetFavorites");
            favoritesList = SageApi.Api("Sort", new Object[] { favoritesList, Boolean.FALSE, "FavoritePriority" });
            int favoritesSize = SageApi.Size(favoritesList);
            Favorite lastFavorite = (favoritesSize > 0) ? new Favorite(SageApi.GetElement(favoritesList, favoritesSize - 1)) : null;
            boolean isLastFavorite = ((favorite != null) && (lastFavorite != null) && (favorite.getID() == lastFavorite.getID()));
            if ((favoritesSize > 0) && !((favoritesSize == 1) && (favorite != null) && (new Favorite(SageApi.GetElement(favoritesList, 0)).getID() == favorite.getID()))) {
                out.println("        <dt>Priority:</dt>");
                out.println("        <dd>");
                out.println("            <select name=\"favoritepriorityrelation\">");
                if (isNew) {
                    out.print("                <option value=\"default\" ");
                    out.print("selected=\"selected\"");
                    out.println(">Default</option>");
                }
                out.print("                <option value=\"Above\" ");
                if (!isNew && !isLastFavorite) {
                    out.print("selected=\"selected\"");
                }
                out.println(">Above</option>");
                out.print("                <option value=\"Below\" ");
                if (!isNew && isLastFavorite) {
                    out.print("selected=\"selected\"");
                }
                out.println(">Below</option>");
                out.println("            </select>");
                out.println("&nbsp;");
                out.println("            <select name=\"relativefavoriteid\">");
                int thisFavoritePriority = -2;
                if (isNew) out.print("                <option value=\"-1\" selected=\"selected\"></option>");
                for (int i = 0; i < favoritesSize; i++) {
                    Favorite currentFavorite = new Favorite(SageApi.GetElement(favoritesList, i));
                    if ((favorite == null) || (favorite.getID() != currentFavorite.getID())) {
                        out.print("                <option value=\"" + currentFavorite.getID() + "\" ");
                        if (!isNew && (i == thisFavoritePriority + 1) || (isLastFavorite && (i == favoritesSize - 2))) {
                            out.print("selected=\"selected\"");
                        }
                        out.println(">" + (i + 1) + ". " + Translate.encode(currentFavorite.getDescription()) + "</option>");
                    } else {
                        thisFavoritePriority = i;
                    }
                }
                out.println("            </select>");
                out.println("        </dd>");
            }
            out.println("        </dl>");
            if (!isNew) {
                out.println("        <p>Internal details: FavoriteID=" + id + "</p>");
            }
            out.println("        <input type=\"submit\" value=\"Save Favorite\"/>");
            out.println("    </form>");
            out.println("</div>");
            out.println("<div id=\"commands\">");
            out.println("    <ul>");
            if (!isNew) {
                out.println("        <li>");
                out.print("            <a href=\"Search?" + favorite.getIdArg() + "&amp;TimeRange=-1\">");
                out.print("All Airings");
                out.println("</a>");
                out.println("        </li>");
                out.println("        <li>");
                out.print("            <a href=\"Search?" + favorite.getIdArg() + "&amp;TimeRange=-999\">");
                out.print("Past Airings");
                out.println("</a>");
                out.println("        </li>");
                out.println("        <li>");
                out.print("            <a href=\"Search?" + favorite.getIdArg() + "&amp;TimeRange=0\">");
                out.print("Future Airings");
                out.println("</a>");
                out.println("        </li>");
                out.println("        <li>");
                out.print("            <a href=\"Search?" + favorite.getIdArg() + "&amp;searchType=TVFiles\">");
                out.print("Recorded Airings");
                out.println("</a>");
                out.println("        </li>");
                out.println("        <li>");
                out.print("            <input type=\"button\" onclick=\"javascript:confirmAction('Are you sure you want to remove this favorite?'," + "'FavoriteCommand?command=Remove&amp;");
                favorite.printIdArg(out);
                out.print("&amp;returnto=Favorites')\"");
                out.print(" value=\"Remove Favorite\"");
                out.println("        </li>");
            } else {
                out.println("        <li>");
                out.println("            (No Commands)");
                out.println("        </li>");
            }
            if (favorite != null && favorite.getID() > 0) out.println("<li><a href=\"props_edit.groovy?type=fav&id=" + favorite.getID() + "\">Edit Properties</a></li>");
            out.println("    </ul>");
            out.println("</div>");
            printFooter(req, out);
            out.println("</div>");
            printMenu(req, out);
            out.println("</body>");
            out.println("</html>");
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
