    private void onTextChange() {
        String text = dateField.getText();
        date = null;
        try {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, getLocale());
            Date parsedDate = dateFormat.parse(text);
            if (text.equals(dateFormat.format(parsedDate))) {
                date = parsedDate;
                currentDateFormat = dateFormat;
                lastDateFormat = dateFormat;
            }
        } catch (Exception e) {
        }
        if (date == null && text.length() > 0) {
            dateField.setBackground(colorBackgroundError);
        } else {
            if (isDateChanged()) {
                dateField.setBackground(colorBackgroundChange);
            } else {
                dateField.setBackground(colorBackground);
            }
        }
        ActionEvent event = new ActionEvent(JDateChooser.this, ActionEvent.ACTION_PERFORMED, "changed", System.currentTimeMillis(), 0);
        fireActionPerformed(event);
    }
