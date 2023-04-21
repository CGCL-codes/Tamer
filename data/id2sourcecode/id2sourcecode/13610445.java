    public ReadableByteChannel readFile(String filename, long position, Session userSession) throws FailedActionException {
        log.debug("trying to get " + filename + " from " + currentDir);
        String uuid = null;
        try {
            uuid = extractUUIDFromDirPath(filename);
        } catch (Exception e) {
            uuid = extractUUIDFromDirPath(currentDir);
        }
        Element md = searchUUIDS.get(uuid);
        Element info = md.getChild(GeoNetworkContext.GEONETINFO, GeoNetworkContext.GEONET_NAMESPACE);
        String id = info.getChildText("id");
        File file = new File(filename);
        String cfilename = getDir(GeoNetworkContext.dataDir, "private", id) + "/" + file.getName();
        if (filename.equals("metadata.xml") || filename.equals("license.html")) {
            Element disclaimer = getXmlFromGeoNetwork(GeoNetworkContext.url + "/" + GeoNetworkContext.disclaimerService + "?uuid=" + uuid + "&access=private", userSession);
            Element license = disclaimer.getChild("license");
            Element metadata = disclaimer.getChild("metadata");
            String output = null;
            if (filename.equals("license.html")) {
                if (license.getContentSize() > 0) {
                    output = Xml.getString((Element) license.getChildren().get(0));
                } else {
                    output = "No license";
                }
            }
            if (filename.equals("metadata.xml")) {
                output = Xml.getString((Element) metadata.getChildren().get(0));
            }
            ByteArrayInputStream input = new ByteArrayInputStream(output.getBytes());
            return Channels.newChannel(input);
        } else {
            try {
                FileInputStream fis = new FileInputStream(new File(cfilename));
                FileChannel fc = fis.getChannel();
                if (position > 0) return fc.position(position); else return fc;
            } catch (Exception e) {
                e.printStackTrace();
                throw new FailedActionException(FailedActionReason.SYSTEM_ERROR);
            }
        }
    }
