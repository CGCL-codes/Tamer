    protected void reloadUsers() {
        if (smscListener != null) {
            try {
                if (users != null) {
                    users.reload();
                } else {
                    users = new Table(usersFileName);
                }
                System.out.println("Users file reloaded.");
            } catch (FileNotFoundException e) {
                event.write(e, "reading users file " + usersFileName);
            } catch (IOException e) {
                event.write(e, "reading users file " + usersFileName);
            }
        } else {
            System.out.println("You must start listener first.");
        }
    }
