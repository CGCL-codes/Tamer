    private String checkFields() {
        String err = "";
        err += checkEmpty(paymentsfield, "Payments file");
        err += checkEmpty(dateformatfield, "Date format");
        err += checkEmpty(timeformatfield, "Time format");
        err += checkEmpty(fieldseparatorfield, "Field separator");
        err += checkEmpty(decimalseparatorfield, "Decimal separator");
        err += checkEmpty(openingtimefield, "Opening time");
        err += checkEmpty(closingtimefield, "Closing time");
        if (doqoptcb.isSelected()) {
            err += checkInt(qoptdelayfield, "Queue optimization delay");
        }
        if (splitthresholdcb.isSelected()) {
            err += checkInt(thresholdfield, "Splitting threshold");
        }
        if (splitliquiditycb.isSelected()) {
            err += checkInt(minliquidityfield, "Minimum liquidity for splitting");
        }
        String d = dateformatfield.getText();
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(d);
            sdf.format(date);
        } catch (Exception e) {
            err += "Syntax error in date format\n";
        }
        if (d.contains("m")) JOptionPane.showMessageDialog(null, "Warning: m in date format means minutes,\n" + "use M for months");
        if (d.contains("D")) JOptionPane.showMessageDialog(null, "Warning: D in date format means day in year,\n" + "use d for day in month");
        d = timeformatfield.getText();
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(d);
            sdf.format(date);
        } catch (Exception e) {
            err += "Syntax error in time format\n";
        }
        if (d.contains("h")) JOptionPane.showMessageDialog(null, "Warning: h in time format means hours in am/pm,\n" + "use H for hours in 0-23 range");
        try {
            String time = openingtimefield.getText();
            SimpleDateFormat sdf = new SimpleDateFormat(timeformatfield.getText());
            sdf.parse(time);
        } catch (Exception e) {
            err += "Parsing error in opening time\n";
        }
        try {
            String time = closingtimefield.getText();
            SimpleDateFormat sdf = new SimpleDateFormat(timeformatfield.getText());
            sdf.parse(time);
        } catch (Exception e) {
            err += "Parsing error in closing time\n";
        }
        return err;
    }
