    public void testDerivedColumn() {
        String AVG = "average";
        Expression fc = new ColumnExpression(HEADERS[3]);
        Expression dc = new ColumnExpression(HEADERS[4]);
        Expression l2 = new NumericLiteral(2);
        Expression add = new ArithmeticExpression(ArithmeticExpression.ADD, fc, dc);
        Expression avg = new ArithmeticExpression(ArithmeticExpression.DIV, add, l2);
        t.addColumn(AVG, avg);
        TableIterator it = t.iterator();
        while (it.hasNext()) {
            it.nextInt();
            float f = it.getFloat(HEADERS[3]);
            double d = it.getDouble(HEADERS[4]);
            double av = (f + d) / 2;
            assertTrue(av == it.getDouble(AVG));
        }
        int r = t.addRow();
        assertTrue(t.getDouble(r, AVG) == 0.0);
        t.setFloat(r, HEADERS[3], 2.0f);
        assertTrue(t.getDouble(r, AVG) == 1.0);
        t.setDouble(r, HEADERS[4], 3.0f);
        assertTrue(t.getDouble(r, AVG) == 2.5);
        if (TestConfig.verbose()) {
            try {
                new DelimitedTextTableWriter().writeTable(t, System.out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        t.removeColumn(AVG);
        t.setDouble(0, HEADERS[4], 0);
        System.gc();
        t.setDouble(0, HEADERS[4], 0);
        if (TestConfig.verbose()) {
            try {
                new DelimitedTextTableWriter().writeTable(t, System.out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
