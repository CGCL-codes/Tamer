    @Test
    public void shouldCompleteTransaction() throws Exception {
        valueToBeTransfered = new BigDecimal("90");
        debtorIsAbleToTransfer(true);
        debtorHasBalance(valueToBeTransfered.add(BigDecimal.ONE));
        debtorHasItsBalanceDeducted();
        creditorHasItsBalanceCredited();
        replayAll();
        paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        verifyAll();
    }
