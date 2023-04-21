        public void itemEdited(int index, String text) {
            if (index == 0) {
                try {
                    Date date = dateParse.parse(text);
                    setText(index, dateFormat.format(date));
                } catch (Exception e) {
                    LogFactory.getLog(getClass()).warn(e);
                    setText(index, dateFormat.format(Calendar.getInstance().getTime()));
                }
            } else if (index == 1 || index == 2) {
                try {
                    int value = numberFormatter.parse(text).intValue();
                    setText(index, numberFormatter.format(value));
                } catch (Exception e) {
                    LogFactory.getLog(getClass()).warn(e);
                }
            }
        }
