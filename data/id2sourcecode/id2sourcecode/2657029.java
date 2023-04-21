        protected void runWithCompleteAssignment(final Assignments complete) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, Throwable {
            new BlockJUnit4ClassRunner(getTestClass().getJavaClass()) {

                @Override
                protected void collectInitializationErrors(List<Throwable> errors) {
                }

                @Override
                public Statement methodBlock(FrameworkMethod method) {
                    final Statement statement = super.methodBlock(method);
                    return new Statement() {

                        @Override
                        public void evaluate() throws Throwable {
                            try {
                                statement.evaluate();
                                handleDataPointSuccess();
                            } catch (AssumptionViolatedException e) {
                                handleAssumptionViolation(e);
                            } catch (Throwable e) {
                                reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));
                            }
                        }
                    };
                }

                @Override
                protected Statement methodInvoker(FrameworkMethod method, Object test) {
                    return methodCompletesWithParameters(method, complete, test);
                }

                @Override
                public Object createTest() throws Exception {
                    return getTestClass().getOnlyConstructor().newInstance(complete.getConstructorArguments(nullsOk()));
                }
            }.methodBlock(fTestMethod).evaluate();
        }
