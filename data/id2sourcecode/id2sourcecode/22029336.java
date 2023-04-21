    public boolean saveLeaveQty(String arrayValues) {
        boolean flag = false;
        String empId = "";
        String lvType = "";
        String[] splitData;
        splitData = arrayValues.split(",");
        System.out.println("arrays values" + arrayValues);
        try {
            for (int i = 0; i < splitData.length; i++) {
                String[] finalValue;
                finalValue = splitData[i].split("/");
                String query = "from LeaveQuota a where a.employeeCode='" + finalValue[0].trim() + "' and a.leaveTyp='" + finalValue[1].trim() + "' ";
                transaction = session.beginTransaction();
                List<LeaveQuota> checkList = session.createQuery(query).list();
                for (Iterator<LeaveQuota> itr = checkList.iterator(); itr.hasNext(); ) {
                    LeaveQuota chekLQ = itr.next();
                    empId = chekLQ.getEmployeeCode();
                    lvType = chekLQ.getLeaveTyp();
                }
                System.out.println(empId + " yes here =" + lvType);
                transaction.commit();
                if (empId.equals(finalValue[0].trim()) && lvType.equals(finalValue[1].trim())) {
                    transaction = session.beginTransaction();
                    String updateQry = " update LeaveQuota set leaveQty='" + finalValue[2].trim() + "' where employeeCode='" + finalValue[0].trim() + "' and leaveTyp='" + finalValue[1].trim() + "' ";
                    org.hibernate.Query qry = session.createQuery(updateQry);
                    int rowcount = qry.executeUpdate();
                    transaction.commit();
                } else {
                    transaction = session.beginTransaction();
                    LeaveQuota lqObj = new LeaveQuota();
                    int qty = Integer.parseInt(finalValue[2].trim());
                    lqObj.setEmployeeCode(finalValue[0].trim());
                    lqObj.setLeaveTyp(finalValue[1].trim());
                    lqObj.setLeaveQty(qty);
                    session.save(lqObj);
                    transaction.commit();
                }
            }
            session.close();
            flag = true;
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return flag;
    }
