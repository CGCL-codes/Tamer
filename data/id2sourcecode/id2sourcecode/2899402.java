    public void loanBook(long bookID, User user) {
        Query q = this.entityManager.createNamedQuery("getBookWithID");
        Object result = q.getSingleResult();
        if (result == null) {
            throw new InvalidOperationException("Loan for book with id " + String.valueOf(bookID) + " cannot be made because book wasn't found");
        }
        if (result instanceof Book) {
            Book b = (Book) result;
            Loan l = new Loan();
            l.setBook(b);
            l.setUser(user);
            l.setloanState(LoanState.BORROWED);
            try {
                l.setDateFrom(this.dateFormatter.parse(this.dateFormatter.format(new Date())));
                Calendar c = Calendar.getInstance();
                c.setTime(l.getDateFrom());
                c.add(Calendar.DAY_OF_YEAR, this.BORROW_DEFAULT_TIME);
                l.setDateTo(c.getTime());
                this.entityManager.persist(l);
            } catch (ParseException ex) {
                Logger.getLogger(LibrarienBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new InvalidOperationException("Loan for book with id " + String.valueOf(bookID) + " cannot be made because book wasn't found");
        }
    }
