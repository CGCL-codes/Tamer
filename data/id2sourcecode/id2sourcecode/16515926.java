    public PatientDocument updatePatient(String patientDocument) {
        ResultSet rsnok = null;
        PatientDocument doc = null;
        PreparedStatement psRollback = null;
        ResultSet rsWard = null;
        boolean ward = false;
        try {
            helper = new DBHelper();
            doc = PatientDocument.Factory.parse(patientDocument);
            PreparedStatement psPatient = helper.prepareStatement(SQL.updatePatient());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psDeleteNOK = helper.prepareStatement(SQL.deleteNextofkin());
            PreparedStatement psAddNOK = helper.prepareStatement(SQL.insertNextofkin());
            PreparedStatement psWardUpdate = helper.prepareStatement(SQL.updatePatientWardDateLeftInformation());
            PreparedStatement psWardInsert = helper.prepareStatement(SQL.insertPatientWard());
            PreparedStatement psWardInfo = helper.prepareStatement(SQL.getPatientInWardInformation());
            psRollback = helper.prepareStatement(SQL.rollback());
            psPatient.setString(1, doc.getPatient().getName());
            psPatient.setString(2, doc.getPatient().getSurname());
            psPatient.setString(3, doc.getPatient().getStreet());
            psPatient.setString(4, doc.getPatient().getSuburb());
            psPatient.setString(5, doc.getPatient().getPostcode());
            psPatient.setString(6, doc.getPatient().getId());
            psPatient.setString(7, doc.getPatient().getIllnessno());
            psPatient.setString(8, doc.getPatient().getPatientno());
            psWardInfo.setString(1, doc.getPatient().getPatientno());
            rsWard = psWardInfo.executeQuery();
            while (rsWard.next()) {
                if (rsWard.getLong("DATELEFT") == 0) {
                    String wardno = rsWard.getString("WARDNO");
                    PatientInWardType[] piw = doc.getPatient().getWardHistory().getPatientInWardArray();
                    String xmlWardNo = "";
                    String xmlOldPwno = "";
                    String xmlPwno = "";
                    long dateAdmitted = 0;
                    for (int i = 0; i < piw.length; i++) {
                        if (piw[i].getDateLeft() == 0) {
                            PatientInWardType first = piw[i];
                            if (i + 1 < piw.length) {
                                for (int j = i + 1; j < piw.length; j++) {
                                    if (piw[i].getDateLeft() == 0) {
                                        PatientInWardType sec = piw[j];
                                        if (first.getDateAdmitted() < sec.getDateAdmitted()) {
                                            xmlWardNo = sec.getWardno();
                                            xmlOldPwno = first.getPwno();
                                            xmlPwno = sec.getPwno();
                                            dateAdmitted = sec.getDateAdmitted();
                                            ward = true;
                                            break;
                                        } else {
                                            xmlWardNo = first.getWardno();
                                            xmlOldPwno = sec.getPwno();
                                            xmlPwno = first.getPwno();
                                            dateAdmitted = first.getDateAdmitted();
                                            ward = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!wardno.equals(xmlWardNo)) {
                        psWardUpdate.setLong(1, System.currentTimeMillis());
                        psWardUpdate.setString(2, xmlOldPwno);
                        psWardInsert.setString(1, xmlPwno);
                        psWardInsert.setString(2, doc.getPatient().getPatientno());
                        psWardInsert.setString(3, xmlWardNo);
                        psWardInsert.setLong(4, dateAdmitted);
                        psWardInsert.setLong(5, 0);
                    } else {
                        ward = false;
                    }
                }
            }
            psDeleteNOK.setString(1, doc.getPatient().getPatientno());
            for (int i = 0; i < doc.getPatient().getNextOfKinList().getNextOfKinArray().length; i++) {
                NextOfKinType n = doc.getPatient().getNextOfKinList().getNextOfKinArray(i);
                n.setNextOfKinNo(MedisisKeyGenerator.generate());
                psAddNOK.setString(1, n.getNextOfKinNo());
                psAddNOK.setString(2, doc.getPatient().getPatientno());
                psAddNOK.setString(3, n.getTitle());
                psAddNOK.setString(4, n.getName());
                psAddNOK.setString(5, n.getSurname());
                psAddNOK.setString(6, n.getTelephone());
                psAddNOK.setString(7, n.getCell());
                psAddNOK.addBatch();
            }
            psBegin.executeUpdate();
            psPatient.executeUpdate();
            psDeleteNOK.executeUpdate();
            psAddNOK.executeBatch();
            if (ward) {
                psWardUpdate.executeUpdate();
                psWardInsert.executeUpdate();
            }
            psCommit.executeUpdate();
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
                if (rsnok != null) {
                    rsnok.close();
                }
                if (rsWard != null) {
                    rsWard.close();
                }
                if (helper != null) {
                    helper.cleanup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doc;
    }
