    public long processBatch() {
        Batch<URL> batch = this.getBatch();
        Iterator<WorkUnit<URL>> itr = batch.iterator();
        while (itr.hasNext()) {
            WorkUnit<URL> unit = itr.next();
            LOG.info("<" + this.getJobId() + "> Invoking web service: " + unit.getWork().toString());
            GetStateParticipantsListRequest request = null;
            GetStateParticipantsListResponse response = null;
            GetStateParticipantsListStub stub = null;
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
                GetStateParticipantsListRequestType body = new GetStateParticipantsListRequestType();
                request = new GetStateParticipantsListRequest();
                request.setGetStateParticipantsListRequest(body);
                stub = new GetStateParticipantsListStub(Axis2Configurator.getConfigurationContext(this), null);
                ServiceClient client = stub._getServiceClient();
                Axis2Configurator.configureOptions(client.getOptions(), unit);
                response = stub.getStateParticipantsList(request, mef);
                LOG.info("<" + this.getJobId() + "> Result Count: " + response.getGetStateParticipantsListResponse().getCount());
                SessionFactory sessionFactory = ClientConfigurator.getSessionFactory();
                Session session = sessionFactory.getCurrentSession();
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    Query query = session.createQuery("DELETE FROM " + MefStateParticipant.class.getName());
                    int rowsDeleted = query.executeUpdate();
                    int rowsInserted = 0;
                    LOG.info("<" + this.getJobId() + "> Rows Deleted: " + rowsDeleted);
                    if (response.getGetStateParticipantsListResponse().getCount() > 0) {
                        StateParticipantType[] participants = response.getGetStateParticipantsListResponse().getStateParticipant();
                        for (int i = 0; i < participants.length; i++) {
                            MefStateParticipant participant = new MefStateParticipant();
                            participant.setState(participants[i].getState());
                            participant.setParticipantPrograms(participants[i].getParticipantPrograms());
                            session.save(participant);
                            rowsInserted++;
                        }
                    }
                    LOG.info("<" + this.getJobId() + "> Rows Inserted: " + rowsInserted);
                    if (rowsInserted != response.getGetStateParticipantsListResponse().getCount()) {
                        throw new HibernateException("Result Count != Rows Inserted");
                    }
                    tx.commit();
                    LOG.info("<" + this.getJobId() + "> Transaction Committed");
                } catch (HibernateException e) {
                    LOG.error("Transaction Failed: Hibernate could not update MeFStateParticipant Records", e);
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
