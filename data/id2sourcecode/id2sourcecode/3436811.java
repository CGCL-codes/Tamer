    @Test
    public void shouldNotCompleteTransactionWhenDebtorDoesNotHaveEnoughBalance() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        debtorIsAbleToTransfer(true);
        debtorHasBalance(valueToBeTransfered.subtract(BigDecimal.ONE));
        replayAll();
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
            fail("Should have failed since debtor does not have enough balance to transfer");
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verifyAll();
        }
    }
