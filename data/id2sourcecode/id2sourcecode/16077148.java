    @Test
    public void shouldCompleteTransaction() throws Exception {
        BigDecimal valueToBeTransfered = new BigDecimal("90");
        debtorIsAbleToTransfer(true);
        transacteeHasBalance(debtor, valueToBeTransfered.add(BigDecimal.ONE));
        debtorHasItsBalanceDeducted(valueToBeTransfered);
        creditorHasItsBalanceCredited(valueToBeTransfered);
        replayAll();
        paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        verifyAll();
    }
