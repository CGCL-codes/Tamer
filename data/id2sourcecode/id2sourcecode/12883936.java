    public void testFuzz() throws Exception {
        Model model = getTestModel();
        StackEvaluator eval = new LazyStackEvaluator();
        PrintStream errStream = new PrintStream(new NullOutputStream());
        QueryEngine qe = new QueryEngine(model, eval, System.out, errStream);
        Collector<RippleList, RippleException> expected = new Collector<RippleList, RippleException>();
        Collector<RippleList, RippleException> results = new Collector<RippleList, RippleException>();
        QueryPipe qp = new QueryPipe(qe, results);
        ModelConnection mc = qe.getConnection();
        RippleValue five = mc.value(5);
        byte[] bytes = new byte[128];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (i >= 32) ? (byte) i : (i >= 16) ? (byte) '\n' : (byte) '\t';
        }
        for (int i = 0; i < REPEAT; i++) {
            int len = MAX_EXPR_LENGTH + rand.nextInt(MAX_EXPR_LENGTH - MIN_EXPR_LENGTH);
            byte[] expr = new byte[len];
            for (int j = 0; j < len; j++) {
                expr[j] = bytes[rand.nextInt(bytes.length)];
            }
            String s = new String(expr);
            qp.put(new String(expr));
            qp.put(".\n");
            qp.put(".\n");
            qp.put(".\n");
            results.clear();
            qp.put("2 3 add >> .\n");
            expected.clear();
            expected.put(createStack(mc, five));
            assertCollectorsEqual(expected, results);
        }
        qp.close();
        mc.close();
    }
