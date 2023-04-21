            @Override
            public void run() {
                getTUser().removeAll();
                getChannel().getUsers();
                int count = getChannel().getUsers().size();
                lblTotaUsers.setText(String.format("Total users: %s", count));
                addUsersToTable(new LinkedList<User>(getChannel().getOwners()), "Owners", "@");
                addUsersToTable(new LinkedList<User>(getChannel().getOps()), "Ops", "@");
                addUsersToTable(new LinkedList<User>(getChannel().getVoices()), "Voice", "+");
                addUsersToTable(new LinkedList<User>(getChannel().getNormalUsers()), "Normal", "");
            }
