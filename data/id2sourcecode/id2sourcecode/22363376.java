    @Test
    public void shouldCompleteTransaction() throws Exception {
        valueToBeTransfered = new BigDecimal("90");
        when(validationService.conformsTo(debtor)).thenReturn(Boolean.TRUE);
        when(paymentRepository.findBalance(debtor)).thenReturn(valueToBeTransfered.add(BigDecimal.ONE));
        paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        verify(paymentRepository).addBalance(debtor, valueToBeTransfered.negate());
        verify(paymentRepository).addBalance(creditor, valueToBeTransfered);
    }
