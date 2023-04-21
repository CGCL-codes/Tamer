    @Test
    public void shouldNotCompleteTransactionWhenDebtorDoesNotExist() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        debtorIsAbleToTransfer(true);
        debtorHasBalance(null);
        replayAll();
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
            fail("Should have failed since debtor does not exist");
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verifyAll();
        }
    }
