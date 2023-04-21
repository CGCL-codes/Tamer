        PriorityPair(final Type type, final SignalBit high, final SignalBit low, final Map<SignalBit, Set<Calculator>> readers, final Map<SignalBit, Set<Calculator>> writers) {
            this.high = high;
            this.low = low;
            must = type == Type.DISJUNCT_READERS ? readers.get(high) : writers.get(high);
            must_not = type == Type.DISJUNCT_READERS ? readers.get(low) : writers.get(low);
        }
