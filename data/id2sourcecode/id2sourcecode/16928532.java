    private ExcelTemplateBuilder(Component component, String fileName) {
        try {
            File f = new File(fileName);
            if (f.length() != 0) {
                int returnVal = JOptionPane.showConfirmDialog(component, "A file named \"NewQuperTemplate.xls\" already exists, " + "overwrite?", "Overwrite?", JOptionPane.OK_CANCEL_OPTION);
                if (returnVal != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            workbook = Workbook.createWorkbook(f);
            addFeature();
            addQuality();
            workbook.write();
            workbook.close();
        } catch (Exception ex) {
            ExceptionHandler.error(ex);
        }
    }
