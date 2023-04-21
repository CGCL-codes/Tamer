    public void writeOn(PrintStream ps) {
        ps.println("----- serialization statistics -----");
        ps.println("read(#read,#bytesRead,avgPerRead), write(#written,#bytesWritten,avgPerWrite)");
        super.writeOn(ps);
        Iterator itr = classStatistics.values().iterator();
        while (itr.hasNext()) {
            ClassStatistics stats = (ClassStatistics) itr.next();
            stats.writeOn(ps);
        }
        ps.println("------------------------------------");
    }
