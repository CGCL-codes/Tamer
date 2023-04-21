    public void createSmartEpgOverview(HttpServletRequest request) {
        VdrPersistence vdrP = (VdrPersistence) this.getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrCache vdrC = (VdrCache) this.getServletContext().getAttribute(VdrCache.class.getSimpleName());
        VdrUser vu = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        List<VdrSmartSearch> lVss = vdrP.lVdrSmartSearch(vu);
        List<EPGEntry> lSmartEpg = new ArrayList<EPGEntry>();
        Hashtable<String, Integer> htSmartHits = new Hashtable<String, Integer>();
        Hashtable<String, VdrSmartSearch> htSmartSearch = new Hashtable<String, VdrSmartSearch>();
        EPGEntryComarator epgComparator = new EPGEntryComarator();
        Comparator<EPGEntry> comparator = epgComparator.getComparator(EPGEntryComarator.CompTyp.DateDown);
        for (VdrSmartSearch vss : lVss) {
            List<EPGEntry> lSepg = vdrC.getSmartEpg(vss);
            Collections.sort(lSepg, comparator);
            if (vss.isAktiv()) {
                if (lSepg.size() > 0) {
                    Collections.sort(lSepg, comparator);
                    EPGEntry epg = lSepg.get(0);
                    htSmartHits.put(epg.getTitle(), lSepg.size());
                    htSmartSearch.put(epg.getTitle(), vss);
                    lSmartEpg.add(epg);
                }
            }
        }
        if (lSmartEpg.size() > 0) {
            WanMenu wm = new WanMenu();
            wm.setMenuType(WanMenu.MenuType.IMAGE);
            Collections.sort(lSmartEpg, comparator);
            for (EPGEntry epg : lSmartEpg) {
                VdrSmartSearch vss = htSmartSearch.get(epg.getTitle());
                WanMenuEntry wi = new WanMenuEntry();
                wi.setName(vss.getSuche());
                StringBuffer sbHeader = new StringBuffer();
                sbHeader.append(epg.getChannelName() + ":");
                sbHeader.append(" " + DateUtil.dayName(epg.getStartTime().getTime(), 2));
                sbHeader.append(" " + DateUtil.tm(epg.getStartTime().getTime()));
                sbHeader.append(" " + DateUtil.sm(epg.getStartTime().getTime()));
                wi.setHeader(sbHeader.toString());
                wi.setFooter(htSmartHits.get(epg.getTitle()) + " Treffer");
                wm.addItem(wi);
            }
            WanDiv wd = new WanDiv();
            wd.setDivclass(WanDiv.DivClass.iMenu);
            wd.addContent(wm);
            alWanRenderables.add(wd);
        } else {
            WanDiv wd = new WanDiv();
            wd.setDivclass(WanDiv.DivClass.iBlock);
            wd.addContent(new WanParagraph("Keine Eintr�ge"));
            alWanRenderables.add(wd);
        }
    }
