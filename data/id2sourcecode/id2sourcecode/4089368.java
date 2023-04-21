    private synchronized void debug(PrintWriter writer, int level) {
        BindingEntry entry;
        Object value;
        for (int j = level; j-- > 0; ) writer.print("  ");
        if (this instanceof MemoryBinding) writer.println("MemoryBinding: " + getName()); else writer.println("ThreadedBinding: " + getName());
        if (_count == 0) writer.println("Empty"); else {
            for (int i = _hashTable.length; i-- > 0; ) {
                entry = _hashTable[i];
                while (entry != null) {
                    for (int j = level; j-- > 0; ) writer.print("  ");
                    value = entry._value;
                    if (value instanceof MemoryBinding) ((MemoryBinding) value).debug(writer, level + 1); else writer.println("  " + entry._name + " = " + value);
                    entry = entry._next;
                }
            }
        }
    }
