    @Test
    public void shouldCompleteTransaction() throws Exception {
        valueToBeTransfered = new BigDecimal("90");
        givenDebtorConformsToValidation(true);
        givenDebtorBalance(valueToBeTransfered.add(BigDecimal.ONE));
        paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        verify(paymentRepository).addBalance(debtor, valueToBeTransfered.negate());
        verify(paymentRepository).addBalance(creditor, valueToBeTransfered);
    }
