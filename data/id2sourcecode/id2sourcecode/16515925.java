    public PatientDocument addPatient(String patient) {
        String key = null;
        PreparedStatement psPatient = null;
        PreparedStatement psNextofkin = null;
        PreparedStatement psCommit = null;
        PreparedStatement psRollback = null;
        PreparedStatement psAdmitance = null;
        PreparedStatement psWard = null;
        PreparedStatement psHospital = null;
        ResultSet rsHospital = null;
        PatientDocument doc = null;
        try {
            doc = PatientDocument.Factory.parse(patient);
            helper = new DBHelper();
            psPatient = helper.prepareStatement(SQL.insertPatient());
            psNextofkin = helper.prepareStatement(SQL.insertNextofkin());
            psCommit = helper.prepareStatement(SQL.commit());
            psRollback = helper.prepareStatement(SQL.rollback());
            psAdmitance = helper.prepareStatement(SQL.insertAdmitance());
            psWard = helper.prepareStatement(SQL.insertPatientWard());
            psHospital = helper.prepareStatement(SQL.getHospitalFromWard());
            key = MedisisKeyGenerator.generate();
            doc.getPatient().setPatientno(key);
            psPatient.setString(1, key);
            psPatient.setString(2, doc.getPatient().getId());
            psPatient.setString(3, doc.getPatient().getIllnessno());
            psPatient.setString(4, doc.getPatient().getTitle());
            psPatient.setString(5, doc.getPatient().getName());
            psPatient.setString(6, doc.getPatient().getSurname());
            psPatient.setString(7, doc.getPatient().getStreet());
            psPatient.setString(8, doc.getPatient().getSuburb());
            psPatient.setString(9, doc.getPatient().getPostcode());
            psPatient.addBatch();
            String wardno = doc.getPatient().getWardHistory().getPatientInWardArray(0).getWardno();
            psHospital.setString(1, wardno);
            rsHospital = psHospital.executeQuery();
            rsHospital.next();
            String hospitalno = rsHospital.getString("HOSPITALNO");
            psAdmitance.setString(1, MedisisKeyGenerator.generate());
            psAdmitance.setString(2, hospitalno);
            psAdmitance.setString(3, doc.getPatient().getPatientno());
            psAdmitance.setLong(4, System.currentTimeMillis());
            psAdmitance.addBatch();
            psWard.setString(1, doc.getPatient().getWardHistory().getPatientInWardArray(0).getPwno());
            psWard.setString(2, doc.getPatient().getPatientno());
            psWard.setString(3, doc.getPatient().getWardHistory().getPatientInWardArray(0).getWardno());
            psWard.setLong(4, System.currentTimeMillis());
            psWard.setLong(5, 0);
            psWard.addBatch();
            NextOfKinType[] nok = doc.getPatient().getNextOfKinList().getNextOfKinArray();
            for (int i = 0; i < nok.length; i++) {
                NextOfKinType n = nok[i];
                n.setNextOfKinNo(MedisisKeyGenerator.generate());
                psNextofkin.setString(1, n.getNextOfKinNo());
                psNextofkin.setString(2, key);
                psNextofkin.setString(3, n.getTitle());
                psNextofkin.setString(4, n.getName());
                psNextofkin.setString(5, n.getSurname());
                psNextofkin.setString(6, n.getTelephone());
                psNextofkin.setString(7, n.getCell());
                psNextofkin.addBatch();
            }
            psPatient.executeBatch();
            psAdmitance.executeBatch();
            psWard.executeBatch();
            psNextofkin.executeBatch();
            psCommit.executeUpdate();
        } catch (Exception e) {
            patient = null;
            try {
                psRollback.executeUpdate();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } finally {
            try {
                if (rsHospital != null) {
                    rsHospital.close();
                }
                if (helper != null) {
                    helper.cleanup();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return doc;
    }
