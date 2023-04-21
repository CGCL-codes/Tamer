    private boolean save(String location) {
        try {
            Debug.print(getClass(), "save(): Saving metadata to XML.");
            java.io.File file = new java.io.File(location + "/" + "PenguinData.xml");
            LoadingBar lb = new LoadingBar(days.size() + 2);
            lb.setVisible(true);
            lb.setTitle("Setting up files and folders");
            if (!file.exists()) {
                java.io.File dir = new java.io.File(location);
                dir.mkdirs();
                file.createNewFile();
            }
            SiteDocument s = null;
            SiteDocument.Site site = null;
            try {
                s = SiteDocument.Factory.parse(file);
                site = s.getSite();
            } catch (Exception ex) {
                Debug.print("save(): Site data didn't exist");
                s = SiteDocument.Factory.newInstance();
                site = s.addNewSite();
                site.setSeason(String.format("%s/%s", seasonName, siteName));
                for (int i = 0; i < defaultCounts.length; i++) {
                    Category cat = site.addNewCategory();
                    cat.setTitle(defaultCounts[i]);
                    cat.setId(i);
                }
                for (int i = 0; i < defaultImageEvents.length; i++) {
                    EventType e = site.addNewEvent();
                    e.setInvalid(defaultImageEvents[i].isInvalid());
                    e.setName(defaultImageEvents[i].getName());
                }
                site.setVersion(DATA_VERSION);
                XmlOptions ops = new XmlOptions();
                ops.setSavePrettyPrint().setSavePrettyPrintIndent(4);
                Debug.print(getClass(), "save(): Calling XMLBeans save.");
                s.save(file, ops);
            }
            Category[] cats = site.getCategoryArray();
            if (site.getVersion() < 2 && cats.length == defaultCounts.length) {
                int totalCats = cats.length;
                for (int i = 0; i < totalCats; i++) {
                    site.removeCategory(0);
                }
                for (int i = 0; i < defaultCounts.length; i++) {
                    Category cat = site.addNewCategory();
                    cat.setTitle(defaultCounts[i]);
                    cat.setId(i);
                }
            }
            lb.increment();
            Debug.print(getClass(), "save(): Days to XML");
            Iterator<Day> d = days.iterator();
            int currentDayID = 0;
            while (d.hasNext()) {
                Debug.print("save(): Day (" + currentDayID + ")");
                Day vday = d.next();
                DateNode[] xmlDays = site.getDayArray();
                DateNode currentDay = null;
                for (int i = 0; i < xmlDays.length; i++) {
                    Date xDate = Conversions.getDateFormat().parse(xmlDays[i].getDatestamp());
                    if (xDate.equals(vday.getDate())) {
                        if (currentDay == null) currentDay = xmlDays[i]; else site.getDomNode().removeChild(xmlDays[i].getDomNode());
                    }
                }
                if (currentDay == null) {
                    Debug.print("      -- Day didn't exist in the xml.");
                    currentDay = site.addNewDay();
                    currentDay.setDatestamp(Conversions.getDateFormat().format(vday.getDate()));
                }
                Iterator<NestImage> images = vday.iterator();
                int currentImageID = 0;
                while (images.hasNext()) {
                    Debug.print("      -- image (" + currentImageID + ")");
                    NestImage image = images.next();
                    TimeNode[] xmlTime = currentDay.getTimeArray();
                    TimeNode currentTime = null;
                    for (int i = 0; i < xmlTime.length; i++) {
                        Date xd = Conversions.getTimeFormat().parse(xmlTime[i].getTimestamp());
                        Date id = image.getTimeDateTaken();
                        if (xd.equals(id)) {
                            currentTime = xmlTime[i];
                            break;
                        }
                    }
                    Debug.print("      -- checked other images.");
                    if (currentTime == null) {
                        Debug.print("      -- current time was null");
                        currentTime = currentDay.addNewTime();
                        currentTime.setTimestamp(Conversions.getTimeFormat().format(image.getTimeDateTaken()));
                    }
                    Debug.print("      -- currentTime has value");
                    if (image.getTemplate() != null) {
                        Debug.print("      -- setting template..");
                        Template t = image.getTemplate();
                        currentTime.setTemplate(t.toString());
                        Debug.print("      -- template set");
                    }
                    File f = new File(image.imageSrc);
                    currentTime.setImg(f.getName());
                    Debug.print("      -- set Image file.");
                    int currentDataID = 0;
                    Debug.print(this.getClass(), String.format("Saving Event Data for %s", currentTime.getTimestamp()));
                    for (int i = 0; i < image.eventValues.length; i++) {
                        Event e;
                        if (currentTime.getEventArray().length < i + 1) {
                            currentTime.addNewEvent();
                        }
                        e = currentTime.getEventArray()[i];
                        e.setValue(image.eventValues[i].getValue());
                        e.setId(i);
                    }
                    Iterator<NestData> nestDataIt = image.nests.iterator();
                    Debug.print(this.getClass(), String.format("Saving NestData for %s", currentTime.getTimestamp()));
                    while (nestDataIt.hasNext()) {
                        NestData data = nestDataIt.next();
                        MetaDataHolder[] xmlMeta = currentTime.getMetadataArray();
                        MetaDataHolder currentMeta = null;
                        if (currentDataID < xmlMeta.length) {
                            currentMeta = xmlMeta[currentDataID];
                        } else {
                            currentMeta = currentTime.addNewMetadata();
                            currentMeta.setNodeID(currentDataID);
                        }
                        currentMeta.setAdults(data.getAdults());
                        currentMeta.setChicks(data.getChicks());
                        currentDataID++;
                    }
                    Debug.print(this.getClass(), String.format("Saving Count Data for %s", currentTime.getTimestamp()));
                    if (currentTime.getCountsArray().length < this.countNames.size()) {
                        int start = currentTime.getCountsArray().length;
                        int end = this.countNames.size();
                        Debug.print("Counts are different Sizes, in XML: " + start + " in Array: " + end);
                        for (int i = start; end != start && i < end; i++) {
                            CountType c = currentTime.addNewCounts();
                        }
                    }
                    Debug.print(this.getClass(), String.format("Saving Count Names for %s", currentTime.getTimestamp()));
                    for (int i = 0; i < this.countNames.size(); i++) {
                        Debug.print("-- Count Names" + countNames.get(i).toString());
                        Iterator<Point> dataIt = image.getCount(i).iterator();
                        CountType currentCount = currentTime.getCountsArray(i);
                        if (currentCount == null) {
                            currentCount = currentTime.addNewCounts();
                        } else {
                            XYValueNode[] chicks = currentCount.getNodeArray();
                            for (int j = 0; j < chicks.length; j++) {
                                currentCount.removeNode(0);
                            }
                        }
                        currentCount.setCounted(image.isCountCounted(i));
                        currentCount.setCatid(i);
                        while (dataIt.hasNext()) {
                            Point p = dataIt.next();
                            XYValueNode newPoint = currentTime.getCountsArray(i).addNewNode();
                            newPoint.setX((int) p.getX());
                            newPoint.setY((int) p.getY());
                        }
                    }
                    currentTime.setComment(image.getComment());
                    if (image.isChanged()) currentTime.setUsername(System.getProperty("user.name"));
                    currentImageID++;
                    image.setChanged(false);
                }
                lb.increment();
                currentDayID++;
            }
            Debug.print(getClass(), "save(): Fixing bookmarks.");
            int maxBookmarks = site.getBookmarkArray().length;
            for (int i = 0; i < maxBookmarks; i++) {
                site.removeBookmark(0);
            }
            for (int i = 0; i < bookmarks.size(); i++) {
                org.penguinuri.penguin.Bookmark bmark = site.addNewBookmark();
                Bookmark b = bookmarks.get(i);
                bmark.setTitle(b.getTitle());
                bmark.setDate(b.getDateString());
                bmark.setTime(b.getTimeString());
            }
            int maxEventTypes = site.getEventArray().length;
            for (int i = 0; i < maxEventTypes; i++) {
                site.removeEvent(0);
            }
            for (int i = 0; i < events.size(); i++) {
                EventType event = site.addNewEvent();
                ImageEvent iEvent = events.get(i);
                event.setId(i);
                event.setName(iEvent.getName());
                event.setInvalid(iEvent.isInvalid());
            }
            site.setVersion(DATA_VERSION);
            XmlOptions ops = new XmlOptions();
            ops.setSavePrettyPrint().setSavePrettyPrintIndent(4);
            Debug.print(getClass(), "save(): Calling XMLBeans save.");
            s.save(file, ops);
            Debug.print(getClass(), "save(): Finished saving metadata to XML.");
            Debug.print(getClass(), "save(): Saving templates.");
            lb.setTitle("Saving templates");
            Iterator<Template> ti = templates.values().iterator();
            while (ti.hasNext()) {
                ti.next().save();
            }
            lb.increment();
            Debug.print("[site] save(): Templates saved.");
        } catch (Exception ex) {
            Debug.print(ex);
            return false;
        }
        return true;
    }
