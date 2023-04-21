    @Override
    public void run() {
        while (running) {
            if (!initialized) {
                io.getOut().write("Initializing Executor Thread...\n");
                io.getOut().flush();
                InitializationResult initializationResult = new InitializationResult();
                try {
                    Class persistenceClass = this.getContextClassLoader().loadClass(PERSISTENCE_CLASS_NAME);
                    Method createEntityManagerFactoryMethod = persistenceClass.getDeclaredMethod(CREATE_ENTITY_MANAGER_FACTORY_METHOD_NAME, String.class);
                    Object entityManagerFactory = createEntityManagerFactoryMethod.invoke(null, this.persistenceUnitName);
                    Class entityManagerFactoryClass = entityManagerFactory.getClass();
                    Method createEntityManagerMethod = entityManagerFactoryClass.getDeclaredMethod(CREATE_ENTITY_MANAGER_METHOD_NAME);
                    this.entityManager = createEntityManagerMethod.invoke(entityManagerFactory);
                    this.createQueryMethod = this.entityManager.getClass().getMethod(CREATE_QUERY_METHOD_NAME, String.class);
                    Class queryClass = this.getContextClassLoader().loadClass(QUERY_CLASS_NAME);
                    this.getResultListMethod = queryClass.getMethod(GET_RESULT_LIST_METHOD_NAME);
                    this.initialized = true;
                    io.getOut().write("Initialized successfully\n");
                    io.getOut().flush();
                    initializationResult.setErrors(false);
                } catch (Exception ex) {
                    initializationResult.setErrors(true);
                    initializationResult.setThrowable(ex);
                    this.stopThread();
                }
                this.listenerSupport.notifListenersForInitialized(initializationResult);
            } else {
                if (!this.queryQueue.isEmpty()) {
                    String queryString = null;
                    synchronized (this.queryQueue) {
                        queryString = this.queryQueue.remove(0);
                    }
                    JPAExecutorResult result = new JPAExecutorResult();
                    try {
                        io.getOut().write("Executing Query " + queryString + "\n");
                        io.getOut().flush();
                        Object query = this.createQueryMethod.invoke(this.entityManager, queryString);
                        List resultList = (List) this.getResultListMethod.invoke(query);
                        result.setResultList(resultList);
                    } catch (Exception ex) {
                        io.getErr().write("Error executing Query: " + ex.getMessage() + "\n");
                        io.getErr().flush();
                        result.setErrors(true);
                        result.setErrorMessage(ExceptionUtil.findRootCause(ex).getMessage());
                    }
                    this.listenerSupport.notifyListenersForResult(result);
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }
