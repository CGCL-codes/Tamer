    @Test
    public void testNegCheck() {
        URL url = NegExTest.class.getClassLoader().getResource("negex_triggers.txt");
        try {
            Scanner rulesScanner = new Scanner(url.openStream());
            ArrayList<String> rules = new ArrayList<String>();
            while (rulesScanner.hasNextLine()) {
                String s = rulesScanner.nextLine();
                rules.add(s);
            }
            NegEx negex = new NegEx(rules);
            NegExResult result = negex.negCheck("This is gene free.", 8, 12, true);
            assertTrue(result.getResult() == NegExResult.NEGATED);
            result = negex.negCheck("This is not a gene.", 14, 18, true);
            assertTrue(result.getResult() == NegExResult.NEGATED);
            result = negex.negCheck("This is a gene.", 10, 14, true);
            assertTrue(result.getResult() == NegExResult.NOT_NEGATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
