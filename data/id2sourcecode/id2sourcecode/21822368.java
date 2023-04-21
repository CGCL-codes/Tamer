    public WanRenderable createForm(HttpServletRequest request) {
        VdrCache vdrC = (VdrCache) this.getServletContext().getAttribute(VdrCache.class.getSimpleName());
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrUser vu = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        ServletForm form = new ServletForm(request);
        boolean isNew = form.getBoolean("new");
        logger.trace("Mode new?" + isNew);
        VdrSmartSearch vss;
        if (isNew) {
            int chNu = new Integer(form.get("chNu"));
            EPGEntry epg = vdrC.getEpg(chNu, form.get("st"));
            vss = new VdrSmartSearch();
            vss.setVdruser(vu);
            vss.setLimitToChannel(false);
            vss.setChannel(chNu);
            vss.setSuche(epg.getTitle());
            vss.setEpgStart(epg.getStartTime().getTime());
            vss.setLimitRange(false);
            vss.setRangeDown(config.getInt("vdr/smartepg/@rangeDown"));
            vss.setRangeUp(config.getInt("vdr/smartepg/@rangeUp"));
            vss.setAktiv(true);
        } else {
            int id = new Integer(form.get("vssid"));
            vss = (VdrSmartSearch) vdrP.findObject(VdrSmartSearch.class, id);
        }
        WanForm wf = new WanForm();
        wf.setRightButtonTitel("Save");
        wf.setAction(layerServletPath + "/" + getLayerId());
        WanFormFieldSet wffs = new WanFormFieldSet();
        wffs.setName("Allgemein");
        wf.addFieldSet(wffs);
        WanFormInputCheckBox boxChannel = new WanFormInputCheckBox(JvdrTranslation.get("epg", "onlyfor") + " \"" + vdrC.getChName(vss.getChannel()) + "\"");
        boxChannel.setName(VdrSmartSearch.Key.limitToChannel.toString());
        boxChannel.setId("idBc");
        boxChannel.setButtonTitle("JA|NEIN");
        boxChannel.setChecked(vss.isLimitToChannel());
        wffs.addInput(boxChannel);
        WanFormInputCheckBox boxAktiv = new WanFormInputCheckBox("SmartEpg aktiv");
        boxAktiv.setName(VdrSmartSearch.Key.aktiv.toString());
        boxAktiv.setId("idBa");
        boxAktiv.setButtonTitle("JA|NEIN");
        boxAktiv.setChecked(vss.isAktiv());
        wffs.addInput(boxAktiv);
        wffs.addInput(new WanFormInputHidden(VdrSmartSearch.Key.channel.toString(), vss.getChannel()));
        wffs.addInput(new WanFormInputHidden("epgStart", vss.getEpgStart().getTime()));
        wffs.addInput(new WanFormInputHidden("new", isNew));
        if (!isNew) {
            wffs.addInput(new WanFormInputHidden("vssid", vss.getId()));
        }
        WanFormFieldSet wffs2 = new WanFormFieldSet();
        wffs2.setName("Suche nach");
        wf.addFieldSet(wffs2);
        WanFormInputText textWort = new WanFormInputText();
        textWort.setName(VdrSmartSearch.Key.suche.toString());
        textWort.setPlaceholderText("Titelanfang eintragen");
        textWort.setDefaultText(vss.getSuche());
        wffs2.addInput(textWort);
        WanFormFieldSet wffs3 = new WanFormFieldSet();
        wffs3.setName("Zeiten");
        wf.addFieldSet(wffs3);
        WanFormInputCheckBox boxLimitRange = new WanFormInputCheckBox(JvdrTranslation.get("epg", "timeframe"));
        boxLimitRange.setName(VdrSmartSearch.Key.limitRange.toString());
        boxLimitRange.setId("idBl");
        boxLimitRange.setButtonTitle("JA|NEIN");
        boxLimitRange.setChecked(vss.isLimitRange());
        wffs3.addInput(boxLimitRange);
        WanFormInputText textStart = new WanFormInputText();
        textStart.setName("d1");
        textStart.setDefaultText("Beginn: " + DateUtil.dayName(vss.getEpgStart()) + ", " + DateUtil.tmj(vss.getEpgStart()) + " " + DateUtil.sm(vss.getEpgStart()));
        wffs3.addInput(textStart);
        WanFormSelect wfsDown = new WanFormSelect("Suchbereich vorher", VdrSmartSearch.Key.rangeDown.toString());
        String[] downSteps = config.getStringArray("vdr/smartepg/down/t");
        for (String downStep : downSteps) {
            int value = new Integer(downStep);
            WanFormSelectItem wfsi = new WanFormSelectItem("-" + value + " min", value);
            if (value == vss.getRangeDown()) {
                wfsi.setSelected(true);
            }
            wfsDown.addItem(wfsi);
        }
        wffs3.addInput(wfsDown);
        WanFormSelect wfsUp = new WanFormSelect("Suchbereich Nachher", VdrSmartSearch.Key.rangeUp.toString());
        String[] upSteps = config.getStringArray("vdr/smartepg/up/t");
        for (String upStep : upSteps) {
            int value = new Integer(upStep);
            WanFormSelectItem wfsi = new WanFormSelectItem("+" + value + " min", value);
            if (value == vss.getRangeUp()) {
                wfsi.setSelected(true);
            }
            wfsUp.addItem(wfsi);
        }
        wffs3.addInput(wfsUp);
        return wf;
    }
