    void dumpAWTThread(PrintWriter writer) {
        writer.println("time:" + (snapshot.getTimeTaken() - snapshot.getBeginTime()));
        writer.println("Threads:");
        dumpCCTNode(writer, 0, getAWTRoot());
    }
