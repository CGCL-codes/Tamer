    private byte[] updateChannel(HTTPurl urlData) throws Exception {
        int freq = 0;
        int band = 0;
        int progID = -1;
        int videoid = -1;
        int audioid = -1;
        int audioType = -1;
        int capType = -1;
        String name = "";
        String oldName = urlData.getParameter("oldName");
        name = urlData.getParameter("name");
        if (name == null) name = "none";
        name = name.trim();
        name = checkName(name);
        GuideStore guide = GuideStore.getInstance();
        try {
            freq = Integer.parseInt(urlData.getParameter("freq").trim());
            band = Integer.parseInt(urlData.getParameter("band").trim());
            progID = Integer.parseInt(urlData.getParameter("programid").trim());
            videoid = Integer.parseInt(urlData.getParameter("videoid").trim());
            audioid = Integer.parseInt(urlData.getParameter("audioid").trim());
            audioType = Integer.parseInt(urlData.getParameter("audioType").trim());
            capType = Integer.parseInt(urlData.getParameter("captureType").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (progID > -1) {
            Channel chan = new Channel(name, freq, band, progID, videoid, audioid);
            chan.setAudioType(audioType);
            chan.setCaptureType(capType);
            store.removeChannel(oldName);
            store.addChannel(chan);
            boolean save = false;
            Vector chanMap = guide.getChannelMap();
            for (int x = 0; x < chanMap.size(); x++) {
                String[] map = (String[]) chanMap.get(x);
                if (map[0].equals(oldName)) {
                    map[0] = name;
                    save = true;
                }
            }
            if (save) guide.saveChannelMap(null);
        }
        store.saveChannels(null);
        StringBuffer out = new StringBuffer(256);
        out.append("HTTP/1.0 302 Moved Temporarily\n");
        out.append("Location: /servlet/" + urlData.getServletClass() + "\n\n");
        return out.toString().getBytes();
    }
