    private static Model getSwoogleQueryResultModel(String queryTypeAndSearchString) {
        String restQuery = SWOOGLE_WEB_SERVICE_URI + queryTypeAndSearchString + SWOOGLE_WEB_SERVICE_KEY;
        Model model = null;
        try {
            System.out.println(queryTypeAndSearchString);
            int index = swoogleQueryList.lastIndexOf(queryTypeAndSearchString);
            index += 1;
            File queryCachFile = new File(SWOOGLE_QUERY_RESULTS_DIR + File.separator + "query_" + index);
            if (queryCachFile.exists()) {
                DODDLE.getLogger().log(Level.DEBUG, "Using Cashed Data");
                model = getModel(new FileInputStream(queryCachFile), DODDLEConstants.BASE_URI);
            } else {
                queryCachFile = new File(SWOOGLE_QUERY_RESULTS_DIR + File.separator + "query_" + (swoogleQueryList.size() + 1));
                URL url = new URL(restQuery);
                saveQueryResult(queryTypeAndSearchString, queryCachFile, url.openStream());
                model = getModel(url.openStream(), DODDLEConstants.BASE_URI);
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (model == null) {
            model = ModelFactory.createDefaultModel();
        }
        return model;
    }
