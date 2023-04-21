    public void doAction() throws Exception {
        if (mStatus == null) return;
        if (mResolver == null) return;
        Profiler profiler = new Profiler();
        String buffer = mStatus + "?a=" + mAuthority + "&u=yes&d=yes";
        if (mStartDate != null) buffer += "&b=" + igpp.util.Encode.urlEncode(mStartDate);
        if (mStopDate != null) buffer += "&e=" + igpp.util.Encode.urlEncode(mStopDate);
        if (mVerbose) System.out.print("Service call: " + buffer);
        Document doc = XMLGrep.parse(buffer);
        ArrayList<Pair> docIndex = XMLGrep.makeIndex(doc, "");
        ArrayList<String> modified = XMLGrep.getValues(docIndex, ".*/Results/Modified");
        ArrayList<String> newItems = XMLGrep.getValues(docIndex, ".*/Results/New");
        ArrayList<String> deleted = XMLGrep.getValues(docIndex, ".*/Results/Deleted");
        URL url = new URL(mIndexer);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "text/xml");
        urlConn.setRequestProperty("charset", "utf-8");
        PrintStream outStream = new PrintStream(urlConn.getOutputStream());
        profiler.setOutput(outStream);
        for (String item : deleted) {
            if (mVerbose) System.out.println("Processing (deleted): " + item);
            profiler.makeDeleteItem(item);
        }
        profiler.writeProfileHeader();
        for (String item : modified) {
            buffer = mResolver + "?i=" + item;
            if (mVerbose) System.out.println("Processing (modified): " + item);
            profiler.makeProfile(buffer);
        }
        for (String item : newItems) {
            buffer = mResolver + "?i=" + item;
            if (mVerbose) System.out.println("Processing (new): " + item);
            profiler.makeProfile(buffer);
        }
        profiler.writeProfileFooter();
        profiler.getOutput().flush();
        profiler.getOutput().close();
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        while (null != (str = input.readLine())) {
            System.out.println(str);
        }
        input.close();
        sendCommit();
    }
