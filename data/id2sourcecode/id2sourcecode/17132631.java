    @Test
    public void testAvarageScore() {
        int a = 10;
        int b = 6;
        double avg = (a + b) / 2;
        Evaluation evalOne = createStrictMock(Evaluation.class);
        Evaluation evalTwo = createStrictMock(Evaluation.class);
        expect(evalOne.getScore()).andReturn(a);
        expect(evalTwo.getScore()).andReturn(b);
        replay(evalOne);
        replay(evalTwo);
        tasterBeanUnderTest.getEvaluations().add(evalOne);
        tasterBeanUnderTest.getEvaluations().add(evalTwo);
        assertEquals(avg, tasterBeanUnderTest.avarageScore());
    }
