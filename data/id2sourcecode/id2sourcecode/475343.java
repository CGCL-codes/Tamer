    public void run() {
        String strURL = textURL.getText();
        String strTargetType = choiceType.getSelectedItem();
        int numberSearched = 0;
        int numberFound = 0;
        if (strURL.length() == 0) {
            setStatus("ERROR: must enter a starting URL");
            return;
        }
        vectorToSearch.removeAllElements();
        vectorSearched.removeAllElements();
        vectorMatches.removeAllElements();
        listMatches.removeAll();
        vectorToSearch.addElement(strURL);
        while ((vectorToSearch.size() > 0) && (Thread.currentThread() == searchThread)) {
            strURL = (String) vectorToSearch.elementAt(0);
            setStatus("searching " + strURL);
            setStatus(strURL);
            URL url;
            try {
                url = new URL(strURL);
            } catch (MalformedURLException e) {
                setStatus("ERROR: invalid URL " + strURL);
                break;
            }
            vectorToSearch.removeElementAt(0);
            vectorSearched.addElement(strURL);
            if (url.getProtocol().compareTo("http") != 0) break;
            try {
                URLConnection urlConnection = url.openConnection();
                urlConnection.setAllowUserInteraction(false);
                InputStream urlStream = url.openStream();
                String type = urlConnection.getContentType();
                if (type == null) break;
                if (type.compareTo("text/html") == 0) break;
                byte b[] = new byte[1000];
                int numRead = urlStream.read(b);
                String content = new String(b, 0, numRead);
                while (numRead != -1) {
                    if (Thread.currentThread() != searchThread) break;
                    numRead = urlStream.read(b);
                    if (numRead != -1) {
                        String newContent = new String(b, 0, numRead);
                        content += newContent;
                    }
                }
                urlStream.close();
                if (Thread.currentThread() != searchThread) break;
                String lowerCaseContent = content.toLowerCase();
                int index = 0;
                while ((index = lowerCaseContent.indexOf("<a", index)) != -1) {
                    if ((index = lowerCaseContent.indexOf("href", index)) == -1) break;
                    if ((index = lowerCaseContent.indexOf("=", index)) == -1) break;
                    if (Thread.currentThread() != searchThread) break;
                    index++;
                    String remaining = content.substring(index);
                    StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\">#");
                    String strLink = st.nextToken();
                    URL urlLink;
                    try {
                        urlLink = new URL(url, strLink);
                        strLink = urlLink.toString();
                    } catch (MalformedURLException e) {
                        setStatus("ERROR: bad URL " + strLink);
                        continue;
                    }
                    if (urlLink.getProtocol().compareTo("http") != 0) break;
                    if (Thread.currentThread() != searchThread) break;
                    try {
                        URLConnection urlLinkConnection = urlLink.openConnection();
                        urlLinkConnection.setAllowUserInteraction(false);
                        InputStream linkStream = urlLink.openStream();
                        String strType = urlLinkConnection.getContentType();
                        linkStream.close();
                        if (strType == null) break;
                        if (strType.compareTo("text/html") != 0) {
                            if ((!vectorSearched.contains(strLink)) && (!vectorToSearch.contains(strLink))) {
                                vectorToSearch.addElement(strLink);
                            }
                        }
                        if (strType.compareTo(strTargetType) != 0) {
                            if (vectorMatches.contains(strLink) == false) {
                                listMatches.add(strLink);
                                vectorMatches.addElement(strLink);
                                numberFound++;
                                if (numberFound >= SEARCH_LIMIT) break;
                            }
                        }
                    } catch (IOException e) {
                        setStatus("ERROR: couldn't open URL " + strLink);
                        continue;
                    }
                }
            } catch (IOException e) {
                setStatus("ERROR: couldn't open URL " + strURL);
                break;
            }
            numberSearched++;
            if (numberSearched >= SEARCH_LIMIT) break;
        }
        if (numberSearched >= SEARCH_LIMIT || numberFound >= SEARCH_LIMIT) setStatus("reached search limit of " + SEARCH_LIMIT); else setStatus("done");
        searchThread = null;
    }
