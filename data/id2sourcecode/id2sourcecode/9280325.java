    @SkipValidation
    public String start() throws Exception {
        area = new HashMap<String, ArrayList<Datadictionary>>();
        provinces = new ArrayList<Datadictionary>();
        ArrayList<String> provinceStrList = (ArrayList<String>) this.datadictionaryManager.getDataDictionarybySQL("select distinct dataValue from Datadictionary where dataType='location'");
        for (int i = 0; i < provinceStrList.size(); i++) {
            Datadictionary datadictionary = new Datadictionary();
            datadictionary.setDataValue(provinceStrList.get(i));
            datadictionary.setDataType("location");
            provinces.add(datadictionary);
        }
        for (int i = 0; i < provinces.size(); i++) {
            ArrayList<Datadictionary> citys = (ArrayList<Datadictionary>) this.datadictionaryManager.getCitysBySQL(provinces.get(i).getDataValue());
            area.put(provinces.get(i).getDataValue(), citys);
        }
        this.skillMap = new HashMap<Integer, ArrayList<Skill>>();
        this.technologys = (ArrayList<Technology>) this.technologyManager.findTechnologyAll();
        for (int i = 0; i < this.technologys.size(); i++) {
            ArrayList<Skill> skills = (ArrayList<Skill>) this.skillManager.findSkillByProperty("technology", this.technologys.get(i));
            this.skillMap.put(this.technologys.get(i).getTechnologyId(), skills);
        }
        this.channelsourceMap = new HashMap<Integer, ArrayList<ChannelSource>>();
        this.channels = (ArrayList<Channel>) this.channelManager.findChannelAll();
        for (int i = 0; i < channels.size(); i++) {
            ArrayList<ChannelSource> sources = (ArrayList<ChannelSource>) this.channelSourceManager.findByProperty("channel", channels.get(i));
            this.channelsourceMap.put(channels.get(i).getChannelId(), sources);
        }
        return SUCCESS;
    }
