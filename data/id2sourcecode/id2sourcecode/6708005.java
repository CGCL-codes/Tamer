    @Test
    public void testAttemptTraces() {
        GlobalConfiguration.getConfiguration().getProperty(G_PROPERTIES.TEMP);
        LearnerEvaluationConfiguration evalConf = new LearnerEvaluationConfiguration(null);
        final String moduleName = "locker";
        evalConf.config = ErlangModule.setupErlangConfiguration(new File("ErlangExamples/locker", moduleName + ErlangRunner.ERL.ERL.toString()));
        ErlangOracleLearner learner = new ErlangOracleLearner(null, evalConf);
        ErlangModule mod = ErlangModule.findModule(moduleName);
        Assert.assertTrue(mod.behaviour instanceof OTPGenServerBehaviour);
        Assert.assertTrue(mod.behaviour.dependencies.isEmpty());
        ErlangLabel initLabel = mod.behaviour.convertErlToMod(AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",'init','AnyWibble','ok'}", evalConf.config)), labelLock = mod.behaviour.convertErlToMod(AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",'call','lock',{'ok','locked'}}", evalConf.config)), labelRead = mod.behaviour.convertErlToMod(AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",'call','read','AnyWibble'}", evalConf.config)), labelWrite = mod.behaviour.convertErlToMod(AbstractLearnerGraph.generateNewLabel("{" + ErlangLabel.missingFunction + ",'call',{'write','AnyWibble'},{'ok','AnyWibble'}}", evalConf.config));
        mod.behaviour.getAlphabet().add(initLabel);
        mod.behaviour.getAlphabet().add(labelLock);
        mod.behaviour.getAlphabet().add(labelRead);
        mod.behaviour.getAlphabet().add(labelWrite);
        List<Label> trace = Arrays.asList(new Label[] { initLabel, labelLock });
        TraceOutcome tr = learner.askErlang(trace);
        Assert.assertEquals(TRACEOUTCOME.TRACE_OK, tr.outcome);
        Assert.assertEquals("[{" + ErlangLabel.missingFunction + ",'init','AnyWibble','ok'},{" + ErlangLabel.missingFunction + ",'call','lock',{'ok','locked'}}]", RPNILearner.questionToString(Arrays.asList(tr.answerDetails)));
        tr = learner.askErlang(Arrays.asList(new Label[] { initLabel, labelLock, labelLock }));
        Assert.assertEquals(TRACEOUTCOME.TRACE_FAIL, tr.outcome);
        Assert.assertEquals("[{" + ErlangLabel.missingFunction + ",'init','AnyWibble','ok'},{" + ErlangLabel.missingFunction + ",'call','lock',{'ok','locked'}},{" + ErlangLabel.missingFunction + ",'call','lock',{'ok','locked'}}]", RPNILearner.questionToString(Arrays.asList(tr.answerDetails)));
        tr = learner.askErlang(Arrays.asList(new Label[] { initLabel, labelLock, labelWrite, labelRead }));
        Assert.assertEquals(TRACEOUTCOME.TRACE_OK, tr.outcome);
        Assert.assertEquals("[{" + ErlangLabel.missingFunction + ",'init','AnyWibble','ok'},{" + ErlangLabel.missingFunction + ",'call','lock',{'ok','locked'}},{" + ErlangLabel.missingFunction + ",'call',{'write','AnyWibble'},{'ok','AnyWibble'}},{" + ErlangLabel.missingFunction + ",'call','read','AnyWibble'}]", RPNILearner.questionToString(Arrays.asList(tr.answerDetails)));
        ErlangLabel lbl = tr.answerDetails[3];
        tr.answerDetails[3] = new ErlangLabel(lbl.function, lbl.callName, lbl.input, new OtpErlangAtom("aa"));
        mod.behaviour.getAlphabet().add(tr.answerDetails[3]);
        tr = learner.askErlang(Arrays.asList(tr.answerDetails));
        Assert.assertEquals(TRACEOUTCOME.TRACE_DIFFERENTOUTPUT, tr.outcome);
        Assert.assertEquals("[{" + ErlangLabel.missingFunction + ",'init','AnyWibble','ok'},{" + ErlangLabel.missingFunction + ",'call','lock',{'ok','locked'}},{" + ErlangLabel.missingFunction + ",'call',{'write','AnyWibble'},{'ok','AnyWibble'}},{" + ErlangLabel.missingFunction + ",'call','read','AnyWibble'}]", RPNILearner.questionToString(Arrays.asList(tr.answerDetails)));
    }
