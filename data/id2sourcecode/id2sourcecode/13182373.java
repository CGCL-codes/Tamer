    public synchronized void vouch(String fromWho, int fromExperience, String aboutWho) throws MaxVouchesException, ExperienceTooLowException, AlreadyVouchedException, UserDBException, SQLException {
        if (fromWho.equals(aboutWho)) throw new UserDBException("can not vouch for yourself");
        con.setAutoCommit(false);
        try {
            updateUser(aboutWho);
            updateInputProvider(fromWho, fromExperience);
        } catch (Exception e) {
            con.rollback();
            con.setAutoCommit(true);
            throw new UserDBException(e.getMessage());
        }
        if (currentInputProvider.getNVouches() >= maxVouches) {
            throw new MaxVouchesException(fromWho + " over max vouches");
        }
        if (currentInputProvider.isVoucher(currentUser)) {
            con.rollback();
            con.setAutoCommit(true);
            updateExperiencePS.setInt(1, fromExperience);
            updateExperiencePS.setString(2, fromWho);
            if (updateExperiencePS.executeUpdate() != 1) {
                throw new UserDBException("updateExperience had no effect");
            }
            con.setAutoCommit(false);
            throw new AlreadyVouchedException(fromWho + " already vouched for " + aboutWho);
        }
        if (currentInputProvider.isComplainer(currentUser)) {
            this.removeComplainer(fromWho);
        }
        if (null == this.addVoucher(fromWho)) {
            throw new ExperienceTooLowException(fromWho + " does not have more experience than current vouchers");
        } else {
            this.updateReputation();
        }
        con.commit();
        con.setAutoCommit(true);
    }
