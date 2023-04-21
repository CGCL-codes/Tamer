    public void start() {
        InputStream listData = null;
        InputStream resultData = null;
        try {
            URL listUrl = new URL("http://i-gnoramus.googlecode.com/svn/trunk/I-gnoramus/prototype/res/ibis_french.xml");
            listData = listUrl.openStream();
            URL resultUrl = new URL("http://i-gnoramus.googlecode.com/svn/trunk/I-gnoramus/prototype/res/ibis_results.xml");
            resultData = resultUrl.openStream();
            loadXmlList(listData, resultData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                resultData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream testDialogDefinitionData = null;
        try {
            URL url = new URL("http://i-gnoramus.googlecode.com/svn/trunk/I-gnoramus/prototype/res/testdialog.xml");
            testDialogDefinitionData = url.openStream();
            testDialogDefinition = itemListParser.loadTestDialogDefinition(testDialogDefinitionData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                testDialogDefinitionData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
