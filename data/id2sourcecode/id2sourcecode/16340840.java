    public void createEpgDetail(ServletForm form, int epgRepeats, HttpSession session, VdrPersistence vdrP) {
        VdrUser vu = (VdrUser) session.getAttribute(VdrUser.class.getSimpleName());
        VdrConfigTimer vcr = vdrP.fVdrConfigTimer(vu);
        VdrCache vdrC = (VdrCache) this.getServletContext().getAttribute(VdrCache.class.getSimpleName());
        String chNu = form.get("chNu");
        String st = form.get("st");
        EPGEntry epg = vdrC.getEpg(chNu, st);
        String style = "float:center;margin:5px";
        String recordIcon = config.getString("icons/icon[@type='record']");
        if (isTimerConflict(vcr, epg, new Integer(chNu), vdrC)) {
            recordIcon = config.getString("icons/icon[@type='recordwarn']");
        }
        HtmlHref hrefTimer = lyrEpgTimer.getLayerTarget();
        hrefTimer.setContent("<img src=\"" + config.getString("icons/@dir") + "/" + recordIcon + "\" style=\"" + style + "\" />");
        hrefTimer.setRev(HtmlHref.Rev.async);
        hrefTimer.addHtPa("chNu", chNu);
        hrefTimer.addHtPa("st", st);
        HtmlHref hrefSmart = lyrSmartEpg.getLayerTarget();
        hrefSmart.setContent("<img src=\"" + config.getString("icons/@dir") + "/" + config.getString("icons/icon[@type='smart']") + "\" style=\"" + style + "\" />");
        hrefSmart.setRev(HtmlHref.Rev.async);
        hrefSmart.addHtPa("chNu", chNu);
        hrefSmart.addHtPa("st", st);
        hrefSmart.addHtPa("new", true);
        String badgeId = "search";
        HtmlHref hrefRepeat = this.getLayerTarget();
        hrefRepeat.setContent("<img src=\"async/badge?badgeId=" + badgeId + "&number=" + epgRepeats + "\" style=\"" + style + "\" />");
        hrefRepeat.addHtPa("chNu", vdrC.getChNum(epg.getChannelID()));
        hrefRepeat.addHtPa("st", epg.getStartTime().getTimeInMillis());
        hrefRepeat.addHtPa("r", true);
        hrefRepeat.setRev(HtmlHref.Rev.async);
        Badge badge = new Badge();
        badge.setSourceImage("resources/images/src/system-search.svg");
        badge.setBadgeImage("resources/images/src/badge-search.svg");
        badge.setNumber(epgRepeats);
        session.removeAttribute(badgeId);
        session.setAttribute(badgeId, badge);
        badgeId = "image";
        int anzBilder;
        try {
            VdrImageSearch vis = vdrP.fVdrImageSearch(epg.getTitle());
            anzBilder = vis.getThumbnails().size();
        } catch (NoResultException e) {
            anzBilder = 0;
        }
        HtmlHref hrefImage = lyrImgSelect.getLayerTarget();
        hrefImage.setContent("<img src=\"async/badge?badgeId=" + badgeId + "&number=" + anzBilder + "\" style=\"" + style + "\" />");
        hrefImage.addHtPa("chNu", vdrC.getChNum(epg.getChannelID()));
        hrefImage.addHtPa("st", epg.getStartTime().getTimeInMillis());
        hrefImage.setRev(HtmlHref.Rev.async);
        badge = new Badge();
        badge.setSourceImage("resources/images/src/image-x-generic.svg");
        badge.setBadgeImage("resources/images/src/badge-image.svg");
        badge.setNumber(anzBilder);
        session.removeAttribute(badgeId);
        session.setAttribute(badgeId, badge);
        StringBuffer sbTitel = new StringBuffer();
        sbTitel.append(epg.getChannelName() + "<br/>");
        sbTitel.append("<b>" + epg.getTitle() + "</b><br/>");
        sbTitel.append(DateUtil.dayName(epg.getStartTime()) + " " + DateUtil.sm(epg.getStartTime()) + " - " + DateUtil.sm(epg.getEndTime()) + "<br/>");
        WanParagraph wpProgramInfo = new WanParagraph(sbTitel.toString());
        StringBuffer sbAction = new StringBuffer();
        sbAction.append(hrefTimer.render());
        sbAction.append(hrefSmart.render());
        sbAction.append(hrefRepeat.render());
        sbAction.append(hrefImage.render());
        WanParagraph wpUserAction = new WanParagraph(sbAction.toString());
        WanDiv wdRepeat = new WanDiv(WanDiv.DivClass.iMenu);
        wdRepeat.setId(getLayerId() + "R");
        alWanRenderables.add(wdRepeat);
        WanDiv wd = new WanDiv(WanDiv.DivClass.iBlock);
        wd.addContent(wpProgramInfo);
        wd.addContent(wpUserAction);
        wd.addContent(new WanParagraph(epg.getDescription()));
        alWanRenderables.add(wd);
    }
