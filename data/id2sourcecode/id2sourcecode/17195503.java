    private IntDomainVar[] buildVarOrder(IntDomainVar[] vars) {
        IntDomainVar[] order = new IntDomainVar[vars.length];
        Queue<Integer> begins = new LinkedList<Integer>();
        Queue<Integer> ends = new LinkedList<Integer>();
        order[0] = vars[0];
        begins.add(1);
        int e = 8;
        ends.add(e);
        int index = 1;
        while (begins.size() > 0) {
            int begin = begins.remove();
            int end = ends.remove();
            int middle = (begin + end) / 2;
            System.out.print(middle + "-" + begin + ", ");
            order[index++] = vars[middle];
            if (middle - begin > 1) {
                begins.add(begin);
                ends.add(middle);
            }
            if (end - middle > 1) {
                begins.add(middle);
                ends.add(end);
            }
        }
        System.out.print(e + "-" + (e / 2) + ", ");
        order[index] = vars[vars.length - 1];
        return order;
    }
