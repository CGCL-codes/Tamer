    public WanRenderable createChannelOverview(HttpServletRequest request) {
        alWanRenderables.clear();
        VdrPersistence vdrP = (VdrPersistence) this.getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrCache vdrC = (VdrCache) this.getServletContext().getAttribute(VdrCache.class.getSimpleName());
        VdrUser vu = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        VdrConfigShowChannels vcsc = vdrP.fcVdrConfigShowChannels(vu);
        List<Channel> lC = vdrC.getChannelList();
        WanMenu wm = new WanMenu();
        wm.setMenuType(WanMenu.MenuType.SIMPLE);
        for (Channel ch : lC) {
            int chnu = ch.getChannelNumber();
            if (vcsc.showChannel(chnu, true)) {
                WanMenuEntry wi = new WanMenuEntry();
                wi.setName(ch.getName());
                HtmlHref href = lyrChannelEpg.getLayerTarget();
                href.setRev(HtmlHref.Rev.async);
                href.addHtPa("chNu", chnu);
                href.addHtPa("first", true);
                wi.setHtmlref(href);
                wm.addItem(wi);
            }
        }
        return wm;
    }
