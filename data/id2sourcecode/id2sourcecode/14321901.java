    public void testProblem4() {
        NaturalNumberStream naturalNumberStream = new NaturalNumberStream(1, 999);
        BaseListStream2<Integer, Integer, Integer> cartesianProduct = new BaseListStream2<Integer, Integer, Integer>(naturalNumberStream, naturalNumberStream) {

            public Integer invoke(Integer input0, Integer input1, int index) {
                return input0 * input1;
            }
        };
        ListFilterStream<Integer> filter = new BaseListFilterStream<Integer>(cartesianProduct) {

            public boolean evaluate(Integer object) {
                return palindrome(object);
            }
        };
        MaxAccumulator accumulator = new MaxAccumulator(filter);
        System.out.println(accumulator.getValue());
        assertEquals(new Integer(906609), accumulator.getValue());
    }
