    private static void dumpThreads(PrintWriter writer) {
        writer.println("+------------------------------");
        writer.print("| Thread depth limit: ");
        if (Controller._compactThreadDepth) {
            writer.println("Compact");
        } else if (Controller._threadDepth == Controller.UNLIMITED) {
            writer.println("Unlimited");
        } else {
            writer.println(Controller._threadDepth);
        }
        writer.println("+------------------------------");
        for (Long threadId : Profile.threads()) {
            int i = 1;
            for (Frame iteractionRoot : Profile.interactions(threadId)) {
                FrameDump.dump(writer, iteractionRoot, i);
                i++;
            }
        }
    }
