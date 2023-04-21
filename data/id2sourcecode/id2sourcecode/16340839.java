    public void createRepeats(List<EPGEntry> lEpgRepeats, VdrCache vdrC) {
        if (lEpgRepeats.size() > 0) {
            WanMenu wm = new WanMenu();
            wm.setMenuType(WanMenu.MenuType.SIMPLE);
            wm.setDiv(false);
            wm.setTitle("Wiederholung Aufnehmen");
            for (EPGEntry eRepeat : lEpgRepeats) {
                StringBuffer sbR = new StringBuffer();
                sbR.append(DateUtil.dayName(eRepeat.getStartTime(), 2));
                sbR.append(", " + DateUtil.tm(eRepeat.getStartTime()));
                sbR.append(": " + DateUtil.sm(eRepeat.getStartTime()));
                WanMenuEntry wmi = new WanMenuEntry();
                wmi.setHeader(eRepeat.getChannelName());
                wmi.setName(sbR.toString());
                HtmlHref href = lyrEpgTimer.getLayerTarget();
                href.setRev(HtmlHref.Rev.async);
                href.addHtPa("rep", 0);
                href.addHtPa("chNu", vdrC.getChNum(eRepeat.getChannelID()));
                href.addHtPa("st", eRepeat.getStartTime().getTimeInMillis());
                wmi.setHtmlref(href);
                wm.addItem(wmi);
            }
            alWanRenderables.add(wm);
        }
    }
