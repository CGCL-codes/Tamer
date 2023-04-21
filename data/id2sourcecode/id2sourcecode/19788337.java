    public void crawl() {
        File file = new File("c:/Seachurin/conf/FeedList.txt");
        FileWriter fw = null;
        FileWriter fw1 = null;
        BufferedReader in = null;
        try {
            FileReader fr = new FileReader(file);
            in = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            System.out.println("File Disappeared!");
        }
        File out1 = new File("c:/Seachurin/crawldb/index.txt");
        try {
            fw1 = new FileWriter(out1);
        } catch (IOException e) {
            System.out.println("Can not open stream fw");
        }
        PrintWriter pw1 = new PrintWriter(fw1);
        vectorToSearch.removeAllElements();
        vectorSearched.removeAllElements();
        vectorMatches.removeAllElements();
        URLConnection.setDefaultAllowUserInteraction(false);
        String strURL = null;
        int numberSearched = 0;
        int numberFound = 1;
        try {
            while ((strURL = in.readLine()) != null) vectorToSearch.addElement(strURL);
        } catch (IOException e) {
            System.out.println(" in.readLine() errors!");
        }
        while (vectorToSearch.size() > 0) {
            strURL = (String) vectorToSearch.elementAt(0);
            if (strURL.length() == 0) {
                System.out.println("ERROR: must enter a starting URL");
                return;
            }
            System.out.println("searching " + strURL);
            URL url;
            String str1 = strURL.concat("/siteinfo.xml");
            String str2 = strURL.concat("/index.html");
            String category = null;
            if ((category = checkSiteInfo(str1)) == null) break; else {
            }
            try {
                url = new URL(str2);
            } catch (MalformedURLException e) {
                System.out.println("ERROR: invalid URL " + strURL);
                break;
            }
            vectorToSearch.removeElementAt(0);
            vectorSearched.addElement(str2);
            if (url.getProtocol().compareTo("http") != 0) break;
            if (!robotSafe(url)) break;
            try {
                URLConnection urlConnection = url.openConnection();
                urlConnection.setAllowUserInteraction(false);
                InputStream urlStream = url.openStream();
                String type = urlConnection.getContentType();
                System.out.println(" File Type is: " + type);
                if (type == null) break;
                byte b[] = new byte[1000];
                int numRead = urlStream.read(b);
                String content = new String(b, 0, numRead);
                while (numRead != -1) {
                    numRead = urlStream.read(b);
                    if (numRead != -1) {
                        String newContent = new String(b, 0, numRead);
                        content += newContent;
                    }
                }
                urlStream.close();
                String fileName = "c:/Seachurin/crawldb/doc" + numberFound + ".txt";
                File out = new File(fileName);
                try {
                    fw = new FileWriter(out);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(content);
                    fw.close();
                } catch (IOException e) {
                    System.out.println("Can not open stream fw");
                }
                System.out.println(content);
                pw1.print(fileName + "\t");
                pw1.print(str2 + "\t");
                pw1.print(category);
                pw1.println();
                fw1.close();
                numberSearched++;
                String lowerCaseContent = content.toLowerCase();
                int index = 0;
                while ((index = lowerCaseContent.indexOf("<a", index)) != -1) {
                    if ((index = lowerCaseContent.indexOf("href", index)) == -1) break;
                    if ((index = lowerCaseContent.indexOf("=", index)) == -1) break;
                    index++;
                    String remaining = content.substring(index);
                    StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\">#");
                    String strLink = st.nextToken();
                    URL urlLink;
                    try {
                        urlLink = new URL(url, strLink);
                        strLink = urlLink.toString();
                    } catch (MalformedURLException e) {
                        System.out.println("ERROR: bad URL " + strLink);
                        continue;
                    }
                    if (urlLink.getProtocol().compareTo("http") != 0) break;
                    try {
                        URLConnection urlLinkConnection = urlLink.openConnection();
                        urlLinkConnection.setAllowUserInteraction(false);
                        InputStream linkStream = urlLink.openStream();
                        String strType = urlLinkConnection.guessContentTypeFromStream(linkStream);
                        linkStream.close();
                        if (strType == null) break;
                        if ((strType.compareTo("text/html") == 0) || (strType.compareTo("application/pdf") == 0)) {
                            if ((!vectorSearched.contains(strLink)) && (!vectorToSearch.contains(strLink))) {
                                if (robotSafe(urlLink)) vectorToSearch.addElement(strLink);
                            }
                        }
                        if ((strType.compareTo("text/html") == 0) || (strType.compareTo("application/pdf") == 0)) {
                            if (vectorMatches.contains(strLink) == false) {
                                vectorMatches.addElement(strLink);
                                numberFound++;
                                if (numberFound >= SEARCH_LIMIT) break;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("ERROR: couldn't open URL " + strLink);
                        continue;
                    }
                }
            } catch (IOException e) {
                System.out.println("ERROR: couldn't open URL " + strURL);
                break;
            }
            numberSearched++;
            if (numberSearched >= SEARCH_LIMIT) break;
        }
        if (numberSearched >= SEARCH_LIMIT || numberFound >= SEARCH_LIMIT) System.out.println("reached search limit of " + SEARCH_LIMIT); else System.out.println("done");
    }
