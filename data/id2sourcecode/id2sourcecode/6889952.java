    public long processBatch() {
        Batch<URL> batch = this.getBatch();
        Iterator<WorkUnit<URL>> itr = batch.iterator();
        while (itr.hasNext()) {
            WorkUnit<URL> unit = itr.next();
            LOG.info("<" + this.getJobId() + "> Invoking web service: " + unit.getWork().toString());
            EtinRetrievalRequest request = null;
            EtinRetrievalResponse response = null;
            EtinRetrievalStub stub = null;
            try {
                HeaderMessageIdType msgId = new HeaderMessageIdType();
                msgId.setHeaderMessageIdType(MessageIdType.fromGenerator().toString());
                MeFHeaderType header = new MeFHeaderType();
                header.setMessageID(msgId);
                header.setAction(this.getWebServiceAction());
                header.setTimestamp(new GregorianCalendar());
                header.setETIN(ClientConfigurator.getCredentials().getETIN());
                header.setSessionIndicator(SessionIndicatorType.Y);
                header.setTestIndicator(TestIndicatorTypeE.T);
                header.setAppSysID(ClientConfigurator.getCredentials().getUsername());
                MeF mef = new MeF();
                mef.setMeF(header);
                EtinRetrievalRequestType body = new EtinRetrievalRequestType();
                body.setTestIndicator(TestIndicatorType.T);
                request = new EtinRetrievalRequest();
                request.setEtinRetrievalRequest(body);
                stub = new EtinRetrievalStub(Axis2Configurator.getConfigurationContext(this), null);
                ServiceClient client = stub._getServiceClient();
                Axis2Configurator.configureOptions(client.getOptions(), unit);
                response = stub.etinRetrieval(request, mef);
                LOG.info("<" + this.getJobId() + "> Result Count: " + response.getEtinRetrievalResponse().getCount());
                SessionFactory sessionFactory = ClientConfigurator.getSessionFactory();
                Session session = sessionFactory.getCurrentSession();
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    Query query = session.createQuery("DELETE FROM " + MefEtinRetrieval.class.getName());
                    int rowsDeleted = query.executeUpdate();
                    int rowsInserted = 0;
                    LOG.info("<" + this.getJobId() + "> Rows Deleted: " + rowsDeleted);
                    if (response.getEtinRetrievalResponse().getCount() > 0) {
                        ETINStatusType[] statuses = response.getEtinRetrievalResponse().getETINStatus();
                        for (int i = 0; i < statuses.length; i++) {
                            ETINFormStatusType[] forms = statuses[i].getETINStatusTypeSequence_type0().getForm();
                            for (int j = 0; j < forms.length; j++) {
                                MefEtinRetrieval etinRetrieval = new MefEtinRetrieval();
                                etinRetrieval.setEtin(statuses[i].getETINStatusTypeSequence_type0().getETIN());
                                etinRetrieval.setStatus(statuses[i].getETINStatusTypeSequence_type0().getStatus());
                                etinRetrieval.setFormName(forms[j].getFormName());
                                etinRetrieval.setFormStatus(forms[j].getFormStatus());
                                session.save(etinRetrieval);
                                rowsInserted++;
                            }
                        }
                    }
                    LOG.info("<" + this.getJobId() + "> Rows Inserted: " + rowsInserted);
                    if (rowsInserted != response.getEtinRetrievalResponse().getCount()) {
                        throw new HibernateException("Result Count != Rows Inserted");
                    }
                    tx.commit();
                    LOG.info("<" + this.getJobId() + "> Transaction Committed");
                } catch (HibernateException e) {
                    LOG.error("Transaction Failed: Hibernate could not update MefEtinRetrevial Records", e);
                    if (tx != null && tx.isActive()) {
                        tx.rollback();
                    }
                    unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                    continue;
                }
            } catch (AxisFault a) {
                ExceptionHandler.handleAxisFault(this, a);
                unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                continue;
            } catch (MeFFaultMessage m) {
                ExceptionHandler.handleMeFFaultMessage(this, m);
                unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                continue;
            } catch (Exception e) {
                ExceptionHandler.handleException(this, e);
                unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                continue;
            }
            unit.setProcessingStatus(WorkUnit.ProcessingStatus.SUCCESS);
        }
        return this.getWorkUnitsProcessed();
    }
