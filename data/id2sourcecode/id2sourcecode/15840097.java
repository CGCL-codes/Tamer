    private void save() {
        ArrayList<UserRight> al = new ArrayList<UserRight>();
        for (UserRight data2 : data) {
            String userName = data2.getName();
            boolean read = getReadRight(userName);
            boolean write = getWriteRight(userName);
            boolean admin = getAdminRight(userName);
            System.out.println(userName + " admin: " + admin);
            Right right;
            if ((read && write) || (!read && write)) {
                right = Right.WRITE;
            } else if (read && !write) {
                right = Right.READ;
            } else {
                right = Right.NORIGHTS;
            }
            al.add(new UserRight(userName, right, admin));
        }
        try {
            if (mainFrame.getController().isAdmin()) mainFrame.getController().saveUserRights(al); else mainFrame.getController().saveUserRightsProject(al);
            mainFrame.displayConfirmation(Messages.getString("CHANGES_WERE_SAVED"));
        } catch (StatementNotExecutedException ex) {
            log.error(ex);
        } catch (NoRightException ex) {
            log.error(ex);
            mainFrame.displayError(Messages.getString("NO_RIGHT_ERROR_OCCURED"));
        } catch (NotLoadedException ex) {
            log.error(ex);
            mainFrame.displayError(Messages.getString("NO_USER_LOGGED_IN"));
            mainFrame.reloadGui(Content.Id.LOGIN);
        } catch (NotConnectedException ex) {
            log.error(ex);
            mainFrame.displayError("NotConnectedException");
        }
    }
