        private String getDateFormat(String format, String target, String rdngDate) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (rdngDate != null && rdngDate.length() > 1) {
                SimpleDateFormat sdftomssql;
                Date rdngdate = null;
                try {
                    sdftomssql = new SimpleDateFormat(target);
                    rdngdate = sdf.parse(rdngDate);
                    cal.setTime(rdngdate);
                    rdngDate = sdftomssql.format(cal.getTime());
                } catch (Exception e) {
                    rdngDate = "";
                }
            } else {
                rdngDate = "";
            }
            return rdngDate;
        }
