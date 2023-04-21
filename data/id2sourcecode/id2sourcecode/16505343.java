    public long processBatch() {
        Batch<URL> batch = this.getBatch();
        Iterator<WorkUnit<URL>> itr = batch.iterator();
        while (itr.hasNext()) {
            WorkUnit<URL> unit = itr.next();
            LOG.info("<" + this.getJobId() + "> Invoking web service: " + unit.getWork().toString());
            EtinStatusRequest request = null;
            EtinStatusResponse response = null;
            EtinStatusStub stub = null;
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
                EtinStatusRequestType body = new EtinStatusRequestType();
                body.setETIN(net.sf.gateway.mef.configuration.ClientConfigurator.getBatchJobs().getETINStatus().getETIN());
                request = new EtinStatusRequest();
                request.setEtinStatusRequest(body);
                stub = new EtinStatusStub(Axis2Configurator.getConfigurationContext(this), null);
                ServiceClient client = stub._getServiceClient();
                Axis2Configurator.configureOptions(client.getOptions(), unit);
                response = stub.etinStatus(request, mef);
                SessionFactory sessionFactory = ClientConfigurator.getSessionFactory();
                Session session = sessionFactory.getCurrentSession();
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    Query query = session.createQuery("DELETE FROM " + MefEtinStatus.class.getName() + " WHERE ETIN = :ETIN");
                    query.setString("ETIN", response.getEtinStatusResponse().getETIN());
                    int rowsDeleted = query.executeUpdate();
                    int rowsInserted = 0;
                    LOG.info("<" + this.getJobId() + "> Rows Deleted: " + rowsDeleted);
                    ETINFormStatusType[] forms = response.getEtinStatusResponse().getForm();
                    for (int j = 0; j < forms.length; j++) {
                        MefEtinStatus etinStatus = new MefEtinStatus();
                        etinStatus.setEtin(response.getEtinStatusResponse().getETIN());
                        etinStatus.setStatus(response.getEtinStatusResponse().getStatus());
                        etinStatus.setFormName(forms[j].getFormName());
                        etinStatus.setFormStatus(forms[j].getFormStatus());
                        session.save(etinStatus);
                        rowsInserted++;
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
