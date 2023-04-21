    @Theory
    public void testReorderModelBodyColumn(int dragFromModelBodyColumn, int dragToModelBodyColumn, @BodyConfigDataPoint IBodyConfig bodyConfig) {
        ColumnTransformSupport support = new ColumnTransformSupport(bodyConfig);
        support.reorderModelBodyColumn(dragFromModelBodyColumn, dragToModelBodyColumn);
        int[] expected = new int[bodyConfig.getColumnCount()];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i;
        }
        if (Math.max(dragFromModelBodyColumn, dragToModelBodyColumn) < bodyConfig.getColumnCount() && Math.min(dragFromModelBodyColumn, dragToModelBodyColumn) >= 0) {
            if (dragFromModelBodyColumn < dragToModelBodyColumn) {
                for (int i = dragFromModelBodyColumn; i < dragToModelBodyColumn; i++) {
                    expected[i] = expected[i + 1];
                }
                expected[dragToModelBodyColumn] = dragFromModelBodyColumn;
            } else if (dragToModelBodyColumn < dragFromModelBodyColumn) {
                for (int i = dragFromModelBodyColumn; i > dragToModelBodyColumn; i--) {
                    expected[i] = expected[i - 1];
                }
                expected[dragToModelBodyColumn] = dragFromModelBodyColumn;
            }
        }
        int[] modelBodyColumnOrder = support.getModelBodyColumnOrder();
        assertEquals(expected.length, modelBodyColumnOrder.length, 0);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], modelBodyColumnOrder[i], 0);
        }
    }
