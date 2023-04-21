    private void saveKwantuModelUncommitted(KwantuModel model, boolean overwrite, Session session) {
        try {
            Query query = session.createQuery("from KwantuModel m where m.name = :s");
            query.setString("s", model.getName());
            KwantuModel existingModel = (KwantuModel) query.uniqueResult();
            if (existingModel != null) {
                if (!overwrite) {
                    throw new KwantuFaultException("model exists already and overwrite is 'false'.");
                }
                emptyKwantuModel(existingModel, session);
                KwantuBusinessObjectModel bom = model.getKwantuBusinessObjectModel();
                bom.setOwningKwantuModel(existingModel);
                existingModel.setKwantuBusinessObjectModel(bom);
                for (KwantuPanel p : model.getUiPanels()) {
                    p.setOwningKwantuModel(existingModel);
                    existingModel.getUiPanels().add(p);
                }
                model = existingModel;
            }
            for (KwantuClass c : model.getKwantuBusinessObjectModel().getKwantuClasses()) {
                for (KwantuRelationship rel : c.getDeclaredKwantuRelationships()) {
                    session.save(rel);
                }
                session.save(c);
            }
            session.save(model);
        } catch (HibernateException ex) {
            session.getTransaction().rollback();
            throw new KwantuFaultException("Unable to complete database transaction", ex);
        }
    }
