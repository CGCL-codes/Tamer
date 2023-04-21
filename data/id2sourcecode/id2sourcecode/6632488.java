                @Override
                public void userList(UserListEvent ule) {
                    System.out.println("New user list for:" + ule.getChannel() + " contains " + ule.getUserList().size() + " users");
                }
