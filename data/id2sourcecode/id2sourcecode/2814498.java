    public VersionInfo getLastVersion() {
        VersionInfo versionInfo = null;
        try {
            URL url = new URL(RELEASES_RSS);
            URLConnection connection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(br);
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String name = reader.getLocalName();
                    if (name.equals("item")) {
                        if (versionInfo != null) {
                            break;
                        }
                        versionInfo = new VersionInfo();
                    }
                    if (versionInfo != null && name.equals("title")) {
                        String versionStr = getTextElement(reader);
                        if (versionStr == null) {
                            continue;
                        }
                        Pattern pattern = Pattern.compile(VERSION_REGEXP);
                        Matcher matcher = pattern.matcher(versionStr);
                        if (matcher.find()) {
                            versionInfo.setVersion(matcher.group(1));
                        } else {
                            versionInfo = null;
                        }
                        continue;
                    }
                    if (versionInfo != null && name.equals("pubDate")) {
                        versionInfo.setReleaseDate(getTextElement(reader));
                        continue;
                    }
                    if (versionInfo != null && name.equals("link")) {
                        versionInfo.setNotesLink(getTextElement(reader));
                        versionInfo.setFilesLink(versionInfo.getNotesLink());
                        continue;
                    }
                }
            }
            setStatus(versionInfo);
        } catch (Exception e) {
            timeSlotTracker.errorLog(e);
        }
        return versionInfo;
    }
