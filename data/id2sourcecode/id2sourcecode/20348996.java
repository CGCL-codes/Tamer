    public void executeUpdate() {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            String key = (String) parameters.get(DataConnector.RECORD_KEY_PARAMETER);
            List updated = (List) parameters.get(DataConnector.RECORD_PARAMETER);
            List existing = session.find("from UserGroupMember as member where member.user.idString = '" + key + "'");
            if (updated != null) {
                Iterator i = updated.iterator();
                while (i.hasNext()) {
                    UserGroupMember thisMember = (UserGroupMember) i.next();
                    UserGroupMember matchingMember = null;
                    if (existing != null) {
                        Iterator j = existing.iterator();
                        while (j.hasNext()) {
                            UserGroupMember existingMember = (UserGroupMember) j.next();
                            if (existingMember.getUserGroupId().equalsIgnoreCase(thisMember.getUserGroupId())) {
                                matchingMember = existingMember;
                            }
                        }
                    }
                    if (matchingMember == null) {
                        session.save(thisMember);
                    } else {
                        existing.remove(matchingMember);
                    }
                }
            }
            if (existing != null) {
                Iterator i = existing.iterator();
                while (i.hasNext()) {
                    UserGroupMember existingMember = (UserGroupMember) i.next();
                    session.delete(existingMember);
                }
            }
            tx.commit();
            responseCode = 0;
            responseString = "Execution complete";
        } catch (Throwable t) {
            responseCode = 10;
            responseString = t.toString();
            t.printStackTrace();
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        }
    }
