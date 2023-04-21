    public WanRenderable createFormFromEpg(HttpServletRequest request, ServletForm form, VdrCache vdrC, VdrConfigTimer vcr) {
        String chNu = form.get("chNu");
        String st = form.get("st");
        EPGEntry epg = vdrC.getEpg(chNu, st);
        WanForm wf = new WanForm();
        wf.setRightButtonTitel("Save");
        wf.setAction(layerServletPath + "/" + getLayerId());
        WanFormFieldSet wffs = new WanFormFieldSet();
        wf.addFieldSet(wffs);
        WanFormSelect wfsBefore = new WanFormSelect("Beginn: " + DateUtil.sm(epg.getStartTime()), "before");
        int defaultBefore;
        if (vcr != null && vcr.isOwn()) {
            defaultBefore = vcr.getTimeBefore();
        } else {
            defaultBefore = config.getInt("vdr/timer/@minBefore");
        }
        boolean beforeSet = false;
        for (int i = 0; i < 10; i++) {
            int value = i * 5;
            WanFormSelectItem wfsi = new WanFormSelectItem("-" + value + " min", value + "");
            if (value == defaultBefore) {
                wfsi.setSelected(true);
                beforeSet = true;
            }
            wfsBefore.addItem(wfsi);
        }
        if (!beforeSet) {
            WanFormSelectItem wfsi = new WanFormSelectItem("- " + defaultBefore, defaultBefore + "");
            wfsi.setSelected(true);
            wfsBefore.addItem(wfsi);
        }
        wffs.addInput(wfsBefore);
        wffs.addInput(new WanFormInputHidden("chNu", chNu));
        wffs.addInput(new WanFormInputHidden("st", st));
        WanFormSelect wfsAfter = new WanFormSelect("Ende: " + DateUtil.sm(epg.getEndTime()), "after");
        int defaultAfter;
        if (vcr != null && vcr.isOwn()) {
            defaultAfter = vcr.getTimeAfter();
        } else {
            defaultAfter = config.getInt("vdr/timer/@minAfter");
        }
        boolean afterSet = false;
        for (int i = 0; i < 10; i++) {
            int value = i * 5;
            WanFormSelectItem wfsi = new WanFormSelectItem("+" + value + " min", value + "");
            if (value == defaultAfter) {
                wfsi.setSelected(true);
                afterSet = true;
            }
            wfsAfter.addItem(wfsi);
        }
        if (!afterSet) {
            WanFormSelectItem wfsi = new WanFormSelectItem("+" + defaultAfter + " min", defaultAfter + "");
            wfsi.setSelected(true);
            wfsAfter.addItem(wfsi);
        }
        wffs.addInput(wfsAfter);
        StringBuffer sbTitel = new StringBuffer();
        sbTitel.append(epg.getChannelName() + "<br/>");
        sbTitel.append("<b>" + epg.getTitle() + "</b><br/>");
        sbTitel.append(DateUtil.dayName(epg.getStartTime()) + ", " + DateUtil.tm(epg.getStartTime()));
        WanParagraph wp = new WanParagraph(sbTitel.toString());
        WanDiv div = new WanDiv();
        div.setDivclass(WanDiv.DivClass.iBlock);
        div.addContent(wp);
        wf.addDiv(div, WanDiv.Orientation.TOP);
        return wf;
    }
