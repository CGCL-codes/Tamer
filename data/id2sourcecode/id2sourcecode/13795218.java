    public void calculateCurrentTimeLogical() {
        ArrayList<AbstractLogUnit> arrayList = new ArrayList<AbstractLogUnit>();
        AbstractLogUnit item;
        String key = null;
        for (Iterator itSets = getItemMap().keySet().iterator(); itSets.hasNext(); ) {
            key = (String) itSets.next();
            if (typeHashMap.equals(ST_INST) && !instanceIDs.contains(key)) continue;
            LogUnitList tempLogUnitList = (LogUnitList) getItemMap().get(key);
            tempLogUnitList.resetPositionOfItems();
            for (Iterator itItm = tempLogUnitList.iterator(); itItm.hasNext(); ) {
                item = (AbstractLogUnit) itItm.next();
                if (eventTypeToKeep != null && (!instanceIDs.contains(item.getProcessInstance().getName()))) continue;
                if (arrayList.size() != 0) {
                    if (arrayList.get(arrayList.size() - 1).getActualTimeStamp().before(item.getActualTimeStamp())) {
                        arrayList.add(item);
                        continue;
                    } else if (!arrayList.get(arrayList.size() - 1).getActualTimeStamp().after(item.getActualTimeStamp())) {
                        arrayList.add(item);
                        continue;
                    }
                    if (arrayList.get(0).getActualTimeStamp().after(item.getActualTimeStamp())) {
                        arrayList.add(0, item);
                        continue;
                    } else if (!arrayList.get(0).getActualTimeStamp().before(item.getActualTimeStamp())) {
                        arrayList.add(0, item);
                        continue;
                    }
                    int x_min = 0;
                    int x_max = arrayList.size();
                    int x_mean;
                    while (true) {
                        int x_temp;
                        x_mean = (x_min + x_max) / 2;
                        if (arrayList.get(x_mean).getActualTimeStamp().before(item.getActualTimeStamp())) {
                            if (x_min == (x_mean + x_max) / 2) {
                                arrayList.add(x_min + 1, item);
                                break;
                            }
                            x_min = x_mean;
                        } else if (arrayList.get(x_mean).getActualTimeStamp().after(item.getActualTimeStamp())) {
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
        }
        if (arrayList.size() > 0) {
            arrayList.get(0).setPosition(0);
            arrayList.get(0).setCurrentTimeStampLogical();
            for (int i = 1; i < arrayList.size(); i++) {
                AbstractLogUnit abs = (AbstractLogUnit) arrayList.get(i);
                AbstractLogUnit abs0 = (AbstractLogUnit) arrayList.get(i - 1);
                if (!abs0.getActualTimeStamp().before(abs.getActualTimeStamp())) {
                    abs.setPosition(abs0.getPosition());
                } else abs.setPosition(i);
                abs.setCurrentTimeStampLogical();
            }
        }
    }
