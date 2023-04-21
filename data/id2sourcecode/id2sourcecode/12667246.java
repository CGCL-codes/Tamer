    private static void outputCallGraph(PrintWriter writer) throws IOException {
        for (Long threadId : Profile.threads()) {
            startElement(writer, "thread", 1);
            attribute(writer, "id", threadId);
            cap(writer);
            int i = 1;
            for (Frame f : Profile.interactions(threadId)) {
                startElement(writer, "interaction", 2);
                attribute(writer, "id", i++);
                cap(writer);
                outputFrame(writer, f, 3);
                endElement(writer, "interaction", 2);
            }
            endElement(writer, "thread", 1);
        }
    }
