    @Test
    public void shouldNotCompleteTransactionWhenDebtorDoesNotExist() throws ServiceException {
        BigDecimal valueToBeTransfered = new BigDecimal("90");
        debtorIsAbleToTransfer(true);
        transacteeHasBalance(debtor, null);
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
