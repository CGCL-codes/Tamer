    public void addHealingEventAndSubcategory(String[] subco, long healing, long overhealing, boolean isCrit) {
        this.addHealingEvent(healing, overhealing, isCrit);
        if (subco.length < 1) return;
        if (this.subcategory == null) this.subcategory = new ArrayList<HPSStats>();
        HPSStats t2 = new HPSStats(subco[0]);
        HPSStats t1 = (HPSStats) CollectionUtils.find(this.subcategory, new RRAPredicate(t2));
        if (t1 == null) {
            t1 = t2;
            this.subcategory.add(t1);
        }
        if (subco.length == 1) t1.addHealingEvent(healing, overhealing, isCrit); else {
            String[] newsubco = new String[subco.length - 1];
            for (int i = 0; i < newsubco.length; i++) newsubco[i] = subco[i + 1];
            t1.addHealingEventAndSubcategory(newsubco, healing, overhealing, isCrit);
        }
    }
