    public void showTrialExpired_Debug() {
        String trialExpireDate = getExpireDate();
        DateFormat df = getDateFormat();
        Date expiryDate = null;
        StringBuffer msg = new StringBuffer();
        msg.append("Host=").append(getHost());
        msg.append("\n");
        msg.append("Version=").append(getVersion());
        msg.append("\n");
        msg.append("Trial Expire Date=").append(trialExpireDate);
        msg.append("\n");
        try {
            expiryDate = df.parse(trialExpireDate);
            msg.append("Parsing Expire Date=OK");
            msg.append("\n");
        } catch (java.text.ParseException exc) {
            msg.append("Parsing Expire Date Exc:");
            msg.append("\n");
            msg.append("\t").append(exc.getMessage());
            msg.append("\n");
        }
        Date now = new Date();
        msg.append("Today's Date=");
        msg.append(df.format(now));
        msg.append("\n");
        int daysLeft = trialDaysLeft();
        msg.append("#Days left=");
        msg.append(daysLeft);
        msg.append("\n");
        boolean isExpired = isExpired();
        msg.append("isExpired=");
        msg.append(isExpired);
        msg.append("\n");
        msg.append("\n");
        Locale defaultLocal = Locale.getDefault();
        msg.append("Default Locale=");
        msg.append(defaultLocal.getDisplayLanguage());
        msg.append(", ");
        msg.append(defaultLocal.getISO3Country());
        msg.append("\n");
        msg.append("Available Locales:");
        msg.append("\n");
        Locale[] locales = DateFormat.getAvailableLocales();
        if (locales != null) {
            int wrap = 10;
            int len = locales.length;
            for (int i = 0; i < len; i++) {
                Locale locale = locales[i];
                String displayL = locale.getDisplayLanguage();
                String iso3 = locale.getISO3Country();
                msg.append(displayL);
                if (iso3.length() > 0) {
                    msg.append("(");
                    msg.append(iso3);
                    msg.append(")");
                }
                wrap--;
                if (wrap > 0) {
                    msg.append(", ");
                } else {
                    msg.append("\n\t");
                    wrap = 10;
                }
            }
        } else {
            msg.append("\tnull");
        }
        msg.append("\n");
        JOptionPane.showMessageDialog(null, msg.toString(), "Mexuar Communications - Corraleta SDK", JOptionPane.ERROR_MESSAGE);
    }
