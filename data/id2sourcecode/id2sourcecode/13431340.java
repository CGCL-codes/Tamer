    @Test
    public void test9() {
        Month month = new Month(budget1);
        Random rnd = new Random(System.currentTimeMillis());
        int middle = (firstDay + lastDay) / 2;
        long c1StartAmount = (long) (rnd.nextFloat() * 10000);
        long c2StartAmount = (long) (rnd.nextFloat() * 10000);
        month.setStartingAllocation(category1, c1StartAmount);
        month.setStartingAllocation(category2, c2StartAmount);
        List<Transaction> c1FirstHalfTr = new ArrayList<Transaction>();
        List<Transaction> c1SecondHalfTr = new ArrayList<Transaction>();
        long c1FirstHalfAmount = 0L;
        long c1SecondHalfAmount = 0L;
        List<Transaction> c2EvenTr = new ArrayList<Transaction>();
        List<Transaction> c2OddTr = new ArrayList<Transaction>();
        long c2EvenAmount = 0L;
        long c2OddAmount = 0L;
        for (int i = 0; i < 1000; i++) {
            int day = (int) (rnd.nextFloat() * (lastDay - firstDay + 1) + firstDay);
            long amount = (long) ((rnd.nextFloat() - 0.5) * 10000);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Transaction transaction = new Transaction(amount, 0, calendar.getTime(), new MoneyPit(cash), new MoneyPit(category1, month.getBudget()));
            month.addAllocation(transaction);
            if (day <= middle) {
                c1FirstHalfTr.add(transaction);
                c1FirstHalfAmount += amount;
            } else {
                c1SecondHalfTr.add(transaction);
                c1SecondHalfAmount += amount;
            }
        }
        for (int i = 0; i < 1000; i++) {
            int day = (int) (rnd.nextFloat() * (lastDay - firstDay + 1) + firstDay);
            long amount = (long) ((rnd.nextFloat() - 0.5) * 10000);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Transaction transaction = new Transaction(amount, 0, calendar.getTime(), new MoneyPit(cash), new MoneyPit(category2, month.getBudget()));
            month.addAllocation(transaction);
            if (day % 2 == 0) {
                c2EvenTr.add(transaction);
                c2EvenAmount += amount;
            } else {
                c2OddTr.add(transaction);
                c2OddAmount += amount;
            }
        }
        assertEquals(c1StartAmount + c1FirstHalfAmount, month.getAllocatedBalance(category1, middle));
        assertEquals(c1StartAmount + c1FirstHalfAmount + c1SecondHalfAmount, month.getAllocatedBalance(category1, lastDay));
        assertEquals(c1StartAmount + c1FirstHalfAmount + c1SecondHalfAmount, month.getFinalAllocation(category1));
        assertEquals(c2StartAmount + c2EvenAmount + c2OddAmount, month.getAllocatedBalance(category2, lastDay));
        assertEquals(c2StartAmount + c2EvenAmount + c2OddAmount, month.getFinalAllocation(category2));
        for (Transaction t : c1FirstHalfTr) month.removeAllocation(t);
        for (int day = firstDay; day <= middle; day++) {
            assertEquals(c1StartAmount, month.getAllocatedBalance(category1, day));
            assertTrue(month.getAllocations(day, category1).isEmpty());
        }
        assertEquals(c1StartAmount + c1SecondHalfAmount, month.getFinalAllocation(category1));
        for (Transaction t : c2EvenTr) month.removeAllocation(t);
        for (int day = firstDay; day <= lastDay; day++) {
            if (day % 2 == 0) {
                assertEquals(month.getAllocatedBalance(category2, day - 1), month.getAllocatedBalance(category2, day));
                assertTrue(month.getAllocations(day, category2).isEmpty());
            }
        }
        for (Transaction t : c1SecondHalfTr) month.removeAllocation(t);
        for (Transaction t : c2OddTr) month.removeAllocation(t);
        for (int day = firstDay; day <= lastDay; day++) assertTrue(month.getAllocations(day).isEmpty());
        assertEquals(c1StartAmount, month.getFinalAllocation(category1));
        assertEquals(c2StartAmount, month.getFinalAllocation(category2));
    }
