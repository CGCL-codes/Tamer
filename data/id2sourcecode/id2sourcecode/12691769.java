    @SuppressWarnings("unchecked")
    public void addDeliveryDate(Long nExerciseId, String sDates) {
        try {
            CoExercises1 coExercises1 = new CoExercises1DAO().findById(nExerciseId);
            if (coExercises1 == null) {
                log.info("Ejercicio no existe: ");
            } else {
                EntityManagerHelper.beginTransaction();
                Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_EXERCISE1_DELIVERYDATE1);
                query.setParameter(1, new Long(nExerciseId));
                query.executeUpdate();
                StringTokenizer tokenizer = new StringTokenizer(sDates, ",");
                while (tokenizer.hasMoreTokens()) {
                    CoDeliveryDate1DAO coDeliveryDate1DAO = new CoDeliveryDate1DAO();
                    try {
                        String sKeyValue = tokenizer.nextToken();
                        String[] data = sKeyValue.split("\\|");
                        String sDeliveryDateNum = data[0];
                        Date deliveryDate = Date.valueOf(data[1]);
                        CoDeliveryDate1 coDeliveryDate1 = new CoDeliveryDate1();
                        coDeliveryDate1.setDeliveryDateId(getSequence("sq_co_delivery_date1"));
                        coDeliveryDate1.setDeliveryDateNum(new Long(sDeliveryDateNum));
                        coDeliveryDate1.setDeliveryDate(deliveryDate);
                        coDeliveryDate1.setCoExercises1(new CoExercises1DAO().findById(nExerciseId));
                        coDeliveryDate1DAO.save(coDeliveryDate1);
                        log.info("Fecha Entrega " + coDeliveryDate1.getDeliveryDate() + " creada con �xito...");
                    } catch (Exception e) {
                        EntityManagerHelper.rollback();
                    }
                }
                EntityManagerHelper.commit();
            }
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            e.printStackTrace();
        }
    }
