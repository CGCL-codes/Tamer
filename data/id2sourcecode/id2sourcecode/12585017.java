    public void doTest(String action, String expectedText) throws Exception {
        URL url = new URL("http://" + server + "/NmrshiftdbServlet?nmrshiftdbaction=" + action);
        Reader is = new InputStreamReader(url.openStream());
        BufferedReader in = new BufferedReader(is);
        for (String s; (s = in.readLine()) != null; ) {
            if (s.contains(expectedText)) {
                in.close();
                return;
            }
        }
        in.close();
        throw new Exception("unexpected text");
    }
