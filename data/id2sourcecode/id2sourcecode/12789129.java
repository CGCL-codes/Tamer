    public static RootedTree parseNewickString(RootedTree tree, String s) {
        if (isNeXML(s)) {
            NexmlIO io = new NexmlIO(tree.getClass());
            try {
                return io.parseString(s);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        boolean oldEnforceUniqueLabels = tree.getEnforceUniqueLabels();
        tree.setEnforceUniqueLabels(false);
        if (tree instanceof PhyloTree) {
            PhyloTree pt = (PhyloTree) tree;
        }
        if (DEBUG) System.out.println(System.currentTimeMillis() + "\tStarting parse...");
        DefaultVertex root = null;
        int endInd = Math.min(10, s.length() - 1);
        String test = s.substring(0, endInd).toLowerCase();
        if (test.startsWith("http://") || test.startsWith("ftp://") || test.startsWith("file://")) {
            try {
                URL url = new URL(s);
                BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
                return TreeIO.parseReader(tree, r);
            } catch (SecurityException e) {
                e.printStackTrace();
                PWPlatform.getInstance().getThisAppContext().getPW().setMessage("Error: to load a tree from a URL, please use PhyloWidget Full!");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean nhx = (s.indexOf("NHX") != -1);
        boolean poorMans = (s.indexOf(POOR_MANS_NHX) != -1);
        if (s.startsWith("'(")) s = s.substring(1);
        if (s.endsWith("'")) s = s.substring(0, s.length() - 1);
        if (s.indexOf(';') == -1) s = s + ';';
        NHXHandler nhxHandler = new NHXHandler();
        boolean stripAnnotations = false;
        if (stripAnnotations) s = nhxHandler.stripAnnotations(s);
        int[] countForDepth = new int[50];
        HashMap<DefaultVertex, DefaultVertex> firstChildren = new HashMap<DefaultVertex, DefaultVertex>();
        int curDepth = 0;
        Stack<DefaultVertex> vertices = new Stack<DefaultVertex>();
        Stack<Double> lengths = new Stack<Double>();
        boolean parsingNumber = false;
        boolean innerNode = false;
        boolean withinEscapedString = false;
        boolean withinNHX = false;
        String controlChars = "();,";
        StringBuffer temp = new StringBuffer(10000);
        String curLabel = new String();
        if (DEBUG) System.out.println(System.currentTimeMillis() + "\tChar loop...");
        long len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            boolean isControl = (c == '(' || c == ')' || c == ';' || c == ',');
            if (DEBUG) {
                if (i % (len / 50 + 1) == 0) {
                    System.out.print(".");
                }
            }
            if (c == '[' && !withinNHX) withinNHX = true; else if (withinNHX && c == ']') withinNHX = false;
            if (withinNHX) isControl = false;
            if (withinEscapedString) {
                temp.append(c);
                if (c == '\'') withinEscapedString = false;
                continue;
            } else if (c == '\'' && temp.length() == 0) {
                temp.append(c);
                withinEscapedString = true;
                continue;
            }
            if (isControl) {
                if (c == '(') {
                    curDepth++;
                    if (curDepth >= countForDepth.length) {
                        int[] newArr = new int[countForDepth.length << 2];
                        System.arraycopy(countForDepth, 0, newArr, 0, countForDepth.length);
                        countForDepth = newArr;
                    }
                }
                if (c == ')' || c == ',' || c == ';') {
                    curLabel = temp.toString();
                    curLabel = curLabel.trim();
                    if (stripAnnotations) curLabel = nhxHandler.replaceAnnotation(curLabel);
                    PhyloNode curNode = newNode(tree, curLabel, nhx, poorMans);
                    if (c == ';') {
                        root = curNode;
                    }
                    if (innerNode) {
                        DefaultVertex child = null;
                        for (int j = 0; j < countForDepth[curDepth]; j++) {
                            child = vertices.pop();
                            double length = lengths.pop();
                            Object o = null;
                            if (!tree.containsEdge(curNode, child)) o = tree.addEdge(curNode, child);
                            o = tree.getEdge(curNode, child);
                            tree.setEdgeWeight(o, length);
                        }
                        countForDepth[curDepth] = 0;
                        curDepth--;
                        firstChildren.put(curNode, child);
                    }
                    vertices.push(curNode);
                    lengths.push(curNode.getBranchLengthCache());
                    countForDepth[curDepth]++;
                    temp.replace(0, temp.length(), "");
                    parsingNumber = false;
                    innerNode = false;
                }
                if (c == ')') {
                    innerNode = true;
                }
            } else {
                temp.append(c);
            }
        }
        tree.setRoot(root);
        if (DEBUG) System.out.println(System.currentTimeMillis() + "\nSorting nodes...");
        PhyloTree pt = (PhyloTree) tree;
        BreadthFirstIterator dfi = new BreadthFirstIterator(tree, tree.getRoot());
        while (dfi.hasNext()) {
            DefaultVertex p = (DefaultVertex) dfi.next();
            if (!tree.isLeaf(p)) {
                List l = tree.getChildrenOf(p);
                if (l.get(0) != firstChildren.get(p)) {
                    tree.sorting.put(p, RootedTree.REVERSE);
                }
            }
        }
        oldTree = null;
        ((CachedRootedTree) tree).modPlus();
        if (tree.getNumEnclosedLeaves(tree.getRoot()) > 1000) tree.setEnforceUniqueLabels(false); else tree.setEnforceUniqueLabels(oldEnforceUniqueLabels);
        if (DEBUG) System.out.println(System.currentTimeMillis() + "\nDone loading tree!");
        return tree;
    }
