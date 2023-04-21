    private void readFile() throws Exception {
        if (null != htChanTimes) return;
        htChanTimes = new Hashtable();
        Hashtable ht = ConfigInfo.getInstance().getChannelSchedule();
        String[] aTimes, aTime;
        String chanPath, times;
        ArrayList al;
        int i;
        if (null != ht && !ht.isEmpty()) {
            Iterator iter = ht.keySet().iterator();
            if (null != iter) {
                while (iter.hasNext()) {
                    chanPath = (String) iter.next();
                    times = (String) ht.get(chanPath);
                    al = new ArrayList();
                    aTimes = Function.stringToArray(times, ";");
                    if (null != aTimes && aTimes.length > 0) {
                        for (i = 0; i < aTimes.length; i++) {
                            if (aTimes[i].length() > 1) {
                                aTime = Function.stringToArray(aTimes[i], "-");
                                if (null != aTime && aTime.length == 2) {
                                    al.add(aTime);
                                }
                            }
                        }
                    }
                    if (al.size() > 0) {
                        htChanTimes.put(chanPath, (String[][]) al.toArray(new String[0][0]));
                    }
                }
            }
        }
    }
