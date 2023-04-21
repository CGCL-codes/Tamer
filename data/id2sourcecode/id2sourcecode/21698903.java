    void sendAlerts() {
        try {
            EventsForUserDAO euEventsForUserDAO = new EventsForUserDAO();
            for (User p : users) {
                if (!euEventsForUserDAO.isEventExist(ann, p)) {
                    String subject = "Whats up Event on " + ann.getStartDate() + " " + ann.getStartTime() + " in " + ann.getCity();
                    String message = "What : " + ann.getName() + "\n" + "Where: " + ann.getStreet() + "," + ann.getCity() + "," + ann.getState() + "\n" + "When: " + ann.getStartDate() + " - " + ann.getEndDate() + "\n" + "Time : " + ann.getStartTime() + " - " + ann.getEndTime() + "\n";
                    if (ann.getUrl() != null && ann.getUrl() != "" && ann.getUrl() != " ") message = message + "Have a look at :" + ann.getUrl();
                    try {
                        MailClient client = new MailClient();
                        String to = p.getUserName();
                        System.out.println("Sending email to " + to);
                        out1.write("Sending email to " + to + "\n");
                        out1.flush();
                        client.sendMail(to, subject, message);
                        euEventsForUserDAO.insert(ann, p);
                        out1.write("Sent email to " + to + "\n");
                        out1.flush();
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    System.out.println("already send to user " + p.getUserName());
                    try {
                        out1.write("already send to user " + p.getUserName() + "\n");
                        out1.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (DAOException e1) {
            e1.printStackTrace();
        }
    }
