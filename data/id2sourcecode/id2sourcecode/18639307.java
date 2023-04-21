    public void processSubmittedForm(HttpServletRequest request) {
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrCache vdrC = (VdrCache) getServletContext().getAttribute(VdrCache.class.getSimpleName());
        VdrUser vu = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        VdrConfigShowChannels vcsc = vdrP.fcVdrConfigShowChannels(vu);
        ServletForm form = new ServletForm(request);
        for (Channel c : vdrC.getChannelList()) {
            int chnu = c.getChannelNumber();
            boolean show = form.getBoolean("c" + chnu, false);
            VdrConfigShowChannel v = vcsc.fVdrConfigShowChannel(chnu);
            if (v == null) {
                v = new VdrConfigShowChannel();
                v.setChnu(chnu);
                v.setVdrconfigchannel(vcsc);
                v.setShow(show);
                vcsc.add(v);
            } else {
                v.setShow(show);
            }
        }
        vcsc.setVcsc(vcsc.getVcsc());
        vdrP.updateObject(vcsc);
        WanDiv div = new WanDiv();
        div.setDivclass(WanDiv.DivClass.iBlock);
        div.addContent(new WanParagraph("Einstellungen der angezeigten Kanäle gespeichert"));
        alWanRenderables.add(div);
    }
