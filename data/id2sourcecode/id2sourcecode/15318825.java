    public static void main(String[] args) throws XMPPException, InterruptedException {
        SetLogForTest.set();
        P2PClient p2p = new P2PClient();
        p2p.connect();
        p2p.getAuthManager().login("Murray", "Muahahahahahahahahahahah");
        Thread.sleep(5000);
        List<DescriptionContent> results = p2p.getSearchResultManager(new SearchRequest()).getResults();
        Thread.sleep(5000);
        System.out.println(results.size() + " resultados.");
        for (DescriptionContent dc : results) {
            System.out.println(dc.getUserContent().getURL());
        }
        p2p.getAuthManager().logout();
        p2p.disconnect();
    }
