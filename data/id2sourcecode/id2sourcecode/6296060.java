    private JasperReport standardListReport(SearchResults results, String title) {
        try {
            JasperDesign design = (JasperDesign) JRXmlLoader.load(urlFor("StandardList").openStream());
            JRDesignBand headerBand = (JRDesignBand) design.getTitle();
            JRDesignBand detailBand = (JRDesignBand) design.getDetail();
            JRDesignElement templateHeader = (JRDesignElement) headerBand.getElementByKey("staticText-1");
            JRDesignElement templateField = (JRDesignElement) detailBand.getElementByKey("textField-1");
            JRDesignStaticText pageTitle = (JRDesignStaticText) headerBand.getElementByKey("staticText-2");
            pageTitle.setText(title);
            for (int i = 0; i < results.getColumnCount(); i++) {
                int cloneWidth = templateHeader.getWidth() / results.getColumnCount();
                int cloneX = i * cloneWidth;
                String columnName = results.getColumnName(i);
                String columnDescription = results.getColumnDescription(i);
                Class<?> columnClass = results.getColumnClass(i);
                design.addField(columnField(columnName));
                headerBand.addElement(columnHeaderElement(templateHeader, cloneWidth, i, cloneX, columnDescription));
                detailBand.addElement(columnValueElement(templateField, cloneWidth, i, cloneX, columnName, columnClass));
            }
            headerBand.removeElement(templateHeader);
            detailBand.removeElement(templateField);
            return JasperCompileManager.compileReport(design);
        } catch (Exception e) {
            throw new ReportPrintException(e);
        }
    }
