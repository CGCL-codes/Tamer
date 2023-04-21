    public void addWarning(String warning) {
        if (warnings.contains(warning) == false) {
            warnings.add(warning);
            DataStore store = DataStore.getInstance();
            String sendOnWarning = store.getProperty("email.send.onwarning");
            if ("1".equals(sendOnWarning)) {
                EmailSender sender = new EmailSender();
                sender.setSubject("TV Scheduler Pro Capture Warning");
                String body = "There was a warning when starting or running the following schedule:\n\n";
                SimpleDateFormat df = new SimpleDateFormat("EE MMM d hh:mm a");
                body += "Name    : " + this.getName() + "\n";
                body += "Channel : " + this.getChannel() + "\n";
                body += "Start   : " + df.format(this.getStart()) + "\n";
                body += "Length  : " + this.getDuration() + " minutes\n";
                body += "\n";
                for (int x = 0; x < warnings.size(); x++) {
                    body += " - " + warnings.get(x) + "\n";
                }
                sender.setBody(body);
                try {
                    Thread mailThread = new Thread(Thread.currentThread().getThreadGroup(), sender, sender.getClass().getName());
                    mailThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
