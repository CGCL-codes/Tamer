    public synchronized void withdraw(String fromWho, int fromExperience, String aboutWho) throws UserDBException, SQLException {
        if (fromWho.equals(aboutWho)) throw new UserDBException("can not withdraw from yourself");
        con.setAutoCommit(false);
        try {
            updateUser(aboutWho);
            updateInputProvider(fromWho, fromExperience);
            if (currentInputProvider.isComplainer(currentUser)) {
                System.out.println("***BEFORE: " + currentUser.getComplainers());
                this.removeComplainer(fromWho);
                System.out.println("***AFTER: " + currentUser.getComplainers());
            }
            if (currentInputProvider.isVoucher(currentUser)) {
                System.out.println("trying to remove " + fromWho + " from vouchers list of " + currentUser);
                this.removeVoucher(fromWho);
                System.out.println("\tsuccess");
            }
            if (currentUser.canBeDestroyed()) {
                deleteUser(currentUser.getName());
            } else {
                this.updateReputation();
                System.out.println("\tsuccess on rep recalc");
            }
            System.out.println("getting _nComplaints and _nVouches for " + fromWho);
            queryNComplaintsPS.setString(1, fromWho);
            rs = queryNComplaintsPS.executeQuery();
            rs.next();
            int _nComplaints = rs.getInt("ncomplaints");
            queryNVouchesPS.setString(1, fromWho);
            rs = queryNVouchesPS.executeQuery();
            rs.next();
            int _nVouches = rs.getInt("nvouches");
            System.out.println(fromWho + "_nComplaints=" + _nComplaints + ", _nVouches=" + _nVouches);
            if ((_nComplaints == 0) && (_nVouches == 0)) {
                deleteInputProviderPS.setString(1, fromWho);
                if (deleteInputProviderPS.executeUpdate() != 1) {
                    throw new UserDBException("deleteInputProvider had no effect");
                }
            }
        } catch (Exception e) {
            con.rollback();
            con.setAutoCommit(true);
            throw new UserDBException(e.getMessage());
        }
        con.commit();
        con.setAutoCommit(true);
    }
