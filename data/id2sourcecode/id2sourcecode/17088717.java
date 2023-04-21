    private void dbStore(Enumeration eData, String uid, String image) throws Exception {
        if (eData == null) return;
        dbFlag.getBusyFlag();
        try {
            con.setAutoCommit(false);
            int i = 500;
            while (eData.hasMoreElements()) {
                String dString = (String) eData.nextElement();
                long deathDate = StoredObject.findDeath(dString);
                String id = StoredObject.findID(dString);
                psStore.clearParameters();
                Timestamp st = new Timestamp(Calendar.getInstance().getTime().getTime());
                psStore.setTimestamp(1, st);
                psStore.setString(2, uid);
                psStore.setString(3, id);
                psStore.setString(4, image);
                psStore.setTimestamp(5, new Timestamp(deathDate));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeChars(dString);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                psStore.setBinaryStream(6, bais, bais.available());
                int rows = psStore.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            while (e != null) {
                System.err.println("state:" + e.getSQLState() + "\ncode:" + e.getErrorCode() + "\nstring:" + e.toString() + "\nstack:");
                e.printStackTrace();
                e = e.getNextException();
            }
            try {
                con.rollback();
            } catch (Exception ex) {
                con = null;
            }
            dbFlag.freeBusyFlag();
            throw (e);
        } catch (Exception e) {
            dbFlag.freeBusyFlag();
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ex) {
                con = null;
            }
            throw (new Exception(e.toString()));
        }
        try {
            con.setAutoCommit(true);
        } catch (Exception e) {
        }
        dbFlag.freeBusyFlag();
    }
