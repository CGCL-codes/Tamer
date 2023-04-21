    @Test
    public void i18n() throws Exception {
        final Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        final HtmlPage page = loadGWTPage("I18N", null);
        i18n(page, "numberFormatOutputText", "31,415,926,535.898");
        String timeZone = new SimpleDateFormat("Z").format(new SimpleDateFormat("d MMMMMMMM yyyy").parse("13 September 1999"));
        timeZone = timeZone.substring(0, 3) + ':' + timeZone.substring(3);
        i18n(page, "dateTimeFormatOutputText", "Monday, September 13, 1999 12:00:00 AM GMT" + timeZone);
        i18n(page, "messagesFormattedOutputText", "User 'amelie' has security clearance 'guest' and cannot access '/secure/blueprints.xml'");
        i18n(page, "constantsFirstNameText", "Amelie");
        i18n(page, "constantsLastNameText", "Crutcher");
        i18n(page, "constantsFavoriteColorList", new String[] { "Red", "White", "Yellow", "Black", "Blue", "Green", "Grey", "Light Grey" });
        i18n(page, "constantsWithLookupResultsText", "Red");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("name", "Amelie Crutcher");
        map.put("timeZone", "EST");
        map.put("userID", "123");
        map.put("lastLogOn", "2/2/2006");
        i18nDictionary(page, map);
        Locale.setDefault(locale);
    }
