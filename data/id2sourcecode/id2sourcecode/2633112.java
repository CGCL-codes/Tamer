    private synchronized void zeit() {
        try {
            Date today = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            Date d = sdf.parse(start);
            long sekunden = Math.round((today.getTime() - d.getTime()) / (1000));
            int proz = 100;
            for (int i = 0; i < prozent.length; ++i) {
                if (prozent[i] > 0) {
                    if (prozent[i] < proz) {
                        proz = prozent[i];
                    }
                }
            }
            if (proz == 0 || proz == 100) {
                jTextFieldEnde.setText("");
            } else {
                sekunden = 100 * sekunden / proz;
                Date ziel = new Date(today.getTime() + sekunden * 1000);
                String output = sdf.format(ziel);
                jTextFieldEnde.setText(output);
            }
        } catch (ParseException ex) {
        }
    }
