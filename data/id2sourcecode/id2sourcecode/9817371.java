    HGSortedSet<E> write() {
        List<LogEntry> log = txManager.getContext().getCurrent().getAttribute(S);
        if (log == null) {
            HGSortedSet<E> readOnly = S.get();
            HGSortedSet<E> writeable = cloneSet(readOnly);
            S.put(writeable);
        }
        return S.get();
    }
