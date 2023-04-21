    public void setText(String s) {
        if ((s != null) && (s.length() > 0)) {
            try {
                if (parseFormat != null) startDate = parseFormat.parse(s); else startDate = new Date(s);
                editor.setText(dateFormat.format(startDate));
            } catch (ParseException ex) {
                ex.printStackTrace();
                startDate = new Date(s);
            }
        }
        fillList();
    }
