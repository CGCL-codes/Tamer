            @Override
            public void runExperiment() {
                setSimpleConfiguration(learnerInitConfiguration.config, active, k);
                learnerInitConfiguration.config.setErlangAlphabetAnyElements(EXPANSIONOFANY.ANY_WIBBLE);
                try {
                    ErlangModule.loadModule(learnerInitConfiguration.config);
                } catch (IOException e) {
                    Helper.throwUnchecked("failed to load module from " + learnerInitConfiguration.config.getErlangModuleName(), e);
                }
                Set<List<Label>> Plus = null, Minus = null;
                Plus = ErlangQSMOracle.convertTracesToErl(sPlus, learnerInitConfiguration.config);
                Minus = ErlangQSMOracle.convertTracesToErl(sMinus, learnerInitConfiguration.config);
                RPNIUniversalLearner learner = new ErlangOracleLearner(null, learnerInitConfiguration);
                LearnerGraph outcome = learner.learnMachine(Plus, Minus);
                LearnerGraph expectedGraph = new LearnerGraph(learnerInitConfiguration.config);
                Label lblInit = AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",init,AnyWibble}", learnerInitConfiguration.config), lblLock = AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",call,lock}", learnerInitConfiguration.config), lblUnlock = AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",call,unlock}", learnerInitConfiguration.config), lblCast = AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",cast,AnyWibble}", learnerInitConfiguration.config), lblRead = AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",call, read}", learnerInitConfiguration.config), lblWrite = AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",call,{write,AnyWibble}}", learnerInitConfiguration.config);
                expectedGraph.paths.augmentPTA(Arrays.asList(new Label[] { lblInit, lblLock }), true, false, null);
                expectedGraph.paths.augmentPTA(Arrays.asList(new Label[] { lblLock }), false, false, null);
                CmpVertex init = expectedGraph.getInit(), P1001 = expectedGraph.getVertex(Arrays.asList(new Label[] { lblInit })), P1003 = expectedGraph.getVertex(Arrays.asList(new Label[] { lblInit, lblLock })), reject = expectedGraph.getVertex(Arrays.asList(new Label[] { lblLock }));
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(init), lblWrite, reject);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(init), lblCast, reject);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(init), lblRead, reject);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(init), lblUnlock, reject);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(P1001), lblCast, P1001);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(P1001), lblInit, reject);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(P1001), lblUnlock, reject);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(P1003), lblCast, P1003);
                expectedGraph.addTransition(expectedGraph.transitionMatrix.get(P1003), lblLock, reject);
                Assert.assertNull(WMethod.checkM_and_colours(expectedGraph, outcome, WMethod.VERTEX_COMPARISON_KIND.NONE));
            }
