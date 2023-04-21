    public void nowAndNext(HTTPurl urlData, HashMap<String, String> headers, OutputStream out) throws Exception {
        XmlDoc xmlDoc = new XmlDoc("epg_now_next");
        GuideStore guide = GuideStore.getInstance();
        Vector<String[]> chanMap = guide.getChannelMap();
        Set<String> wsChannels = store.getChannels().keySet();
        Date now = new Date();
        Calendar startTime = Calendar.getInstance();
        for (int y = 0; y < chanMap.size(); y++) {
            String[] map = (String[]) chanMap.get(y);
            if (wsChannels.contains(map[0])) {
                Element channel = xmlDoc.createElement("channel");
                channel.setAttribute("epg_channel", map[1]);
                channel.setAttribute("ws_channel", map[0]);
                GuideItem[] items = guide.getProgramsForChannel(map[1]);
                for (int x = 0; x < items.length; x++) {
                    GuideItem gitem = items[x];
                    startTime.setTime(gitem.getStart());
                    if (gitem.getStart().before(now) && gitem.getStop().after(now)) {
                        Element elmNow = xmlDoc.createElement("now");
                        addGuideItem(items[x], elmNow, xmlDoc.getDoc());
                        channel.appendChild(elmNow);
                        if (x + 1 < items.length) {
                            Element elmNext = xmlDoc.createElement("next");
                            addGuideItem(items[x + 1], elmNext, xmlDoc.getDoc());
                            channel.appendChild(elmNext);
                        }
                        break;
                    }
                    if (gitem.getStart().after(now)) {
                        Element elmNext = xmlDoc.createElement("next");
                        addGuideItem(gitem, elmNext, xmlDoc.getDoc());
                        channel.appendChild(elmNext);
                        break;
                    }
                }
                xmlDoc.getRoot().appendChild(channel);
            }
        }
        out.write(xmlDoc.getDocBytes());
    }
