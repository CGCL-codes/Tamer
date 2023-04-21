    private int write(OpenOfficeCalc calc, OpenOfficeFunction func, XSpreadsheet sheet, int row, TimeRecordModel timeRecord) throws IndexOutOfBoundsException {
        int y = Math.max(row + 5, 25);
        for (TimeRecordProjectTaskModel tpt : timeRecord.getProjectTasks()) {
            if (y == row + 5) {
                try {
                    calc.setCell(func, sheet.getCellByPosition(0, y), tpt.getTimeRecord().getStart(), true, false, "DD/MM/YY");
                    calc.setCell(func, sheet.getCellByPosition(1, y), tpt.getTimeRecord().getStart(), false, true, "HH:MM:SS");
                    calc.setCell(func, sheet.getCellByPosition(2, y), tpt.getTimeRecord().getStop(), false, true, "HH:MM:SS");
                } catch (Exception exc) {
                    myLog.error("write", exc);
                }
            }
            sheet.getCellByPosition(3, y).setValue(tpt.getMinutes());
            sheet.getCellByPosition(4, y).setFormula(tpt.getProjectTask().getProject().getName());
            sheet.getCellByPosition(5, y).setValue(tpt.getProjectTask().getKey());
            sheet.getCellByPosition(6, y).setFormula("Admin");
            try {
                calc.setCellFormat(sheet.getCellByPosition(3, y), null);
                calc.setCellFormat(sheet.getCellByPosition(5, y), null);
            } catch (MalformedNumberFormatException exc) {
                myLog.error("write", exc);
            } catch (UnknownPropertyException exc) {
                myLog.error("write", exc);
            } catch (PropertyVetoException exc) {
                myLog.error("write", exc);
            } catch (IllegalArgumentException exc) {
                myLog.error("write", exc);
            } catch (WrappedTargetException exc) {
                myLog.error("write", exc);
            }
            y++;
        }
        return y - 1;
    }
