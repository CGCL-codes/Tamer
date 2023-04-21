    public static void main(String[] args) {
        int exitCode = 0;
        long count = 0;
        String poolAddress = "centera1.cascommunity.org";
        InputStreamReader inputReader = new InputStreamReader(System.in);
        BufferedReader stdin = new BufferedReader(inputReader);
        TimeZone TZ = TimeZone.getTimeZone("GMT");
        FPQueryResult queryResult = null;
        int queryStatus = 0;
        Calendar timeConverter = Calendar.getInstance(TZ);
        try {
            System.out.print("Address of cluster[" + poolAddress + "]: ");
            String answer = stdin.readLine();
            if (!answer.equals("")) poolAddress = answer;
            System.out.println("Connecting to Centera cluster(" + poolAddress + ")");
            FPPool.setGlobalOption(FPLibraryConstants.FP_OPTION_OPENSTRATEGY, FPLibraryConstants.FP_LAZY_OPEN);
            FPPool thePool = new FPPool(poolAddress);
            if (thePool.getCapability(FPLibraryConstants.FP_CLIPENUMERATION, FPLibraryConstants.FP_ALLOWED) == "False") {
                throw new IllegalArgumentException("Query is not supported for this pool connection.");
            }
            System.out.println("Query capability is enabled for this pool connection.");
            DateFormat dFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
            dFormat.setTimeZone(TZ);
            Calendar clusterTime = Calendar.getInstance(TZ);
            clusterTime.setTime(new Date(thePool.getClusterTime()));
            System.out.println("Cluster Time: " + dFormat.format(clusterTime.getTime()));
            Calendar epoch = Calendar.getInstance(TZ);
            epoch.setTime(new Date(0));
            String start = ConvertDateTimeToString(epoch);
            System.out.print("Query start time in GMT(YYYY.MM.DD.hh.mm.ss)[" + start + "]: ");
            answer = stdin.readLine();
            if (!answer.equals("")) start = answer;
            Calendar startTime = ConvertDisplayToCalendar(start);
            String end = ConvertDateTimeToString(clusterTime);
            System.out.print("Query end time in GMT(YYYY.MM.DD.hh.mm.ss)[" + end + "]: ");
            answer = stdin.readLine();
            if (!answer.equals("")) end = answer;
            Calendar endTime = ConvertDisplayToCalendar(end);
            System.out.println("\nStarting Query:  Starting " + dFormat.format(startTime.getTime()) + ", Ending " + dFormat.format(endTime.getTime()));
            FPQueryExpression queryExp = new FPQueryExpression();
            queryExp.setStartTime(startTime.getTime().getTime());
            queryExp.setEndTime(endTime.getTime().getTime());
            queryExp.setType(FPLibraryConstants.FP_QUERY_TYPE_EXISTING);
            queryExp.selectField("creation.date");
            queryExp.selectField("modification.date");
            FPPoolQuery theQuery = new FPPoolQuery(thePool, queryExp);
            clusterTime.setTime(new Date(thePool.getClusterTime()));
            System.out.println("Current Cluster Time is " + dFormat.format(clusterTime.getTime()));
            while (true) {
                queryResult = theQuery.FetchResult();
                queryStatus = queryResult.getResultCode();
                if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_OK) {
                    timeConverter.setTime(new Date(queryResult.getTimestamp()));
                    System.out.println("Clip ID: " + queryResult.getClipID() + " Query timestamp: " + dFormat.format(timeConverter.getTime()) + " creation date: " + queryResult.getField("creation.date") + "modification time on clip : " + queryResult.getField("modification.date"));
                    count++;
                } else if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_INCOMPLETE) {
                    System.out.println("Received FP_QUERY_RESULT_CODE_INCOMPLETE error, invalid C-Clip, trying again.");
                } else if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_COMPLETE) {
                    System.out.println("Received FP_QUERY_RESULT_CODE_COMPLETE, there should have been a previous " + "FP_QUERY_RESULT_CODE_INCOMPLETE error reported.");
                } else if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_END) {
                    System.out.println("End of query reached, exiting.");
                    break;
                } else if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_ABORT) {
                    System.out.println("received FP_QUERY_RESULT_CODE_ABORT error, exiting.");
                    break;
                } else if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_ERROR) {
                    System.out.println("received FP_QUERY_RESULT_CODE_ERROR error, retrying again");
                } else if (queryStatus == FPLibraryConstants.FP_QUERY_RESULT_CODE_PROGRESS) {
                    System.out.println("received FP_QUERY_RESULT_CODE_PROGRESS, continuing.");
                } else {
                    System.out.println("received error: " + queryStatus);
                    break;
                }
                queryResult.Close();
            }
            queryResult.Close();
            System.out.println("\nTotal number of clips \t" + count);
            theQuery.Close();
            stdin.close();
            thePool.Close();
            System.out.println("\nClosed connection to Centera cluster (" + poolAddress + ")");
        } catch (FPLibraryException e) {
            exitCode = e.getErrorCode();
            System.err.println("Centera SDK Error: " + e.getMessage() + "(" + exitCode + ")");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            exitCode = -1;
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            exitCode = -1;
        }
        System.exit(exitCode);
    }
