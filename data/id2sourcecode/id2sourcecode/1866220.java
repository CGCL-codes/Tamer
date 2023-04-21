    private boolean load(String location, boolean loadbar) throws Exception {
        countNames.clear();
        events.clear();
        bookmarks.clear();
        days.clear();
        templates.clear();
        File f = new java.io.File(location);
        SiteDocument s = org.penguinuri.penguin.SiteDocument.Factory.parse(f);
        SiteDocument.Site sd = s.getSite();
        Category[] cat = sd.getCategoryArray();
        for (int i = 0; i < cat.length; i++) {
            if (sd.getVersion() < 2 && cat.length == defaultCounts.length) {
                countNames.add(defaultCounts[i]);
            } else countNames.add(cat[i].getTitle());
        }
        EventType[] ev = sd.getEventArray();
        if (ev.length > 0) {
            for (int i = 0; i < ev.length; i++) {
                ImageEvent event = new ImageEvent(ev[i].getName(), ev[i].getInvalid());
                events.add(event);
            }
        } else {
            for (int i = 0; i < defaultImageEvents.length; i++) {
                events.add(defaultImageEvents[i]);
            }
        }
        DateNode[] dn = sd.getDayArray();
        LoadingBar lb = new LoadingBar(dn.length);
        lb.setVisible(loadbar);
        try {
            File templateFolder = new java.io.File(xmlLocation + "//templates");
            File[] temps = templateFolder.listFiles();
            for (int i = 0; i < temps.length; i++) {
                if (temps[i].isFile()) {
                    getTemplate(temps[i].getName());
                }
            }
        } catch (Exception ex) {
            Debug.print(ex);
        }
        try {
            String previousDate = "";
            for (int i = 0; i < dn.length; i++) {
                Day d = new Day(Conversions.getDateFormat().parse(dn[i].getDatestamp()), this);
                if (previousDate.equals(dn[i].getDatestamp())) {
                    sd.getDomNode().removeChild(dn[i].getDomNode());
                    lb.increment();
                    continue;
                }
                previousDate = dn[i].getDatestamp();
                addDay(d);
                TimeNode[] nestImages = dn[i].getTimeArray();
                for (int x = 0; x < nestImages.length; x++) {
                    java.util.Date date = Conversions.getTimeFormat().parse(nestImages[x].getTimestamp());
                    TimeNode image = nestImages[x];
                    EventValue[] eValues = new EventValue[events.size()];
                    Event[] xValues = image.getEventArray();
                    for (int j = 0; j < events.size(); j++) {
                        if (j < xValues.length) {
                            eValues[j] = new EventValue(j, xValues[j].getValue());
                        } else {
                            eValues[j] = new EventValue(j, false);
                        }
                    }
                    if (sd.getVersion() < 3) {
                        eValues[0].setValue(!image.getValid());
                    }
                    NestImage ni = new NestImage(date, String.format("%s/%s", f.getParent(), image.getImg()), getTemplate(image.getTemplate()), eValues, cat.length, image.getUsername());
                    MetaDataHolder[] data = image.getMetadataArray();
                    for (int y = 0; y < data.length; y++) {
                        ni.addDataToNest(y, data[y].getAdults(), data[y].getChicks());
                    }
                    CountType[] count = image.getCountsArray();
                    for (int j = 0; j < count.length; j++) {
                        XYValueNode[] penguin = count[j].getNodeArray();
                        for (int k = 0; k < penguin.length; k++) {
                            XYValueNode current = penguin[k];
                            ni.addPoint(j, new Point(current.getX(), current.getY()));
                        }
                        ni.setCountAsCounted(j, count[j].getCounted());
                    }
                    d.addImage(ni);
                    ni.setComment(image.getComment());
                    ni.setChanged(false);
                }
                lb.increment();
            }
            if (sd.getBookmarkArray().length <= 0) {
                for (int i = 0; i < defaultBookmarkTitles.length; i++) {
                    org.penguinuri.penguin.Bookmark bmark = sd.addNewBookmark();
                    bmark.setTitle(defaultBookmarkTitles[i]);
                    bmark.setDate("");
                    bmark.setTime("");
                }
            }
            org.penguinuri.penguin.Bookmark[] BArray = sd.getBookmarkArray();
            for (int i = 0; i < BArray.length; i++) {
                org.penguinuri.penguin.Bookmark bmark = BArray[i];
                int day = bmark.getDayIndex(), time = bmark.getTimeIndex();
                String sDay = bmark.getDate(), sTime = bmark.getTime();
                if (((sDay == null || sDay.equals("")) || (sTime == null || sTime.equals(""))) && (day < 0 || time < 0)) {
                    addBookmark(new Bookmark(bmark.getTitle()));
                } else {
                    if ((sDay != null && !sDay.equals("")) && (sTime != null && !sTime.equals(""))) {
                        addBookmark(new Bookmark(this, bmark.getTitle(), sDay, sTime));
                    } else {
                        String imageTime = Conversions.getDateFormat().format(this.getDay(day).getImage(time).getTimeDateTaken());
                        String dayDate = Conversions.getDateFormat().format(this.getDay(day).getDate());
                        addBookmark(new Bookmark(bmark.getTitle(), dayDate, imageTime));
                    }
                }
            }
        } catch (Exception ex) {
            lb.setVisible(false);
            throw ex;
        }
        isChanged = false;
        return true;
    }
