    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[tg.activeCount()];
        int n = tg.enumerate(threads);
        if (n < threads.length) {
            Thread[] newThreads = new Thread[n];
            System.arraycopy(threads, 0, newThreads, 0, n);
            threads = newThreads;
        }
        out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n");
        out.write("<html>\n");
        out.write("  <head>\n");
        out.write("    <title>Currently Executing Threads</title>\n");
        out.write("  </head>\n");
        out.write("  <body bgcolor='khaki'>\n");
        out.write("    <table border=\"1\">\n");
        out.write("      <tr id=\"th\" align=\"center\" bgcolor=\"#eeeeee\">\n");
        out.write("        <td>Name</td>\n");
        out.write("        <td>Priority</td>\n");
        out.write("        <td>Daemon?</td>\n");
        out.write("        <td>Alive?</td>\n");
        out.write("        <td>Running?</td>\n");
        out.write("      </tr>\n");
        if (1 == 1) {
            {
                final Thread[] $threads = threads;
                final int lim$threads = $threads.length;
                Thread thread;
                for (int iThreads = 0; iThreads < lim$threads; ++iThreads) {
                    thread = $threads[iThreads];
                    out.write("      <tr align=\"center\">");
                    out.write("<td id=\"name\">");
                    out.write(String.valueOf(thread.getName()));
                    out.write("</td>\n");
                    out.write("        <td id=\"priority\">");
                    out.write(String.valueOf(thread.getPriority()));
                    out.write("</td>\n");
                    out.write("        <td>");
                    if (thread.isDaemon()) {
                        out.write("<span id=\"isDaemon\">Y</span>");
                    }
                    out.write(" ");
                    if (!thread.isDaemon()) {
                        out.write("<span id=\"isNotDaemon\">&nbsp;</span>");
                    }
                    out.write(" </td>\n");
                    out.write("        <td>");
                    if (thread.isAlive()) {
                        out.write("<span id=\"isAlive\">Y</span>");
                    }
                    out.write(" ");
                    if (!thread.isAlive()) {
                        out.write("<span id=\"isNotAlive\">&nbsp;</span>");
                    }
                    out.write(" </td>\n");
                    out.write("        <td>");
                    if (thread.isInterrupted()) {
                        out.write("<span id=\"isInterrupted\">&nbsp;</span>");
                    }
                    out.write(" ");
                    if (!thread.isInterrupted()) {
                        out.write("<span id=\"isNotInterrupted\">Y</span>");
                    }
                    out.write(" </td>\n");
                    out.write("      </tr>\n");
                }
            }
        }
        out.write("    </table>\n");
        out.write("  </body>\n");
        out.write("</html>\n");
        out.flush();
        out.close();
    }
