    public int addBracketLabelsAndScoreGenFormElements(final Connection pConnection, final int tournament, final String division) throws SQLException {
        final List<String[]> tournamentTables = Queries.getTournamentTables(pConnection);
        final List<String> tables = new LinkedList<String>();
        final Iterator<String[]> ttIt = tournamentTables.iterator();
        while (ttIt.hasNext()) {
            final String[] tt = ttIt.next();
            tables.add(tt[0]);
            tables.add(tt[1]);
        }
        if (tables.isEmpty()) {
            tables.add("Table 1");
            tables.add("Table 2");
        }
        Iterator<String> tAssignIt = tables.iterator();
        int assignCount = Queries.getTableAssignmentCount(pConnection, tournament, division) % tables.size();
        while (assignCount-- > 0) {
            tAssignIt.next();
        }
        int matchNum = 1;
        for (int i = _firstRound; i <= _lastRound && i <= _finalsRound; i++) {
            final SortedMap<Integer, BracketDataType> roundData = _bracketData.get(i);
            if (roundData != null) {
                final List<Integer[]> rows = new LinkedList<Integer[]>();
                final Iterator<Integer> it = roundData.keySet().iterator();
                while (it.hasNext()) {
                    final Integer firstTeamRow = it.next();
                    if (!it.hasNext()) {
                        throw new RuntimeException("Mismatched team in playoff brackets. Check database for corruption.");
                    }
                    final Integer secondTeamRow = it.next();
                    rows.add(new Integer[] { firstTeamRow, secondTeamRow });
                }
                int bracketNumber = 1;
                final Iterator<Integer[]> rit = rows.iterator();
                Integer[] curArray;
                String bracketLabel;
                while (rit.hasNext()) {
                    if (i == _finalsRound && bracketNumber == 1) {
                        bracketLabel = "1st/2nd Place";
                    } else if (i == _finalsRound && bracketNumber == 2) {
                        bracketLabel = "3rd/4th Place";
                    } else {
                        bracketLabel = "Bracket " + bracketNumber;
                    }
                    curArray = rit.next();
                    if (((TeamBracketCell) roundData.get(curArray[0])).getTeam().getTeamNumber() > 0 && ((TeamBracketCell) roundData.get(curArray[1])).getTeam().getTeamNumber() > 0) {
                        String tableA = ((TeamBracketCell) roundData.get(curArray[0])).getTable();
                        if (null == tableA || tableA.length() == 0) {
                            if (!tAssignIt.hasNext()) {
                                tAssignIt = tables.iterator();
                            }
                            tableA = tAssignIt.next();
                        }
                        String tableB = ((TeamBracketCell) roundData.get(curArray[1])).getTable();
                        if (null == tableB || tableB.length() == 0) {
                            if (!tAssignIt.hasNext()) {
                                tAssignIt = tables.iterator();
                            }
                            tableB = tAssignIt.next();
                        }
                        final TeamBracketCell topCell = (TeamBracketCell) roundData.get(curArray[0]);
                        final TeamBracketCell bottomCell = (TeamBracketCell) roundData.get(curArray[1]);
                        roundData.put(curArray[0] + 1, new ScoreSheetFormBracketCell(tables, bracketLabel, matchNum++, topCell.getPrinted() && bottomCell.getPrinted(), tableA, tableB, topCell.getTeam(), bottomCell.getTeam(), curArray[1].intValue() - curArray[0].intValue() - 1));
                        for (int j = curArray[0].intValue() + 2; j < curArray[1].intValue(); j++) {
                            roundData.put(j, new SpannedOverBracketCell("spanned row" + j));
                        }
                    } else {
                        final int firstRow = curArray[0].intValue();
                        final int lastRow = curArray[1].intValue();
                        final int line = firstRow + (lastRow - firstRow) / 2;
                        roundData.put(line, new BracketLabelCell(bracketLabel));
                    }
                    bracketNumber++;
                }
            }
        }
        return matchNum - 1;
    }
