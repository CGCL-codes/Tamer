    @Test
    public void shouldNotCompleteTransactionWhenDebtorIsNotValid() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        givenDebtorConformsToValidation(false);
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verifyNoBalanceWasChanged();
        }
    }
