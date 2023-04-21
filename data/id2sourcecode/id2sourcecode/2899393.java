    public void proccessReservation(Reservation reservation) {
        try {
            Date now = this.dateFormatter.parse(this.dateFormatter.format(new Date()));
            if (wasDateReached(reservation.getDateTo())) {
                throw new RuntimeException("Outdated reservation");
            }
            Book reservationBook = this.entityManager.merge(reservation.getBook());
            User reservationUser = this.entityManager.merge(reservation.getUser());
            Loan newLoan = new Loan();
            newLoan.setBook(reservationBook);
            newLoan.setUser(reservationUser);
            newLoan.setDateFrom(now);
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.DATE, this.BORROW_DEFAULT_TIME);
            newLoan.setDateTo(c.getTime());
            newLoan.setloanState(LoanState.WAITING);
            this.entityManager.persist(newLoan);
            this.entityManager.remove(reservation);
            reservationBook.setBookState(BookState.WAITING_FOR_BORROWER);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
