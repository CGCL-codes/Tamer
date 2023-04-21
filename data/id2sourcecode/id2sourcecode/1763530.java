    private List getValidCloseOffDays() {
        List closeOffDays = new ArrayList();
        try {
            String hql = "select min(registerTime),max(registerTime) from " + "com.widen.prima.finance.entites.SubjectBalanceBo where boaType=" + BoaType.CLOSEOFF.getValue();
            Collection result = Util.financeMgrService.findByHql(hql);
            Object[] dates = (Object[]) result.iterator().next();
            String minTimeString = (String) dates[0];
            String maxTimeString = (String) dates[1];
            if (minTimeString != null && maxTimeString != null) {
                Date minTime = Util.dateFormatter_code.parse(minTimeString);
                Date maxTime = Util.dateFormatter_code.parse(maxTimeString);
                int minYear = DateUtil.getYear(minTime);
                int minMonth = DateUtil.getMonth(minTime);
                int maxYear = DateUtil.getYear(maxTime);
                int maxMonth = DateUtil.getMonth(maxTime);
                for (int year = minYear; year <= maxYear; year++) {
                    int beginMonth = 0;
                    int endMonth = 0;
                    if (year == minYear && year < maxYear) {
                        beginMonth = minMonth;
                        endMonth = 12;
                    } else if (year == maxYear && year > minYear) {
                        beginMonth = 1;
                        endMonth = maxMonth;
                    } else if (minYear == year && year == maxYear) {
                        beginMonth = minMonth;
                        endMonth = maxMonth;
                    } else {
                        beginMonth = 1;
                        endMonth = 12;
                    }
                    for (int month = beginMonth; month <= endMonth; month++) {
                        Date closeOffDay = DateUtil.getEndTime(year, month);
                        String closeOfDayString = Util.dateFormatter_code.format(closeOffDay);
                        closeOffDays.add(closeOfDayString);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return closeOffDays;
    }
