    @Test
    public void i18n_fr() throws Exception {
        final Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/gwt/" + getDirectory() + "/I18N");
        final WebClient client = getWebClient();
        final String url = "http://localhost:" + HttpWebConnectionTest.PORT + "/I18N.html?locale=fr";
        final HtmlPage page = client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(10000);
        i18n(page, "numberFormatOutputText", "31 415 926 535,898");
        String timeZone = new SimpleDateFormat("Z").format(new SimpleDateFormat("d MMMMMMMM yyyy").parse("13 September 1999"));
        timeZone = timeZone.substring(0, 3) + ':' + timeZone.substring(3);
        i18n(page, "dateTimeFormatOutputText", "lundi 13 septembre 1999 00:00:00 GMT" + timeZone);
        i18n(page, "messagesFormattedOutputText", "L'utilisateur 'amelie' a un niveau de securité 'guest', " + "et ne peut accéder à '/secure/blueprints.xml'");
        i18n(page, "constantsFirstNameText", "Amelie");
        i18n(page, "constantsLastNameText", "Crutcher");
        i18n(page, "constantsFavoriteColorList", new String[] { "Rouge", "Blanc", "Jaune", "Noir", "Bleu", "Vert", "Gris", "Gris clair" });
        i18n(page, "constantsWithLookupResultsText", "Rouge");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("name", "Amelie Crutcher");
        map.put("timeZone", "EST");
        map.put("userID", "123");
        map.put("lastLogOn", "2/2/2006");
        i18nDictionary(page, map);
        Locale.setDefault(locale);
    }
