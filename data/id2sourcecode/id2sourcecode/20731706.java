    @Override
    HGSortedSet<E> write() {
        List<LogEntry> log = txManager.getContext().getCurrent().getAttribute(S);
        if (log == null) {
            HGSortedSet<E> readOnly = read();
            HGSortedSet<E> writeable = cloneSet(readOnly);
            S.put(writeable);
            writeMap.put(key, S);
        }
        return S.get();
    }
