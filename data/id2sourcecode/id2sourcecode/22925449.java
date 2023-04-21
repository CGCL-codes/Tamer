    public Simulator(final Set<Calculator> calculators, final Map<SignalBit, Set<SignalBit>> priority_map, final AnalyzeListener listener) throws AnalyzeException {
        assert calculators != null;
        assert priority_map != null;
        assert checkCalculators(calculators);
        assert checkPriorityMap(priority_map);
        if (listener != null) {
            int priority_pairs_count = 0;
            for (final Set<SignalBit> s : priority_map.values()) priority_pairs_count += s.size();
            listener.initTotals(calculators.size(), priority_pairs_count);
        }
        final State state = new State(listener);
        final Set<SignalBit> bits = new HashSet<SignalBit>();
        for (final Calculator c : calculators) {
            for (final SignalBit b : c.getInputSignalBits()) {
                if (!state.readers.containsKey(b)) state.readers.put(b, new LinkedHashSet<Calculator>());
                state.readers.get(b).add(c);
                bits.add(b);
            }
            for (final SignalBit b : c.getOutputSignalBits()) {
                if (!state.writers.containsKey(b)) state.writers.put(b, new LinkedHashSet<Calculator>());
                state.writers.get(b).add(c);
                bits.add(b);
            }
            if (listener != null) listener.increaseCalculators(1, 1);
        }
        final Set<PriorityPair> priority_pairs = new LinkedHashSet<PriorityPair>();
        for (final Map.Entry<SignalBit, Set<SignalBit>> e : priority_map.entrySet()) {
            for (final SignalBit s : e.getValue()) {
                dr: {
                    if (state.readers.containsKey(e.getKey()) && state.readers.containsKey(s)) for (final Calculator c1 : state.readers.get(e.getKey())) for (final Calculator c2 : state.readers.get(s)) if (c1 == c2) break dr;
                    priority_pairs.add(new PriorityPair(PriorityPair.Type.DISJUNCT_READERS, e.getKey(), s, state.readers, state.writers));
                    if (listener != null) listener.increasePriorityPairs(1, 1);
                    continue;
                }
                if (state.writers.containsKey(e.getKey()) && state.writers.containsKey(s)) for (final Calculator c1 : state.writers.get(e.getKey())) for (final Calculator c2 : state.writers.get(s)) if (c1 == c2) throw new AnalyzeException.NondisjunctReadersWritersException();
                priority_pairs.add(new PriorityPair(PriorityPair.Type.DISJUNCT_WRITERS, e.getKey(), s, state.readers, state.writers));
                if (listener != null) listener.increasePriorityPairs(1, 1);
            }
        }
        final Map<Calculator, Integer> before_map = new HashMap<Calculator, Integer>();
        final Map<Calculator, Integer> after_map = new HashMap<Calculator, Integer>();
        final ArrayList<Set<PriorityPair>> priority_sets = new ArrayList<Set<PriorityPair>>();
        for (final PriorityPair p : priority_pairs) {
            int ins_before = Integer.MAX_VALUE;
            if (p.must_not != null) for (final Calculator c : p.must_not) if (before_map.containsKey(c) && before_map.get(c) < ins_before) ins_before = before_map.get(c);
            int ins_after = -1;
            if (p.must != null) for (final Calculator c : p.must) if (after_map.containsKey(c) && after_map.get(c) > ins_after) ins_after = after_map.get(c);
            if (ins_after > ins_before) throw new AnalyzeException.DirectedPriorityCycleException();
            if (ins_after == ins_before) {
                priority_sets.add(ins_before++, new LinkedHashSet<PriorityPair>());
                outer: for (final Iterator<PriorityPair> i = priority_sets.get(ins_before).iterator(); i.hasNext(); ) {
                    final PriorityPair pp = i.next();
                    if (p.must_not != null && pp.must != null) for (final Calculator c1 : p.must_not) for (final Calculator c2 : pp.must) if (c1 == c2) {
                        if (p.must != null && pp.must_not != null) for (final Calculator c3 : p.must) for (final Calculator c4 : pp.must_not) if (c3 == c4) throw new AnalyzeException.DirectedPriorityCycleException();
                        break outer;
                    }
                    priority_sets.get(ins_after).add(pp);
                    i.remove();
                }
            }
            if (++ins_after == ins_before) {
                priority_sets.add(ins_before, new LinkedHashSet<PriorityPair>());
                for (final Map.Entry<Calculator, Integer> e : before_map.entrySet()) if (e.getValue() >= ins_before) e.setValue(e.getValue() + 1);
                for (final Map.Entry<Calculator, Integer> e : after_map.entrySet()) if (e.getValue() >= ins_before) e.setValue(e.getValue() + 1);
            } else if (ins_after == priority_sets.size()) priority_sets.add(new HashSet<PriorityPair>());
            priority_sets.get(ins_after).add(p);
            if (p.must != null) for (final Calculator c : p.must) before_map.put(c, ins_after);
            if (p.must_not != null) for (final Calculator c : p.must_not) after_map.put(c, ins_after);
            if (listener != null) listener.increasePriorityPairs(2, 1);
        }
        final Set<Calculator> unordered_calculators = new LinkedHashSet<Calculator>(calculators);
        for (int i = 0; i < priority_sets.size(); ++i) {
            final Set<Calculator> priority_calculators = new LinkedHashSet<Calculator>();
            final Queue<Calculator> queue = new ArrayDeque<Calculator>();
            for (final PriorityPair p : priority_sets.get(i)) if (p.must != null) for (final Calculator c : p.must) if (unordered_calculators.remove(c)) {
                priority_calculators.add(c);
                queue.add(c);
                if (listener != null) listener.increaseCalculators(2, 1);
            }
            Calculator h;
            while ((h = queue.poll()) != null) for (final SignalBit sb : h.getInputSignalBits()) {
                if (state.writers.containsKey(sb)) dep: for (final Calculator c : state.writers.get(sb)) if (unordered_calculators.contains(c)) {
                    for (int k = i; k < priority_sets.size(); ++k) for (final PriorityPair pp : priority_sets.get(i)) if (pp.must_not != null) for (final Calculator cal : pp.must_not) if (c == cal) continue dep;
                    unordered_calculators.remove(c);
                    priority_calculators.add(c);
                    queue.add(c);
                    if (listener != null) listener.increaseCalculators(2, 1);
                }
            }
            orderGraph(state, priority_calculators, i);
        }
        if (listener != null) listener.increaseCalculators(2, unordered_calculators.size());
        orderGraph(state, unordered_calculators, priority_sets.size());
        connections = new HashMap<SignalBit, OrderedCalculator[]>();
        for (final Map.Entry<SignalBit, Set<OrderedCalculator>> e : state.connection_sets.entrySet()) {
            final OrderedCalculator[] array = new OrderedCalculator[e.getValue().size()];
            int i = 0;
            for (final OrderedCalculator oc : e.getValue()) array[i++] = oc;
            connections.put(e.getKey(), array);
            e.getKey().addSignalBitListener(this);
        }
        int p = priority_sets.size() + 1;
        priority_tree = VBETree.create(p);
        order_tree = new VBETree[p][];
        this.calculators = new OrderedCalculator[priority_sets.size() + 1][][][];
        dirty_stack = new AtomicReferenceArray<OrderedCalculator>(calculators.size());
        dirty_count = new AtomicInteger(0);
        ex_cnt = new AtomicInteger(0);
        calculate_stack = new AtomicReferenceArray<OrderedCalculator>(calculators.size());
        calculate_count = new AtomicInteger(0);
        for (int i = 0; i < p; ++i) {
            final int g = state.group_count.get(i) + 1;
            priority_tree.insert(i);
            order_tree[i] = new VBETree[g];
            this.calculators[i] = new OrderedCalculator[g][][];
            for (int j = 0; j < g; ++j) {
                final int o = state.order_count.get(new OrderKey(i, j)) + 1;
                order_tree[i][j] = VBETree.create(o);
                this.calculators[i][j] = new OrderedCalculator[o][];
                for (int k = 0; k < o; ++k) {
                    final int n = state.number_count.get(new NumberKey(i, j, k)) + 1;
                    order_tree[i][j].insert(k);
                    this.calculators[i][j][k] = new OrderedCalculator[n];
                    if (listener != null) listener.increaseCalculators(6, n);
                }
            }
        }
        for (final OrderedCalculator c : state.ordered_calculators) this.calculators[c.priority][c.group][c.order][c.number] = c;
        listeners = new SimulationListener[0];
        listener_lock = new AtomicBoolean(false);
        shutdown = false;
        parent = null;
        workers = new Thread[Runtime.getRuntime().availableProcessors()];
        suspended = new AtomicBoolean[workers.length];
        for (int i = 0; i < workers.length; ++i) {
            suspended[i] = new AtomicBoolean(true);
            workers[i] = new Thread(new Worker(i));
            workers[i].setDaemon(true);
            workers[i].start();
        }
    }
