    @Test
    public void shouldNotCompleteTransactionWhenDebtorDoesNotHaveEnoughBalance() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        when(validationService.conformsTo(debtor)).thenReturn(Boolean.TRUE);
        when(paymentRepository.findBalance(debtor)).thenReturn(valueToBeTransfered.subtract(BigDecimal.ONE));
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
            fail("Should have failed since debtor does not have enough balance to transfer");
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verify(paymentRepository, never()).addBalance(debtor, valueToBeTransfered.negate());
            verify(paymentRepository, never()).addBalance(debtor, valueToBeTransfered);
        }
    }
