    public String generate(SouscriptionDTO pSouscription, Locale pCurrentLocale) throws Exception {
        SimpleDateFormat vFormat = new SimpleDateFormat("yyyyMMdd");
        String vDirectory = new StringBuilder(FactoryBundle.getInstance().getProperty("root.directory")).append("resources/invoices/").append(pSouscription.getClient().getIdclient()).append("/").toString();
        String vFile = new StringBuilder(vDirectory).append(pSouscription.getFormule().getNom()).append("_").append(vFormat.format(pSouscription.getDateSouscription())).append("-").append(vFormat.format(pSouscription.getDateResiliation())).append(".pdf").toString();
        if (!new File(vDirectory).exists()) {
            new File(vDirectory).mkdirs();
        }
        FileOutputStream buffer = new FileOutputStream(vFile);
        startDocument(buffer, PageSize.A4);
        createAndAddHeader(new StringBuilder(pSouscription.getClient().getPrenom()).append(" ").append(pSouscription.getClient().getNom()).append("\r\n").append(pSouscription.getClient().getEmail()).append("\r\n").append(pSouscription.getClient().getAdresse()).toString(), document);
        if (pCurrentLocale.equals(Locale.ENGLISH)) {
            createAndAddFooter(new StringBuilder(FactoryBundle.getInstance().getProperty("corp.name")).append(" - ").append(getStrDate(new Date())).toString(), document);
            document.open();
            publish(pSouscription);
        } else if (pCurrentLocale.equals(Locale.FRANCE)) {
            createAndAddFooter(new StringBuilder(FactoryBundle.getInstance().getProperty("corp.name")).append(" - Le ").append(getStrDateFr(new Date())).toString(), document);
            document.open();
            publishFr(pSouscription);
        }
        endDocument(document);
        buffer.close();
        return vFile;
    }
