    public static LomType makeLomFromPairsAndSize(String[][] pairs, long size) {
        InputStream is = MetadataHelper.class.getResourceAsStream("/res/fetlar-md.xml");
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String mdTemplate = sb.toString();
        for (String[] pair : pairs) {
            mdTemplate = updateStringFromPair(pair, mdTemplate);
        }
        if (Preferences.userRoot().node("Spectatus").get("TaxonNoFETLAR", "N").equals("Y")) {
            mdTemplate = mdTemplate.replaceAll("(?s)\\<!-- FETLAR-CLASS-START --\\>.*<!-- FETLARCLASSEND -->", "");
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = df.format(date);
        mdTemplate = mdTemplate.replace("{{DATE}}", dateStr);
        mdTemplate = mdTemplate.replace("{{RANDOM-HASH}}", MiscUtils.fullUUID());
        mdTemplate = mdTemplate.replace("{{MIME-TYPE}}", "text/x-imsqti-test-xml");
        mdTemplate = mdTemplate.replace("{{TYPE}}", "ExaminationTest");
        mdTemplate = mdTemplate.replace("{{RESDESC}}", "A test");
        mdTemplate = mdTemplate.replace("{{FETLAR}}", "www.fetlar.ac.uk");
        if (size != -1) mdTemplate = mdTemplate.replace("{{SIZE}}", String.valueOf(size)); else mdTemplate = mdTemplate.replace("{{SIZE}}", "0");
        long epoch = System.currentTimeMillis() / 1000;
        mdTemplate = mdTemplate.replace("{{UNIQUE-ID}}", "FETLAR-2-" + String.valueOf(epoch));
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = String.valueOf(epoch).getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            String md5 = String.format("%1$032X", i);
            mdTemplate = mdTemplate.replace("{{RANDOM-HASH}}", md5);
        } catch (NoSuchAlgorithmException e) {
        }
        mdTemplate = mdTemplate.replaceAll(">\\s+$", ">");
        mdTemplate = mdTemplate.replaceAll(">\\s+<", "><");
        mdTemplate = mdTemplate.replaceAll("^\\s<", "<");
        try {
            return (GroundControl.fileHelper.getLomFromString(mdTemplate));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
