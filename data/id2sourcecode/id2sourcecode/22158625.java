    private void doLogin() {
        if ((!midlet.getSettings().loginNow) && (midlet.getSettings().autoLogin)) {
            midlet.displayAttributesForm();
            return;
        }
        midlet.setGaugeForm("Logging in...");
        connector.connect(midlet.params);
        Hashtable response;
        try {
            response = connector.login();
        } catch (IOException e) {
            midlet.displayError("Logging error", "Error during sending data via GPRS");
            return;
        }
        if (response.get("success").equals("OK")) {
            if (response.get("access_count") != null) {
                int journalsCount = Integer.parseInt((String) response.get("access_count")) + 1;
                midlet.journals = new String[journalsCount];
                midlet.journals[0] = UTFDecoder.decode((String) response.get("name"));
                for (int i = 1; i < journalsCount; i++) {
                    midlet.journals[i] = (String) response.get("access_" + String.valueOf(i));
                }
            } else {
                midlet.journals = new String[1];
                midlet.journals[0] = UTFDecoder.decode((String) response.get("name"));
            }
            if (response.get("pickw_count") != null) {
                int picsCount = Integer.parseInt((String) response.get("pickw_count"));
                if (picsCount > 0) {
                    midlet.userpics = new String[picsCount];
                    for (int i = 0; i < picsCount; i++) {
                        midlet.userpics[i] = UTFDecoder.decode((String) response.get("pickw_" + String.valueOf(i + 1)));
                    }
                }
            } else {
                midlet.userpics = null;
            }
        } else {
            midlet.displayError("Logging error", (String) response.get("errmsg"));
            return;
        }
        midlet.displayAttributesForm();
    }
