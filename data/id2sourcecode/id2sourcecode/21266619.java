    @SuppressWarnings("unchecked")
    @Test
    public final void testClassCorrWithContext() throws Exception {
        final double confidence1 = .3d;
        final double confidence2 = .5d;
        final double expected = (confidence1 + confidence2) / 2;
        OWLEntity dp1 = getEntity("http://example.org/similarityFloodingTest1.owl#TwoSubClasses", OWLClass.class, ontology1);
        OWLEntity dp2 = getEntity("http://example.org/similarityFloodingTest2.owl#TwoSubClasses", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> dpCorr1 = fac.createCorrespondence(dp1, dp2);
        dpCorr1.setConfidence(confidence1);
        dp1 = getEntity("http://example.org/similarityFloodingTest1.owl#Sub", OWLClass.class, ontology1);
        dp2 = getEntity("http://example.org/similarityFloodingTest2.owl#Sub", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> dpCorr2 = fac.createCorrespondence(dp1, dp2);
        dpCorr2.setConfidence(confidence2);
        initWithCorrespondences(dpCorr1, dpCorr2);
        OWLEntity ent1 = getEntity("http://example.org/similarityFloodingTest1.owl#OneSuperClassOneSubClass", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://example.org/similarityFloodingTest2.owl#OneSuperClassOneSubClass", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = fac.createCorrespondence(ent1, ent2);
        final double actual = baseMatcher.getEvaluation(corr);
        assertEquals(expected, actual, EPSILON);
    }
