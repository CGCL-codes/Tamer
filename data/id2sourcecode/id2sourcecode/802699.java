    @Override
    void parseLine(String strLine) {
        String dest = strLine;
        int groupID = _randMemId.nextInt(_totNumOfTeams + 1);
        _missionID++;
        int month = _randDateMonth.nextInt(10);
        int day = _randDateDay.nextInt(26);
        String startDate = gerateDate(month, day);
        String endDate = gerateDate(month + 1, day + 1);
        _cols.clear();
        _vals.clear();
        _cols.add("MissionID");
        _cols.add("GroupID");
        _cols.add("StartDate");
        _cols.add("EndDate");
        _cols.add("Location");
        _cols.add("State");
        _vals.add("'" + _missionID + "'");
        _vals.add("'" + groupID + "'");
        _vals.add("'" + startDate + "'");
        _vals.add("'" + endDate + "'");
        _vals.add("'" + dest + "'");
        _vals.add("'" + "NA" + "'");
        genSQLInsertForTable("missions", _cols, _vals);
    }
