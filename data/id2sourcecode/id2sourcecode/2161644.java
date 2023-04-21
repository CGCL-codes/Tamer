    private void saveBrokerConfFile(Map brokerConf, String brokerConfFile) {
        List<String> commentList = (List<String>) brokerConf.get("commentList");
        Map brokers = (Map) brokerConf.get("brokers");
        StringBuilder sb = new StringBuilder();
        if (commentList != null) {
            for (int i = 0; i < commentList.size(); i++) {
                String comment = (String) commentList.get(i);
                if (comment != null) {
                    sb.append(comment + System.getProperty("line.separator"));
                }
            }
        }
        Iterator<String> iter = brokers.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Map oneBroker = (Map) brokers.get(key);
            sb.append("[%" + key + "%]" + System.getProperty("line.separator"));
            Iterator<String> infoIter = oneBroker.keySet().iterator();
            while (infoIter.hasNext()) {
                String name = (String) infoIter.next();
                String value = (String) oneBroker.get(name);
                sb.append(name + "    " + value + System.getProperty("line.separator"));
            }
            sb.append(System.getProperty("line.separator"));
        }
        LogUtil.log(logId, "[configure Broker]:" + brokerConfFile);
        String readFile = FileUtil.readFile(brokerConfFile);
        FileUtil.writeToFile(brokerConfFile, readFile + System.getProperty("line.separator") + sb.toString());
    }
