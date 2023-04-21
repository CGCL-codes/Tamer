    private Object filterShowList(Object searchResults) throws Exception {
        if (manrec != null && !manrec.equals("any") && SageApi.Size(searchResults) > 0) {
            searchResults = SageApi.Api("FilterByBoolMethod", new Object[] { searchResults, "IsManualRecord", new Boolean(manrec.equalsIgnoreCase("set")) });
        }
        if (watched != null && !watched.equals("any") && SageApi.Size(searchResults) > 0) {
            searchResults = SageApi.Api("FilterByBoolMethod", new Object[] { searchResults, "IsWatched", new Boolean(watched.equalsIgnoreCase("set")) });
        }
        if (dontlike != null && !dontlike.equals("any") && SageApi.Size(searchResults) > 0) {
            searchResults = SageApi.Api("FilterByBoolMethod", new Object[] { searchResults, "IsDontLike", new Boolean(dontlike.equalsIgnoreCase("set")) });
        }
        if (favorite != null && !favorite.equals("any") && SageApi.Size(searchResults) > 0) {
            searchResults = SageApi.Api("FilterByBoolMethod", new Object[] { searchResults, "IsFavorite", new Boolean(favorite.equalsIgnoreCase("set")) });
        }
        if (firstRuns != null && !firstRuns.equals("any") && SageApi.Size(searchResults) > 0) {
            searchResults = SageApi.Api("FilterByBoolMethod", new Object[] { searchResults, "IsShowFirstRun", new Boolean(firstRuns.equalsIgnoreCase("set")) });
        }
        if (hdtv != null && !hdtv.equals("any") && SageApi.Size(searchResults) > 0) {
            try {
                searchResults = SageApi.Api("FilterByBoolMethod", new Object[] { searchResults, "IsAiringHDTV", new Boolean(hdtv.equalsIgnoreCase("set")) });
            } catch (InvocationTargetException e) {
            }
        }
        if (isTimeFiltered() && SageApi.Size(searchResults) > 0) {
            searchResults = SageApi.Api("FilterByRange", new Object[] { searchResults, "GetAiringEndTime", getStarttime(), getEndtime(), Boolean.TRUE });
        }
        if (getCategories() != null && getCategories().length > 0 && SageApi.Size(searchResults) > 0) {
            List<String> categories_l = Arrays.asList(getCategories());
            if (categories_l.contains("**Any**")) {
                if (getCategories().length > 1) setCategories(new String[] { "**Any**" });
            } else {
                Object allFiltered = null;
                for (Iterator<String> it = categories_l.iterator(); it.hasNext(); ) {
                    Object filtered = SageApi.Api("DataUnion", new Object[] { searchResults });
                    String category = URLDecoder.decode((String) it.next(), charset);
                    filtered = SageApi.Api("FilterByMethod", new Object[] { filtered, "GetShowCategory", category, Boolean.TRUE });
                    allFiltered = SageApi.Api("DataUnion", new Object[] { allFiltered, filtered });
                }
                searchResults = allFiltered;
            }
        }
        if (getChannels() != null && getChannels().length > 0 && SageApi.Size(searchResults) > 0) {
            List<String> channels_l = Arrays.asList(getChannels());
            if (channels_l.contains("**Any**")) {
                if (getChannels().length > 1) setChannels(new String[] { "**Any**" });
            } else {
                Object allFiltered = null;
                for (Iterator<String> it = channels_l.iterator(); it.hasNext(); ) {
                    String channel = (String) it.next();
                    try {
                        Integer chID = new Integer(channel);
                        Object channelObj = SageApi.Api("GetChannelForStationID", new Object[] { chID });
                        if (channelObj != null) {
                            Object filtered = SageApi.Api("DataUnion", new Object[] { searchResults });
                            filtered = SageApi.Api("FilterByMethod", new Object[] { filtered, "GetChannel", channelObj, Boolean.TRUE });
                            allFiltered = SageApi.Api("DataUnion", new Object[] { allFiltered, filtered });
                        }
                    } catch (Exception e) {
                        Serve.extLog("error filtering by channel in search: " + e.toString());
                        e.printStackTrace(System.out);
                    }
                }
                searchResults = allFiltered;
            }
        }
        return searchResults;
    }
