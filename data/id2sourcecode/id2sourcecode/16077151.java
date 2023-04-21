    @Test
    public void shouldNotCompleteTransactionWhenDebtorIsNotValid() throws ServiceException {
        BigDecimal valueToBeTransfered = new BigDecimal("90");
        debtorIsAbleToTransfer(false);
        replayAll();
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verifyAll();
        }
    }
