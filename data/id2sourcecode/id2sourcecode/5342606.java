    public void init() {
        Vector calendars = new Vector();
        try {
            for (int i = 1; true; i++) {
                String param = "file" + i;
                String urlStr = getParameter(param);
                if (urlStr != null) {
                    CalendarParser parser = null;
                    URL url = new URL(urlStr);
                    URLConnection conn = url.openConnection();
                    InputStream inStream = conn.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
                    if (urlStr.toLowerCase().endsWith(".csv")) {
                        System.out.println("Parsing CVS calendar: " + url);
                        parser = new CSVParser(CSVParser.PARSE_STRICT);
                    } else {
                        System.out.println("Parsing ICS calendar: " + url);
                        parser = new ICalendarParser(ICalendarParser.PARSE_LOOSE);
                    }
                    parser.parse(in);
                    System.out.println("  found " + parser.getDataStoreAt(0).getAllEvents().size() + " events");
                    inStream.close();
                    param = "color" + i;
                    Color color = defaultColors[(i - 1) % defaultColors.length];
                    if (getParameter(param) != null) {
                        color = parseColor(getParameter(param));
                    }
                    RemoteCalendar rc = new RemoteCalendar(parser, color);
                    calendars.addElement(rc);
                } else {
                    break;
                }
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.data = new AppletDataRepository(calendars, false);
        buildUI();
    }
