    public ArrayList orderPIEndTimestampList(LogUnitList tempDrawList) {
        ArrayList orderedList = new ArrayList();
        AbstractLogUnit event = null;
        Date endPIDate = null;
        if (tempDrawList != null && tempDrawList.size() > 0) {
            for (Iterator iter = tempDrawList.iterator(); iter.hasNext(); ) {
                event = (AbstractLogUnit) iter.next();
                if (timeOption.equals(TIME_RELATIVE_TIME)) {
                    endPIDate = new Date(csModel.getEndDateMap(ST_INST).get(event.getProcessInstance().getName()).getTime() - csModel.getStartDateMap(ST_INST).get(event.getProcessInstance().getName()).getTime());
                } else if (timeOption.equals(TIME_RELATIVE_RATIO)) {
                    endPIDate = new Date(10000);
                } else endPIDate = csModel.getEndDateMap(ST_INST).get(event.getProcessInstance().getName());
                if (orderedList.size() != 0) {
                    if (((Date) orderedList.get(orderedList.size() - 1)).before(endPIDate)) {
                        orderedList.add(endPIDate);
                        continue;
                    } else if (!((Date) orderedList.get(orderedList.size() - 1)).after(endPIDate)) {
                        orderedList.add(endPIDate);
                        continue;
                    }
                    if (((Date) orderedList.get(0)).after(endPIDate)) {
                        orderedList.add(0, endPIDate);
                        continue;
                    } else if (!((Date) orderedList.get(0)).before(endPIDate)) {
                        orderedList.add(0, endPIDate);
                        continue;
                    }
                    int x_min = 0;
                    int x_max = orderedList.size();
                    int x_mean;
                    while (true) {
                        int x_temp;
                        x_mean = (x_min + x_max) / 2;
                        if (((Date) orderedList.get(x_mean)).before(endPIDate)) {
                            if (x_min == (x_mean + x_max) / 2) {
                                orderedList.add(x_min + 1, endPIDate);
                                break;
                            }
                            x_min = x_mean;
                        } else if (((Date) orderedList.get(x_mean)).after(endPIDate)) {
                            if (x_min == (x_min + x_mean) / 2) {
                                orderedList.add(x_min + 1, endPIDate);
                                break;
                            }
                            x_max = x_mean;
                        } else {
                            orderedList.add(x_mean + 1, endPIDate);
                            break;
                        }
                    }
                } else {
                    orderedList.add(endPIDate);
                }
            }
            return orderedList;
        } else return null;
    }
