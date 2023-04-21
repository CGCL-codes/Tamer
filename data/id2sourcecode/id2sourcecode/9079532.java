    public static void main(String[] args) {
        (new PyFile("c:\\Extract-7.txx", "w", 4000)).write((new PyFile("c:\\Extract-7.txt", "r", 4000)).read());
        Collection<Object> a = toCollection(new ArrayList<Object>(), 1, 2, 3, 4, 5, 6, 3, 3, 10);
        System.out.println("Collection<Object> a = toCollection(new ArrayList<Object>(), 1,2,3,4,5,6,3,3,10): " + str(a));
        Iterable<Integer> b = range(2, 10, 3);
        System.out.println("Iterable<Integer> b = irange(2,10,3): " + b);
        Iterable<Object> c = iterable(1, 2, 3, 4);
        System.out.println("Iterable c = iterable(1,2,3): " + c);
        Iterable<Object> d = iterable(new int[] { 1, 2, 3, 4 });
        System.out.println("Iterable d = iterable(new Integer []{1,2,3,4}): " + c);
        System.out.println("testing ienum(b): " + enumerate(c));
        Iterator forever = repeat(4).iterator();
        System.out.println("Iterable forever = repeat(4).iterator()");
        System.out.println("testing forever.next(), forever.next(): " + forever.next() + "," + forever.next());
        System.out.println("testing repeat(3, iterable(1,2,3)): " + repeat(3, iterable(1, 2, 3)));
        System.out.println("testing repeat(3, iterable(iterable(1,2,3))): " + repeat(3, iterable(iterable(1, 2, 3))));
        System.out.println("testing map(lambda x: x*10, b): " + map(new Lambda() {

            public Object process(Object... args) {
                return ((Integer) args[0]) * 10;
            }
        }, b));
        System.out.println("testing map(lambda x,y: x*10+y, a, b): " + map(new Lambda() {

            public Object process(Object... args) {
                return ((Integer) args[0]) * 10 + ((Integer) args[1]);
            }
        }, a, b));
        System.out.println("testing filter(lambda x: x%2==0, a): " + filter(new Lambda() {

            public Object process(Object... args) {
                return ((Integer) args[0]) % 2 == 0 ? args[0] : null;
            }
        }, a));
        System.out.println("testing reduce(lambda x,y: x+y, a,0): " + reduce(new Lambda() {

            public Object process(Object... args) {
                int x = (Integer) args[0];
                int y = (Integer) args[1];
                return x + y;
            }
        }, iterable(1, 2, 3, 4, 5), 0));
        System.out.println("testing slice(" + str(a) + "\n\t, 1,10,3): " + slice(a, 1, 10, 3));
        System.out.println("testing chain(a,b): " + chain(a, b));
        System.out.println("testing chain(a,b,a): " + chain(a, b, a));
        System.out.println("testing iterate(10, count(20)): " + iterate(10, count(20)));
        System.out.println("testing iterate(10, cycle(b)): " + iterate(10, cycle(b)));
        System.out.println("testing all(lambda x: x==2, " + b + ": " + all(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] == 2;
            }
        }, b));
        System.out.println("testing any(lambda x: x==2, " + b + ": " + any(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] == 2;
            }
        }, b));
        System.out.println("testing any(lambda x: x==2, repeat(10, iterable(1)): " + any(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] == 2;
            }
        }, repeat(10, iterable(1))));
        System.out.println("testing all(lambda x: x==1, repeat(10, iterable(1)): " + all(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] == 2;
            }
        }, repeat(10, iterable(1))));
        System.out.println("testing zip(a,b,c): " + zip(a, b, c));
        System.out.println("testing takewhile(lambda x: x< 4, a): " + takewhile(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] < 4 ? args[0] : null;
            }
        }, a));
        System.out.println("testing dropwhile(lambda x: x< 4, a): " + dropwhile(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] < 4;
            }
        }, a));
        System.out.println("example enumerate = zip(count().iterator(), a): " + zip(count(0), a));
        System.out.println("example dot product of (1,2,3,4) * (4,5,6,7): " + reduce(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] + (Integer) args[1];
            }
        }, map(new Lambda() {

            public Object process(Object... args) {
                return (Integer) args[0] * (Integer) args[1];
            }
        }, iterable(1, 2, 3, 4), iterable(4, 5, 6, 7)), 0));
    }
