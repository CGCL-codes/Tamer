    public void createTimerOverview() {
        VdrCache vdrC = (VdrCache) this.getServletContext().getAttribute(VdrCache.class.getSimpleName());
        Configuration config = (Configuration) this.getServletContext().getAttribute(Configuration.class.getSimpleName());
        VdrDataFetcherSvdrp vdrD = new VdrDataFetcherSvdrp(config);
        vdrC.fetchTimer(vdrD);
        List<VDRTimer> lTimer = vdrC.getLTimer();
        if (lTimer.size() > 0) {
            Date d = new Date();
            WanDiv wd = new WanDiv();
            wd.setDivclass(WanDiv.DivClass.iBlock);
            String style = "float:right;margin:0 5px";
            style = "";
            WanParagraph wp = new WanParagraph();
            wp.setContent("<img src=\"async/timerchart?" + d.getTime() + "\" style=\"" + style + "\" />");
            wd.addContent(wp);
            alWanRenderables.add(wd);
            WanMenu wm = new WanMenu();
            wm.setMenuType(WanMenu.MenuType.IMAGE);
            for (VDRTimer timer : lTimer) {
                StringBuffer sbHeader = new StringBuffer();
                sbHeader.append(DateUtil.dayName(timer.getStartTime()));
                sbHeader.append(", " + DateUtil.tmj(timer.getStartTime()));
                sbHeader.append(", " + DateUtil.sm(timer.getStartTime()));
                sbHeader.append(" - " + DateUtil.sm(timer.getEndTime()));
                HtmlHref href = lyrEpgTimer.getLayerTarget();
                href.setRev(HtmlHref.Rev.async);
                href.addHtPa("tid", timer.getID());
                WanMenuEntry wmi = new WanMenuEntry();
                wmi.setName(timer.getTitle());
                wmi.setHtmlref(href);
                wmi.setHeader(sbHeader.toString());
                wmi.setFooter(vdrC.getChannel(timer.getChannelNumber()).getName());
                wm.addItem(wmi);
            }
            wd = new WanDiv();
            wd.setDivclass(WanDiv.DivClass.iMenu);
            wd.addContent(wm);
            alWanRenderables.add(wd);
        } else {
            WanDiv wd = new WanDiv();
            wd.setDivclass(WanDiv.DivClass.iBlock);
            WanParagraph wp = new WanParagraph();
            wp.setContent("Keine Timer definiert!");
            wd.addContent(wp);
            alWanRenderables.add(wd);
        }
    }
