    public void render() {
        String[][] data = srTable.getAsMatrix();
        FastReportBuilder rb = new FastReportBuilder();
        ArrayList<DJReportParameters> DJReportParameters = new ArrayList<DJReportParameters>();
        for (int j = 0; j < data[0].length; j++) {
            try {
                PropertyColumn column = new SimpleColumn();
                column.setTitle(srTable.isHeaderFirstRow() ? data[0][j] : "");
                column.setName(String.class.getName());
                column.setColumnProperty(new ColumnProperty("param[" + j + "]", String.class.getName()));
                column.setWidth(100);
                Style style = new Style();
                style.setBorderTop(border);
                style.setBorderColor(borderColor);
                Style styleHeader = new Style();
                styleHeader.setBackgroundColor(headerBackgroundColor);
                column.setStyle(style);
                styleHeader.setBorder(headerBorder);
                Color colorHeader = new Color(255, 251, 174);
                rb.addColumn(column);
                if (data.length > 3 && colorBackgroundOddRows == true) rb.setPrintBackgroundOnOddRows(true);
                Style style2 = new Style();
                style2.setBackgroundColor(backgroundColor);
                rb.setOddRowBackgroundStyle(style2);
            } catch (Exception e) {
            }
        }
        for (int i = 0; i < data.length; i++) {
            String ff[] = null;
            boolean header = (i == 0 && srTable.isHeaderFirstRow());
            ff = new String[maxTableSize];
            for (int j = 0; j < data[i].length; j++) {
                if (srTable.isHeaderFirstRow() == true) {
                    if (i < data.length - 1) ff[j] = data[i + 1][j];
                } else ff[j] = data[i][j];
                if (ff[j] != null && ff[j].contains(".") == true) {
                    try {
                        Double number = Double.parseDouble(ff[j]);
                        {
                            number = Math.round(number * rounding) / rounding;
                            ff[j] = number.toString();
                        }
                    } catch (Exception e) {
                    }
                }
            }
            DJReportParameters.add(new DJReportParameters(ff));
        }
        rb.setMargins(5, 5, 20, 20);
        rb.setUseFullPageWidth(true);
        rb.setTitle(srTable.getCaption());
        if (srTable.isHeaderFirstRow() == false) rb.setPrintColumnNames(false);
        DynamicReport dr = rb.build();
        DJReportRenderer.addParams("" + DJReportRenderer.getSubreportNumber(), DJReportParameters);
        DJReportRenderer.addSubList(dr);
    }
