    private void performEncapsulate(String typeName, String fieldName, String getterName, String setterName, boolean preConditionsWarningExpected, boolean userInputWarningsExpected, int fieldVisibility, boolean read, boolean write) throws Exception {
        cat.info("Testing " + getStrippedTestName());
        final Project project = getMutableProject();
        BinTypeRef aRef = project.findTypeRefForName(typeName);
        BinField field = aRef.getBinCIType().getDeclaredField(fieldName);
        final EncapsulateField encapsulator = new EncapsulateField(new NullContext(project), field);
        RefactoringStatus status = encapsulator.checkPreconditions();
        assertTrue("Preconditions check status is ok: " + status.getAllMessages(), preConditionsWarningExpected ? !status.isErrorOrFatal() : status.isOk());
        encapsulator.setGetterName(getterName);
        encapsulator.setSetterName(setterName);
        encapsulator.setUsages(encapsulator.getAllUsages());
        encapsulator.setFieldVisibility(fieldVisibility);
        encapsulator.setEncapsulateRead(read);
        encapsulator.setEncapsulateWrite(write);
        status = encapsulator.checkUserInput();
        assertTrue("User input check status is ok: " + status.getAllMessages(), userInputWarningsExpected ? !status.isErrorOrFatal() : status.isOk());
        status = encapsulator.apply();
        assertTrue("Encapsulated successfully: " + status == null ? "" : status.getAllMessages(), status == null || status.isOk());
        RwRefactoringTestUtils.assertSameSources("Encapsulated field", getExpectedProject(), project);
        cat.info("SUCCESS");
    }
