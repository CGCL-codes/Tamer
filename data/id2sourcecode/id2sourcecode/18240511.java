    public long processBatch() {
        Batch<URL> batch = this.getBatch();
        Iterator<WorkUnit<URL>> itr = batch.iterator();
        while (itr.hasNext()) {
            WorkUnit<URL> unit = itr.next();
            LOG.info("<" + this.getJobId() + "> Invoking web service: " + unit.getWork().toString());
            GetSubmissionReconciliationListRequestArgs request = null;
            GetSubmissionReconciliationListResponse response = null;
            GetSubmissionReconciliationListStub stub = null;
            try {
                boolean moreAvailable = true;
                while (moreAvailable) {
                    HeaderMessageIdType msgId = new HeaderMessageIdType();
                    msgId.setHeaderMessageIdType(MessageIdType.fromGenerator().toString());
                    MeFHeaderType header = new MeFHeaderType();
                    header.setMessageID(msgId);
                    header.setAction(this.getWebServiceAction());
                    header.setTimestamp(new GregorianCalendar());
                    header.setETIN(ClientConfigurator.getCredentials().getETIN());
                    header.setSessionIndicator(SessionIndicatorType.Y);
                    header.setTestIndicator(TestIndicatorType.T);
                    header.setAppSysID(ClientConfigurator.getCredentials().getUsername());
                    MeF mef = new MeF();
                    mef.setMeF(header);
                    GetSubmissionReconciliationListRequestType body = new GetSubmissionReconciliationListRequestType();
                    body.setMaxResults(new PositiveInteger("10"));
                    body.setCategory(CategoryType.IND);
                    request = new GetSubmissionReconciliationListRequestArgs();
                    request.setGetSubmissionReconciliationListRequestArgs(body);
                    stub = new GetSubmissionReconciliationListStub(Axis2Configurator.getConfigurationContext(this), null);
                    ServiceClient client = stub._getServiceClient();
                    Axis2Configurator.configureOptions(client.getOptions(), unit);
                    response = stub.getSubmissionReconciliationList(request, mef);
                    SubmissionIdType[] submissionIds = response.getGetSubmissionReconciliationListResponse().getSubmissionId();
                    SessionFactory sessionFactory = ClientConfigurator.getSessionFactory();
                    Session session = sessionFactory.getCurrentSession();
                    Transaction tx = null;
                    try {
                        tx = session.beginTransaction();
                        Query query = session.createQuery("DELETE FROM " + MefSubmissionReconciliation.class.getName());
                        int rowsDeleted = query.executeUpdate();
                        int rowsInserted = 0;
                        LOG.info("<" + this.getJobId() + "> Rows Deleted: " + rowsDeleted);
                        if (submissionIds != null) {
                            for (int i = 0; i < submissionIds.length; i++) {
                                MefSubmissionReconciliation submissionReconciliation = new MefSubmissionReconciliation();
                                submissionReconciliation.setSubmissionId(submissionIds[i].getSubmissionIdType());
                                session.save(submissionReconciliation);
                                rowsInserted++;
                            }
                        }
                        LOG.info("<" + this.getJobId() + "> Rows Inserted: " + rowsInserted);
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
                    LOG.info("<" + this.getJobId() + "> More Available? " + response.getGetSubmissionReconciliationListResponse().getMoreAvailable());
                    moreAvailable = response.getGetSubmissionReconciliationListResponse().getMoreAvailable();
                }
            } catch (AxisFault a) {
                ExceptionHandler.handleAxisFault(this, a);
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
