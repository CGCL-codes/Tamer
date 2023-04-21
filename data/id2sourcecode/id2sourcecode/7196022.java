    public List<EventData> getEvents() throws IOException {
        final long methodStartTime = System.currentTimeMillis();
        final List<EventData> l = new ArrayList<EventData>();
        URL url;
        url = new URL("http://www.nycgovparks.org/xml/events_300_rss.xml");
        final long gotISTime;
        final long parsedTime;
        try {
            InputStream is = url.openConnection().getInputStream();
            gotISTime = System.currentTimeMillis();
            RootElement root = new RootElement("rss");
            Element channel = root.getChild("channel");
            Element item = channel.getChild("item");
            Element title = item.getChild("title");
            Element description = item.getChild("description");
            Element location = item.getChild("http://www.nycgovparks.org/eventsrss_ns/", "location");
            Element parknames = item.getChild("parknames");
            Element link = item.getChild("link");
            Element contactPhone = item.getChild("http://www.nycgovparks.org/eventsrss_ns/", "contact_phone");
            Element startDate = item.getChild("http://www.nycgovparks.org/eventsrss_ns/", "startdate");
            Element startTime = item.getChild("http://www.nycgovparks.org/eventsrss_ns/", "starttime");
            Element endTime = item.getChild("http://www.nycgovparks.org/eventsrss_ns/", "endtime");
            item.setElementListener(new ElementListener() {

                @Override
                public void start(Attributes arg0) {
                    currentEvent = new EventData();
                    currentEvent.setCategory(Constants.PARKS);
                }

                @Override
                public void end() {
                    currentEvent.setStartTime(parseTime(currentStartDate, currentStartTime));
                    if (currentEvent.getStartTime() != null) {
                        currentEvent.setEndTime(parseTime(currentStartDate, currentEndTime));
                        if (currentEvent.getEndTime() == null) currentEvent.setEndTime(new Date(currentEvent.getStartTime().getTime() + EventUtilities.ONE_HOUR));
                    }
                    if (currentEvent.getTitle() == null || currentEvent.getStartTime() == null) {
                        return;
                    }
                    l.add(currentEvent);
                }
            });
            title.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEvent.setTitle(aBody);
                }
            });
            description.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEvent.setDescription(aBody);
                }
            });
            location.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEvent.setLocation(aBody);
                }
            });
            parknames.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEvent.setLocation2(aBody);
                }
            });
            link.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEvent.setWebsite(aBody);
                }
            });
            contactPhone.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEvent.setTelephone(aBody);
                }
            });
            startDate.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentStartDate = aBody;
                }
            });
            startTime.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentStartTime = aBody;
                }
            });
            endTime.setEndTextElementListener(new EndTextElementListener() {

                @Override
                public void end(String aBody) {
                    currentEndTime = aBody;
                }
            });
            XMLReader reader = XMLReaderFactory.createXMLReader(org.xmlpull.v1.sax2.Driver.class.getName());
            reader.setContentHandler(root.getContentHandler());
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            line = sb.toString().replaceAll("House & Park", "House &amp; Park");
            in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(line.getBytes("UTF-8"))));
            reader.parse(new InputSource(in));
            in.close();
            parsedTime = System.currentTimeMillis();
        } catch (SAXException e) {
            e.printStackTrace();
            return l;
        }
        Collections.sort(l, new Comparator<EventData>() {

            public int compare(EventData e1, EventData e2) {
                return e1.getStartTime().compareTo(e2.getStartTime());
            }
        });
        final long endTime = System.currentTimeMillis();
        Log.i(ParkEventLoader.class.getName(), String.format("getting input stream took %d ms; parsing took %d ms; building took %d ms", (gotISTime - methodStartTime), (parsedTime - gotISTime), (endTime - parsedTime)));
        return l;
    }
