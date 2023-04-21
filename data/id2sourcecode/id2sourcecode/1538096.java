    private final void handleCIDEncoding(String encodingType) throws PdfFontException {
        String line = "";
        int begin, end, entry;
        boolean inDefinition = false;
        BufferedReader CIDstream = null;
        if (encodingType.startsWith("/")) encodingType = encodingType.substring(1);
        CIDfontEncoding = encodingType;
        if (encodingType.startsWith("Identity-")) {
            isDoubleByte = true;
        } else {
            if (!isCidJarPresent) {
                isCidJarPresent = true;
                InputStream in = PdfFont.class.getResourceAsStream("/org/jpedal/res/cid/00_ReadMe.pdf");
                if (in == null) throw new PdfFontException("cid.jar not on classpath");
            }
            CMAP = new String[65536];
            if (encodingType.equals("ETenms-B5-H")) encodingType = "ETen-B5-H"; else if (encodingType.equals("ETenms-B5-V")) encodingType = "ETen-B5-V";
            try {
                CIDstream = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/cid/" + encodingType), "Cp1252"));
            } catch (Exception e) {
                LogWriter.writeLog("Problem reading encoding for CID font " + fontID + " " + encodingType + " Check CID.jar installed");
            }
            if (encodingType.equals("UniJIS-UCS2-H")) isDoubleByte = true;
            if (CIDstream != null) {
                while (true) {
                    try {
                        line = CIDstream.readLine();
                    } catch (Exception e) {
                    }
                    if (line == null) break;
                    if (line.indexOf("endcidrange") != -1) inDefinition = false;
                    if (inDefinition == true) {
                        StringTokenizer CIDentry = new StringTokenizer(line, " <>[]");
                        boolean multiple_values = false;
                        if (line.indexOf("[") != -1) multiple_values = true;
                        begin = Integer.parseInt(CIDentry.nextToken(), 16);
                        end = Integer.parseInt(CIDentry.nextToken(), 16);
                        entry = Integer.parseInt(CIDentry.nextToken(), 16);
                        for (int i = begin; i < end + 1; i++) {
                            if (multiple_values == true) {
                                entry = Integer.parseInt(CIDentry.nextToken(), 16);
                                CMAP[i] = "" + (char) entry;
                            } else {
                                CMAP[i] = "" + (char) entry;
                                entry++;
                            }
                        }
                    }
                    if (line.indexOf("begincidrange") != -1) inDefinition = true;
                }
            }
        }
        if (CIDstream != null) {
            try {
                CIDstream.close();
            } catch (Exception e) {
                LogWriter.writeLog("Problem reading encoding for CID font " + fontID + " " + encodingType + " Check CID.jar installed");
            }
        }
    }
