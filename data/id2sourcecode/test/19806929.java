    public Request(PipedInputStream pisI, int debugI, String source, boolean bUltimateHostSpecifiedI) {
        pis = pisI;
        debug = debugI;
        source = source;
        bUltimateHostSpecified = bUltimateHostSpecifiedI;
        boolean done = false;
        Vector headerV = new Vector();
        int nAvail = 0;
        int nRead = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(pis));
        try {
            while (!done) {
                String nextLine = br.readLine();
                if (debug > 4) {
                    System.err.println("Request nextLine " + nextLine);
                }
                if (nextLine == null) {
                    done = true;
                } else {
                    if (nextLine.length() == 0) {
                        headerV.add("Connection: close");
                        headerV.add(nextLine);
                        done = true;
                    } else if ((!nextLine.toLowerCase().startsWith("keep-alive")) && (!nextLine.toLowerCase().startsWith("connection"))) {
                        if (nextLine.regionMatches(true, 0, authorizationLabel, 0, authorizationLabel.length())) {
                            authorizationString = nextLine.substring(authorizationLabel.length()).trim();
                            if ((authorizationString != null) && (authorizationString.trim().equals(""))) {
                                authorizationString = null;
                            }
                            authorizationString = authorizationString.trim();
                        } else if (nextLine.regionMatches(true, 0, optionLabel, 0, optionLabel.length())) {
                            namespaceCode = nextLine.substring(optionLabel.length()).trim();
                            if ((namespaceCode != null) && (namespaceCode.trim().equals(""))) {
                                namespaceCode = null;
                            }
                            namespaceCode = namespaceCode.trim();
                        } else if (nextLine.regionMatches(true, 2, sourceIPLabel, 0, sourceIPLabel.length()) && (namespaceCode != null) && (nextLine.regionMatches(true, 0, namespaceCode, 0, namespaceCode.length()))) {
                            ipAddress = nextLine.substring(sourceIPLabel.length() + 2).trim();
                            if ((ipAddress != null) && (ipAddress.trim().equals(""))) {
                                ipAddress = null;
                            }
                            ipAddress = ipAddress.trim();
                        } else {
                            headerV.add(nextLine);
                        }
                    }
                }
            }
            numHeaderLines = headerV.size();
            headerA = new String[numHeaderLines];
            headerV.copyInto(headerA);
            for (int i = numHeaderLines - 1; i >= 0; i--) {
                if (headerA[i].regionMatches(true, 0, contentLabel, 0, contentLabel.length())) {
                    contentLength = Integer.parseInt(headerA[i].substring(contentLabel.length()));
                } else if (headerA[i].regionMatches(true, 0, modifiedLabel, 0, modifiedLabel.length())) {
                    String modifiedString = headerA[i].substring(modifiedLabel.length());
                }
            }
            if (contentLength > 0) {
                content = new char[contentLength];
                int numRead = br.read(content, 0, contentLength);
                while (numRead < contentLength) {
                    numRead += br.read(content, numRead, contentLength - numRead);
                }
            }
        } catch (Exception e) {
            System.err.println("Request exception: " + e.getMessage());
            isNull = true;
        }
    }
