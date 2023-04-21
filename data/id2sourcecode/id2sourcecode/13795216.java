    public HashMap<String, LogUnitList> getSortedMap(HashMap<String, LogUnitList> aMap) {
        ArrayList<AbstractLogUnit> arrayList = new ArrayList<AbstractLogUnit>();
        HashMap<String, LogUnitList> sortedMap = new HashMap<String, LogUnitList>();
        AbstractLogUnit item, ev;
        String key = null;
        for (Iterator itSets = aMap.keySet().iterator(); itSets.hasNext(); ) {
            key = (String) itSets.next();
            LogUnitList tempLogUnitList = (LogUnitList) aMap.get(key);
            tempLogUnitList.resetPositionOfItems();
            arrayList.clear();
            for (Iterator itItm = tempLogUnitList.iterator(); itItm.hasNext(); ) {
                item = (AbstractLogUnit) itItm.next();
                if (arrayList.size() != 0) {
                    if (arrayList.get(arrayList.size() - 1).getCurrentTimeStamp().before(item.getCurrentTimeStamp())) {
                        arrayList.add(item);
                        continue;
                    } else if (!arrayList.get(arrayList.size() - 1).getCurrentTimeStamp().after(item.getCurrentTimeStamp())) {
                        arrayList.add(item);
                        continue;
                    }
                    if (arrayList.get(0).getCurrentTimeStamp().after(item.getCurrentTimeStamp())) {
                        arrayList.add(0, item);
                        continue;
                    } else if (!arrayList.get(0).getCurrentTimeStamp().before(item.getCurrentTimeStamp())) {
                        arrayList.add(0, item);
                        continue;
                    }
                    int x_min = 0;
                    int x_max = arrayList.size();
                    int x_mean;
                    while (true) {
                        int x_temp;
                        x_mean = (x_min + x_max) / 2;
                        if (arrayList.get(x_mean).getCurrentTimeStamp().before(item.getCurrentTimeStamp())) {
                            if (x_min == (x_mean + x_max) / 2) {
                                arrayList.add(x_min + 1, item);
                                break;
                            }
                            x_min = x_mean;
                        } else if (arrayList.get(x_mean).getCurrentTimeStamp().after(item.getCurrentTimeStamp())) {
                            if (x_min == (x_min + x_mean) / 2) {
                                arrayList.add(x_min + 1, item);
                                break;
                            }
                            x_max = x_mean;
                        } else {
                            arrayList.add(x_mean + 1, item);
                            break;
                        }
                    }
                } else {
                    arrayList.add(item);
                }
            }
            LogUnitList sortedList = new LogUnitList();
            for (Iterator itr = arrayList.iterator(); itr.hasNext(); ) {
                ev = (AbstractLogUnit) itr.next();
                sortedList.addEvent(ev);
            }
            sortedMap.put(key, sortedList);
        }
        return sortedMap;
    }
