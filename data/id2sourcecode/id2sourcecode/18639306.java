    public WanRenderable createFrom(HttpServletRequest request) {
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrCache vdrC = (VdrCache) getServletContext().getAttribute(VdrCache.class.getSimpleName());
        VdrUser vu = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        VdrConfigShowChannels vcsc = vdrP.fcVdrConfigShowChannels(vu);
        if (vcsc.getVcsc() != null) {
            logger.debug(vcsc.getVcsc().size() + "Einträge");
        } else {
            logger.debug("vcsc==null");
        }
        WanForm wf = new WanForm();
        wf.setRightButtonTitel("Save");
        wf.setAction(layerServletPath + "/" + getLayerId());
        WanFormFieldSet wffs = new WanFormFieldSet(JvdrTranslation.get("settings", "showchannel"));
        wf.addFieldSet(wffs);
        for (Channel c : vdrC.getChannelList()) {
            WanFormInputCheckBox box = new WanFormInputCheckBox(c.getChannelNumber() + ": " + c.getName());
            box.setName("c" + c.getChannelNumber());
            box.setId("id" + c.getChannelNumber());
            box.setChecked(vcsc.showChannel(c.getChannelNumber(), true));
            wffs.addInput(box);
        }
        return wf;
    }
