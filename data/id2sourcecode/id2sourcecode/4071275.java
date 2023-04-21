    private void renderFilterThreadsPage(Hashtable<String, String> parameters, HtmlWriter writer) {
        writer.startPanel("Active PreCheckers &amp; Filters");
        Vector<TaskThread> threads = spamato.filterThreadPool.getActiveThreads();
        synchronized (threads) {
            int activePreCheckers = 0;
            int activeFilters = 0;
            if (threads.size() > 0) {
                Hashtable<Mail, Vector<String>> mails = new Hashtable<Mail, Vector<String>>();
                for (TaskThread thread : threads) {
                    String name;
                    Mail mail;
                    Task mainTask = thread.getTask();
                    if (mainTask != null) {
                        if (mainTask instanceof PreCheckerTask) {
                            PreCheckerTask task = (PreCheckerTask) mainTask;
                            mail = task.getFilterProcessResult().getMail();
                            name = task.getPreChecker().getPreCheckerName() + " (PreChecker)";
                            activePreCheckers++;
                        } else {
                            FilterTask task = (FilterTask) mainTask;
                            mail = task.getFilterProcessResult().getMail();
                            name = task.getFilter().getName();
                            activeFilters++;
                        }
                        Vector<String> names = mails.get(mail);
                        if (names == null) {
                            names = new Vector<String>();
                            mails.put(mail, names);
                        }
                        names.add(name);
                    }
                }
                writer.writeLnIndent("<p>");
                writer.writeLnIndent(String.format("Currently, %1d PreCheckers and %2d Filters are checking the following %3d emails:", activePreCheckers, activeFilters, mails.size()));
                writer.writeLnIndent("</p>");
                boolean rowType = true;
                writer.writeLnIndent("<table class='list' cellspacing='0'>");
                writer.writeLnIndent("<tr><th class='list' align='left'><b>Subject</b></th><th class='list' align='left'>&nbsp;</th><th class='list' align='left'><b>PreChecker / Filter</b></th></tr>");
                for (Mail mail : mails.keySet()) {
                    Vector<String> names = mails.get(mail);
                    writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
                    rowType = !rowType;
                    writer.writeLnIndent(String.format("<td valign='top'>%1s</td><td>&nbsp;</td>", mail.getSubject()));
                    writer.writeLnIndent("<td valign='top'>");
                    for (String name : names) {
                        writer.writeLnIndent(name);
                        writer.addBr();
                    }
                    writer.writeLnIndent("</td></tr>");
                }
                writer.writeLnIndent("</table>");
            } else {
                writer.writeLnIndent("No emails are being checked.");
            }
        }
        writer.endPanel();
        writer.startPanel("Buffered PreCheckers &amp; Filters");
        Vector<Task> tasks = spamato.filterThreadPool.getBufferedTasks();
        synchronized (tasks) {
            int bufferedPreCheckers = 0;
            int bufferedFilters = 0;
            if (tasks.size() > 0) {
                Hashtable<Mail, Vector<String>> mails = new Hashtable<Mail, Vector<String>>();
                for (Task task : tasks) {
                    String name;
                    Mail mail;
                    if (task instanceof PreCheckerTask) {
                        PreCheckerTask pcTask = (PreCheckerTask) task;
                        mail = pcTask.getFilterProcessResult().getMail();
                        name = pcTask.getPreChecker().getPreCheckerName() + " (PreChecker)";
                        bufferedPreCheckers++;
                    } else {
                        FilterTask fTask = (FilterTask) task;
                        mail = fTask.getFilterProcessResult().getMail();
                        name = fTask.getFilter().getName();
                        bufferedFilters++;
                    }
                    Vector<String> names = mails.get(mail);
                    if (names == null) {
                        names = new Vector<String>();
                        mails.put(mail, names);
                    }
                    names.add(name);
                }
                writer.writeLnIndent("<p>");
                writer.writeLnIndent(String.format("Currently, %1d PreCheckers and %2d Filters are buffered to check the following %3d emails:", bufferedPreCheckers, bufferedFilters, mails.size()));
                writer.writeLnIndent("</p>");
                boolean rowType = true;
                writer.writeLnIndent("<table class='list' cellspacing='0'>");
                writer.writeLnIndent("<tr><th class='list' align='left'><b>Subject</b></th><th class='list' align='left'>&nbsp;</th><th class='list' align='left'><b>PreChecker / Filter</b></th></tr>");
                for (Mail mail : mails.keySet()) {
                    Vector<String> names = mails.get(mail);
                    writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
                    rowType = !rowType;
                    writer.writeLnIndent(String.format("<td valign='top'>%1s</td><td>&nbsp;</td>", mail.getSubject()));
                    writer.writeLnIndent("<td valign='top'>");
                    for (String name : names) {
                        writer.writeLnIndent(name);
                        writer.addBr();
                    }
                    writer.writeLnIndent("</td></tr>");
                }
                writer.writeLnIndent("</table>");
            } else {
                writer.writeLnIndent("No emails are buffered.");
            }
        }
        writer.endPanel();
    }
