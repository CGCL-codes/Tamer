    @Test
    public void shouldNotCompleteTransactionWhenDebtorIsNotValid() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        when(validationService.conformsTo(debtor)).thenReturn(Boolean.FALSE);
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verify(paymentRepository, never()).addBalance(debtor, valueToBeTransfered.negate());
            verify(paymentRepository, never()).addBalance(debtor, valueToBeTransfered);
        }
    }
