    private Baike getOldBaikeFromBaikeByNameAndYear(Baike baike) {
        Baike oldBaike = null;
        String name = baike.getName();
        String directors = baike.getDirectors();
        String year = baike.getYear();
        String channelCode = baike.getChannelCode();
        Filter f1 = new Filter("name", name, Filter.EQ);
        Filter f2 = new Filter("alias", name, Filter.LIKE);
        CriteriaInfo ci = new CriteriaInfo();
        ci.or(f1, f2);
        if (StringUtils.isNotEmpty(year)) {
            ci.eq("year", year);
        }
        ci.eq("channelCode", channelCode);
        List<Baike> baikeList = universalBo.getEntitiesByCriteriaInfo(Baike.class, ci);
        if (baikeList.size() > 1) {
            ci = new CriteriaInfo();
            ci.eq("name", name);
            if (StringUtils.isNotEmpty(year)) {
                ci.eq("year", year);
            }
            ci.eq("channelCode", channelCode);
            baikeList = universalBo.getEntitiesByCriteriaInfo(Baike.class, ci);
        }
        if (baikeList.size() > 1) {
            ci = new CriteriaInfo();
            ci.eq("name", name);
            if (StringUtils.isNotEmpty(directors)) {
                ci.eq("directors", directors);
            }
            if (StringUtils.isNotEmpty(year)) {
                ci.eq("year", year);
            }
            ci.eq("channelCode", channelCode);
            baikeList = universalBo.getEntitiesByCriteriaInfo(Baike.class, ci);
        }
        if (baikeList.size() > 1) {
            throw new RuntimeException("新百科无法与老百科合并！" + baike.getSourceSite() + "-" + baike.getId());
        }
        if (baikeList.size() == 1) {
            oldBaike = baikeList.get(0);
            return oldBaike;
        }
        return oldBaike;
    }
