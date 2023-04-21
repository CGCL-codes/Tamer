    @Test
    public void shouldNotCompleteTransactionWhenDebtorDoesNotHaveEnoughBalance() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        givenDebtorConformsToValidation(true);
        givenDebtorBalance(valueToBeTransfered.subtract(BigDecimal.ONE));
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
            fail("Should have failed since debtor does not have enough balance to transfer");
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verifyNoBalanceWasChanged();
        }
    }
