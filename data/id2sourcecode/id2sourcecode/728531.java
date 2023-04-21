    private void addNOWandNEXT(DataStore store, Element item, Document doc) {
        GuideStore guide = GuideStore.getInstance();
        Vector chanMap = guide.getChannelMap();
        Set wsChannels = store.getChannels().keySet();
        Date now = new Date();
        Calendar startTime = Calendar.getInstance();
        for (int y = 0; y < chanMap.size(); y++) {
            String[] map = (String[]) chanMap.get(y);
            if (wsChannels.contains(map[0])) {
                Element channel = doc.createElement("channel");
                channel.setAttribute("epg_channel", map[1]);
                channel.setAttribute("ws_channel", map[0]);
                GuideItem[] items = guide.getProgramsForChannel(map[1]);
                for (int x = 0; x < items.length; x++) {
                    GuideItem gitem = items[x];
                    startTime.setTime(gitem.getStart());
                    if (gitem.getStart().before(now) && gitem.getStop().after(now)) {
                        Element elmNow = doc.createElement("now");
                        addGuideItem(items[x], elmNow, doc);
                        channel.appendChild(elmNow);
                        if (x + 1 < items.length) {
                            Element elmNext = doc.createElement("next");
                            addGuideItem(items[x + 1], elmNext, doc);
                            channel.appendChild(elmNext);
                        }
                        break;
                    }
                    if (gitem.getStart().after(now)) {
                        Element elmNext = doc.createElement("next");
                        addGuideItem(gitem, elmNext, doc);
                        channel.appendChild(elmNext);
                        break;
                    }
                }
                item.appendChild(channel);
            }
        }
    }
