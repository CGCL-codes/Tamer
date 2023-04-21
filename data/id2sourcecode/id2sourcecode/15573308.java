    protected Vector<Object> getChannels(HttpServletRequest req, int numchans) throws Exception {
        Vector<Object> rv = new Vector<Object>();
        String[] Channels = req.getParameterValues("Channels");
        if (Channels == null) {
            Integer StartChanID = null;
            try {
                String s = req.getParameter("startchan");
                StartChanID = new Integer(s);
            } catch (Exception e) {
            }
            Object StartChanObj = null;
            if (StartChanID != null) {
                StartChanObj = SageApi.Api("GetChannelForStationID", new Object[] { StartChanID });
                if (StartChanObj != null && !SageApi.booleanApi("IsChannelViewable", new Object[] { StartChanObj })) {
                    StartChanObj = null;
                }
            }
            Object channellist = SageApi.Api("GetAllChannels");
            channellist = SageApi.Api("FilterByBoolMethod", new Object[] { channellist, "IsChannelViewable", Boolean.TRUE });
            channellist = SageApi.Api("Sort", new Object[] { channellist, Boolean.FALSE, "GetChannelNumber" });
            int channum = 0;
            if (StartChanObj != null) for (; channum < SageApi.Size(channellist); channum++) if (SageApi.GetElement(channellist, channum) == StartChanObj) break;
            if (channum > SageApi.Size(channellist)) channum = 0;
            for (; channum < SageApi.Size(channellist) && rv.size() < numchans; channum++) rv.add(SageApi.GetElement(channellist, channum));
        } else {
            for (int i = 0; i < Channels.length; i++) {
                Integer chID = new Integer(Channels[i]);
                Object channelObj = SageApi.Api("GetChannelForStationID", new Object[] { chID });
                if (channelObj != null) rv.add(channelObj);
            }
        }
        return rv;
    }
