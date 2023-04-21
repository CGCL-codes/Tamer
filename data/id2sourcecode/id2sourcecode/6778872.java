    @Test
    public void shouldNotCompleteTransactionWhenDebtorDoesNotExist() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        givenDebtorConformsToValidation(true);
        givenDebtorBalance(null);
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
            fail("Should have failed since debtor does not exist");
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verifyNoBalanceWasChanged();
        }
    }
