    private int write(OpenOfficeCalc calc, XSpreadsheet sheet, Map<ProjectTaskModel, ProjectTaskTimeWeek> map) throws IndexOutOfBoundsException {
        int y = 1;
        for (ProjectTaskTimeWeek w : map.values()) {
            myLog.debug(w.toString());
            sheet.getCellByPosition(0, y).setFormula(w.getProjectTask().getProject().getName() + " - " + w.getProjectTask().getName());
            for (int d = 2; d < 8; d++) {
                if (w.getMinutes(d) == 0) {
                    sheet.getCellByPosition(d, y).setFormula("");
                } else {
                    XCell cell = sheet.getCellByPosition(d, y);
                    cell.setValue(w.getMinutes(d) / 60.0);
                    try {
                        calc.setCellFormat(cell, "0.0");
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
                }
            }
            if (w.getMinutes(1) == 0) {
                sheet.getCellByPosition(8, y).setFormula("");
            } else {
                XCell cell = sheet.getCellByPosition(8, y);
                cell.setValue(w.getMinutes(1) / 60.0);
                try {
                    calc.setCellFormat(cell, "0.0");
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
            }
            y++;
        }
        int lastRow = y - 1;
        for (; y < 100; y++) {
            for (int x = 0; x < 9; x++) {
                sheet.getCellByPosition(x, y).setFormula("");
            }
        }
        return lastRow;
    }
