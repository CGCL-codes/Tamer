    private void renderProcessThreadsPage(Hashtable<String, String> parameters, HtmlWriter writer) {
        writer.startPanel("Currently Checked Emails");
        Vector<TaskThread> threads = spamato.processThreadPool.getActiveThreads();
        synchronized (threads) {
            if (threads.size() > 0) {
                writer.writeLnIndent("<p>");
                writer.writeLnIndent(String.format("Currently, the following %1d emails are being checked:", threads.size()));
                writer.addBr();
                writer.writeLnIndent("</p>");
                boolean rowType = true;
                writer.writeLnIndent("<table class='list' cellspacing='0'><tr><th class='list' align='left'><b>Subject</b></th></tr>");
                for (TaskThread thread : threads) {
                    FilterProcess process = (FilterProcess) thread.getTask();
                    if (process != null) {
                        writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
                        rowType = !rowType;
                        writer.writeLnIndent("<td valign='top'>");
                        String subject = process.getMail().getSubject();
                        if (subject == null || subject.trim().equals("")) subject = "[no subject found by Spamato]";
                        writer.writeLnIndent(subject);
                        writer.writeLnIndent("</td></tr>");
                    }
                }
                writer.writeLnIndent("</table>");
            } else {
                writer.writeLnIndent("No emails are being checked.");
            }
        }
        writer.endPanel();
        writer.startPanel("Buffered Emails");
        Vector<Task> tasks = spamato.processThreadPool.getBufferedTasks();
        synchronized (tasks) {
            if (tasks.size() > 0) {
                writer.writeLnIndent("<p>");
                writer.writeLnIndent(String.format("Currently, the following %1d emails have been buffered:", tasks.size()));
                writer.writeLnIndent("</p>");
                boolean rowType = true;
                writer.writeLnIndent("<table class='list' cellspacing='0'><tr><th class='list' align='left'><b>Subject</b></th></tr>");
                for (Task task : tasks) {
                    FilterProcess process = (FilterProcess) task;
                    writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
                    rowType = !rowType;
                    writer.writeLnIndent("<td valign='top'>");
                    String subject = process.getMail().getSubject();
                    if (subject == null || subject.trim().equals("")) subject = "[no subject found by Spamato]";
                    writer.writeLnIndent(subject);
                    writer.writeLnIndent("</td></tr>");
                }
                writer.writeLnIndent("</table>");
            } else {
                writer.writeLnIndent("No emails are buffered.");
            }
        }
        writer.endPanel();
    }
