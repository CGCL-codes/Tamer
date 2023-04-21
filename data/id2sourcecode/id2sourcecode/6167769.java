    private int writeMonthSummary(OpenOfficeCalc calc, OpenOfficeFunction func, XSpreadsheet sheet, Date date, Map<ProjectTaskModel, ProjectTaskTime> map) throws IndexOutOfBoundsException {
        myLog.debug("write - {}", date);
        try {
            calc.setCell(func, sheet.getCellByPosition(1, 0), date, true, false, "MMMM YYYY");
        } catch (Exception exc) {
            myLog.error("write", exc);
        }
        int y = 1;
        int yStartParty = 1;
        PartyModel party = null;
        for (ProjectTaskTime t : map.values()) {
            myLog.debug("write - y = {}, t = {}", y, t.toString());
            if (party == null) {
                party = t.getProjectTask().getProject().getParty();
                sheet.getCellByPosition(0, y).setFormula(party.getNameAsString());
            } else if (party != t.getProjectTask().getProject().getParty()) {
                sheet.getCellByPosition(3, y - 1).setFormula("=sum(C" + (yStartParty + 1) + ":C" + y);
                party = t.getProjectTask().getProject().getParty();
                for (int x = 0; x < 9; x++) {
                    sheet.getCellByPosition(x, y).setFormula("");
                }
                y++;
                sheet.getCellByPosition(0, y).setFormula(party.getNameAsString());
                yStartParty = y;
            } else {
                sheet.getCellByPosition(0, y).setFormula("");
            }
            sheet.getCellByPosition(1, y).setFormula(t.getProjectTask().getProject().getName() + " - " + t.getProjectTask().getName());
            sheet.getCellByPosition(2, y).setValue(t.getMinutes() / 60.0);
            sheet.getCellByPosition(3, y).setFormula("");
            sheet.getCellByPosition(4, y).setFormula("");
            y++;
        }
        sheet.getCellByPosition(3, y - 1).setFormula("=sum(C" + (yStartParty + 1) + ":C" + y);
        for (int x = 0; x < 9; x++) {
            sheet.getCellByPosition(x, y).setFormula("");
        }
        y++;
        sheet.getCellByPosition(1, y).setFormula("Total for Month");
        sheet.getCellByPosition(2, y).setFormula("=sum(C2:C" + y);
        sheet.getCellByPosition(3, y).setFormula("=sum(C2:C" + y);
        myLog.debug("write - y = {}", y);
        int lastRow = y++;
        for (; y < 100; y++) {
            for (int x = 0; x < 9; x++) {
                sheet.getCellByPosition(x, y).setFormula("");
            }
        }
        return lastRow;
    }
