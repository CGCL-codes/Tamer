    public void loadCurrencyRessources() {
        String resFile, coinsOrBills, str;
        ImageIcon img = new ImageIcon();
        String[] workArr;
        URL url;
        int i;
        moneyResFile = "default.jar";
        resFile = System.getProperty("user.dir") + "/conf/currencies/" + moneyResFile;
        moneyResImg.clear();
        availableBillImg = null;
        availableCoinImg = null;
        coinsOrBills = "";
        try {
            Properties props = new Properties();
            url = new URL("jar:file:" + resFile + "!/description");
            if (url != null) {
                try {
                    props.load(url.openStream());
                    if (props.getProperty("COINS") != null) coinsOrBills = (props.getProperty("COINS"));
                    workArr = coinsOrBills.split(",");
                    availableCoinImg = new Integer[workArr.length];
                    for (i = 0; i < workArr.length; i++) {
                        str = workArr[i].trim();
                        availableCoinImg[i] = Integer.parseInt(str);
                        img = getRessourceImg(resFile, "/ressources/icons/coin." + str + ".png");
                        moneyResImg.put("iconCoin" + str, img);
                        if (logger.isDebugEnabled()) {
                            if (img != null) CoreConfig.logger.debug("iconCoin" + str + " OK!"); else CoreConfig.logger.debug("iconCoin" + str + "  NULL!");
                        }
                    }
                    if (props.getProperty("BILLS") != null) coinsOrBills = (props.getProperty("BILLS"));
                    workArr = coinsOrBills.split(",");
                    availableBillImg = new Integer[workArr.length];
                    for (i = 0; i < workArr.length; i++) {
                        str = workArr[i].trim();
                        availableBillImg[i] = Integer.parseInt(str);
                        img = getRessourceImg(resFile, "/ressources/icons/bill." + str + ".png");
                        moneyResImg.put("iconBill" + str, img);
                        if (logger.isDebugEnabled()) {
                            if (img != null) CoreConfig.logger.debug("iconBill" + str + " OK!"); else CoreConfig.logger.debug("iconBill" + str + "  NULL!");
                        }
                    }
                } catch (IOException ex) {
                    CoreConfig.logger.error("error whille loading icons for currency!\n" + ex);
                }
            }
        } catch (MalformedURLException ex) {
            CoreConfig.logger.error("error whille loading icons for currency!\n" + ex);
        }
    }
