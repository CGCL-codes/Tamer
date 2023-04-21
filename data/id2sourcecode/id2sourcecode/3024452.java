    public WanRenderable createFormTimer(int tid, VdrCache vdrC) {
        VDRTimer timer = vdrC.getTimer(tid);
        WanForm wf = new WanForm();
        wf.setRightButtonTitel("Save");
        wf.setAction(layerServletPath + "/" + getLayerId());
        WanFormFieldSet wffs = new WanFormFieldSet();
        wf.addFieldSet(wffs);
        WanFormInputCheckBox wficb = new WanFormInputCheckBox("L�sche Timer");
        wficb.setName("deltimer");
        wffs.addInput(wficb);
        wffs.addInput(new WanFormInputHidden("tid", timer.getID()));
        int steps[] = { -25, -10, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 10, 25 };
        WanFormSelect wfsBefore = new WanFormSelect("Beginn: " + DateUtil.sm(timer.getStartTime()), "before");
        for (int value : steps) {
            WanFormSelectItem wfsi = new WanFormSelectItem(value + " min", value + "");
            if (value == 0) {
                wfsi.setSelected(true);
            }
            wfsBefore.addItem(wfsi);
        }
        wffs.addInput(wfsBefore);
        WanFormSelect wfsAfter = new WanFormSelect("Ende: " + DateUtil.sm(timer.getEndTime()), "after");
        for (int value : steps) {
            WanFormSelectItem wfsi = new WanFormSelectItem(value + " min", value + "");
            if (value == 0) {
                wfsi.setSelected(true);
            }
            wfsAfter.addItem(wfsi);
        }
        wffs.addInput(wfsAfter);
        StringBuffer sbTitel = new StringBuffer();
        sbTitel.append(vdrC.getChName(timer.getChannelNumber()) + "<br/>");
        sbTitel.append("<b>" + timer.getTitle() + "</b><br/>");
        sbTitel.append(DateUtil.dayName(timer.getStartTime()) + ", " + DateUtil.tm(timer.getStartTime()));
        WanParagraph wp = new WanParagraph(sbTitel.toString());
        WanDiv div = new WanDiv();
        div.setDivclass(WanDiv.DivClass.iBlock);
        div.addContent(wp);
        wf.addDiv(div, WanDiv.Orientation.TOP);
        return wf;
    }
