    public boolean dischargePatient(String patientDocument) {
        PreparedStatement psRollback = null;
        boolean ret = false;
        try {
            helper = new DBHelper();
            PatientDocument doc = PatientDocument.Factory.parse(patientDocument);
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psAdmitance = helper.prepareStatement(SQL.updateAdmitanceRecord());
            PreparedStatement psWard = helper.prepareStatement(SQL.updatePatientWardDateLeftInformation());
            psRollback = helper.prepareStatement(SQL.rollback());
            PatientInWardType pp = doc.getPatient().getWardHistory().getPatientInWardArray(0);
            AdmitanceType aa = doc.getPatient().getAdmitanceRecord().getAdmitanceArray(0);
            psWard.setLong(1, System.currentTimeMillis());
            psWard.setString(2, pp.getPwno());
            psAdmitance.setLong(1, System.currentTimeMillis());
            psAdmitance.setString(2, aa.getAdmitanceNo());
            psBegin.executeUpdate();
            psAdmitance.executeUpdate();
            psWard.executeUpdate();
            psCommit.executeUpdate();
            ret = true;
        } catch (Exception e) {
            try {
                if (psRollback != null) {
                    psRollback.executeUpdate();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (helper != null) {
                    helper.cleanup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
