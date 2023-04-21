    @SuppressWarnings("unchecked")
    public void addDeliveryDate(Long nExerciseId, String sDates) {
        try {
            CoExercises2 coExercises2 = new CoExercises2DAO().findById(nExerciseId);
            if (coExercises2 == null) {
                log.info("Ejercicio no existe: ");
            } else {
                EntityManagerHelper.beginTransaction();
                Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_EXERCISE2_DELIVERYDATE2);
                query.setParameter(1, new Long(nExerciseId));
                query.executeUpdate();
                StringTokenizer tokenizer = new StringTokenizer(sDates, ",");
                while (tokenizer.hasMoreTokens()) {
                    CoDeliveryDate2DAO coDeliveryDate2DAO = new CoDeliveryDate2DAO();
                    try {
                        String sKeyValue = tokenizer.nextToken();
                        String[] data = sKeyValue.split("\\|");
                        String sDeliveryDateNum = data[0];
                        Date deliveryDate = Date.valueOf(data[1]);
                        CoDeliveryDate2 coDeliveryDate2 = new CoDeliveryDate2();
                        coDeliveryDate2.setDeliveryDateId(getSequence("sq_co_delivery_date2"));
                        coDeliveryDate2.setDeliveryDateNum(new Long(sDeliveryDateNum));
                        coDeliveryDate2.setDeliveryDate(deliveryDate);
                        coDeliveryDate2.setCoExercises2(new CoExercises2DAO().findById(nExerciseId));
                        coDeliveryDate2DAO.save(coDeliveryDate2);
                        log.info("Fecha Entrega " + coDeliveryDate2.getDeliveryDate() + " creada con �xito...");
                    } catch (Exception e) {
                        EntityManagerHelper.rollback();
                    }
                }
                EntityManagerHelper.commit();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }
