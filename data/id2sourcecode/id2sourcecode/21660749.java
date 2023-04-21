    public Map<String, List<Float>> hhData() throws ProgrammerException, UserException {
        Map<String, List<Float>> map = new HashMap<String, List<Float>>();
        List<Float> importFromNet = new ArrayList<Float>();
        map.put("import-from-net", importFromNet);
        List<Float> exportToNet = new ArrayList<Float>();
        map.put("export-to-net", exportToNet);
        List<Float> importFromGen = new ArrayList<Float>();
        map.put("import-from-gen", importFromGen);
        List<Float> exportToGen = new ArrayList<Float>();
        map.put("export-to-gen", exportToGen);
        HhEndDate hhEndDate = getFrom();
        while (!hhEndDate.getDate().after(getTo().getDate())) {
            importFromNet.add(0f);
            exportToNet.add(0f);
            importFromGen.add(0f);
            exportToGen.add(0f);
            hhEndDate = hhEndDate.getNext();
        }
        for (Supply supply : getSupplies()) {
            for (Channel channel : supply.getChannels()) {
                if (!channel.getIsKwh()) {
                    continue;
                }
                List<Float> hhStream = null;
                if (supply.getSource().getCode().equals(SourceCode.NETWORK)) {
                    if (channel.getIsImport()) {
                        hhStream = importFromNet;
                    } else {
                        hhStream = exportToNet;
                    }
                } else {
                    if (channel.getIsImport()) {
                        hhStream = importFromGen;
                    } else {
                        hhStream = exportToGen;
                    }
                }
                List<HhDatum> hhData = channel.getHhData(getFrom(), getTo());
                if (hhData.isEmpty()) {
                    continue;
                }
                int i = 0;
                int missing = 0;
                hhEndDate = getFrom();
                while (!hhEndDate.getDate().after(getTo().getDate())) {
                    HhDatum datum = null;
                    if (i - missing < hhData.size()) {
                        datum = hhData.get(i - missing);
                        if (!hhEndDate.equals(datum.getEndDate())) {
                            missing++;
                        } else {
                            hhStream.set(i, hhStream.get(i) + datum.getValue());
                        }
                    }
                    i++;
                    hhEndDate = hhEndDate.getNext();
                }
            }
        }
        return map;
    }
