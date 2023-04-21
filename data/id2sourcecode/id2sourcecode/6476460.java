    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setForeground(foregroundColor);
        if (value == BudgetTreeModel.accounts) {
            setIcon(accountIcon);
            setText(BudgetTreeModel.accounts.toString());
        } else if (value == BudgetTreeModel.expenseCategories) {
            setIcon(categoryIcon);
            setText(BudgetTreeModel.expenseCategories.toString());
        } else if (value == BudgetTreeModel.incomeSources) {
            setIcon(incomeIcon);
            setText(BudgetTreeModel.incomeSources.toString());
        } else if (value instanceof ExpenseCategory) {
            ExpenseCategory bc = (ExpenseCategory) value;
            long allocationAmount = theBudget.monthlyAllocationAmount(bc, cumulative);
            String allocationString = CurrencyFormater.format(allocationAmount);
            long transactionAmount = -theBudget.monthlyAutoTrAmountDest(bc, cumulative);
            String transactionString = CurrencyFormater.format(transactionAmount);
            StringBuffer text = new StringBuffer();
            text.append("<html><b>" + bc.getName() + "</b>");
            if (bc.getSelectUsers()) {
                text.append("(");
                List<User> users = bc.getHolders();
                if (!users.isEmpty()) {
                    text.append(users.get(0).getName());
                    if (users.size() >= 2) {
                        text.append(", ");
                        text.append(users.get(1).getName());
                        if (users.size() > 2) text.append(", ...");
                    }
                } else text.append(" ");
                text.append(")");
            }
            text.append(" [+<font color=#217523>" + allocationString + "</font>] ");
            if (transactionAmount < 0) text.append(" [<font color=#c43127>" + transactionString + "</font>]"); else text.append(" [+<font color=#217523>" + transactionString + "</font>] ");
            text.append("</html>");
            setIcon(categoryIcon);
            setText(text.toString());
        } else if (value instanceof Account) {
            Account account = (Account) value;
            long inAmount = theBudget.monthlyAutoTrAmountDest(new MoneyPit(account));
            long outAmount = theBudget.monthlyAutoTrAmountSource(new MoneyPit(account));
            if (inAmount < 0) {
                outAmount -= inAmount;
                inAmount = 0L;
            }
            if (outAmount < 0) {
                inAmount -= outAmount;
                outAmount = 0;
            }
            outAmount = -outAmount;
            String inString = CurrencyFormater.format(inAmount);
            String outString = CurrencyFormater.format(outAmount);
            StringBuffer text = new StringBuffer();
            text.append("<html><b>" + account.getName() + "</b>");
            List<User> users = account.getAccountHolders();
            if (!users.isEmpty()) {
                text.append("(");
                text.append(users.get(0).getName());
                if (users.size() >= 2) {
                    text.append(", ");
                    text.append(users.get(1).getName());
                    if (users.size() > 2) text.append(", ...");
                }
                text.append(")");
            }
            text.append(" [+<font color=#217523>" + inString + "</font>] ");
            text.append(" [<font color=#c43127>" + outString + "</font>]");
            text.append("</html>");
            setIcon(accountIcon);
            setText(text.toString());
        } else if (value instanceof IncomeSource) {
            IncomeSource incomeSource = (IncomeSource) value;
            long inAmount = incomeSource.getMonthlyAmount();
            long outAmount = theBudget.monthlyAutoTrAmountSource(new MoneyPit(incomeSource, theBudget));
            if (inAmount < 0) {
                outAmount -= inAmount;
                inAmount = 0L;
            }
            if (outAmount < 0) {
                inAmount -= outAmount;
                outAmount = 0;
            }
            outAmount = -outAmount;
            String inString = CurrencyFormater.format(inAmount);
            String outString = CurrencyFormater.format(outAmount);
            StringBuffer text = new StringBuffer();
            text.append("<html><b>" + incomeSource.getName() + "</b>");
            text.append(" [+<font color=#217523>" + inString + "</font>] ");
            text.append(" [<font color=#c43127>" + outString + "</font>]");
            text.append("</html>");
            setIcon(incomeIcon);
            setText(text.toString());
        } else if (value instanceof ExpenseAllocation) {
            setForeground(allocationColor);
            ExpenseAllocation expAlloc = (ExpenseAllocation) value;
            String text = "Allocation: " + CurrencyFormater.format(expAlloc.getAmount());
            AutomaticTransaction.Type type = expAlloc.getType();
            if (type == AutomaticTransaction.Type.DAILY) text += "/day"; else if (type == AutomaticTransaction.Type.WEEKLY) text += "/week"; else if (type == AutomaticTransaction.Type.MONTHLY) text += "/month"; else if (type == AutomaticTransaction.Type.YEARLY) text += "/year";
            setText(text);
            setIcon(allocationIcon);
        } else if (value instanceof AutomaticTransaction) {
            setForeground(transferFromColor);
            AutomaticTransaction autoTr = (AutomaticTransaction) value;
            String text = "Auto. Trans.: " + CurrencyFormater.format(autoTr.getAmount());
            AutomaticTransaction.Type type = autoTr.getType();
            if (type == AutomaticTransaction.Type.DAILY) text += "/day"; else if (type == AutomaticTransaction.Type.WEEKLY) text += "/week"; else if (type == AutomaticTransaction.Type.MONTHLY) text += "/month"; else if (type == AutomaticTransaction.Type.YEARLY) text += "/year";
            setText(text);
            TreePath path = tree.getPathForRow(row);
            MoneyPit source = autoTr.getSource();
            MoneyPit dest = autoTr.getDestination();
            Icon icon = null;
            if (path != null) {
                Object parent = path.getParentPath().getLastPathComponent();
                if (source != MoneyPit.NONE && parent.equals(source.getContent())) {
                    if (source.getType() == MoneyPit.Type.EXPENSE) {
                        icon = transferToIcon;
                        setForeground(transferToColor);
                    } else icon = transferFromIcon;
                } else if (dest != MoneyPit.NONE && parent.equals(dest.getContent())) {
                    if (dest.getType() == MoneyPit.Type.EXPENSE) icon = transferFromIcon; else {
                        icon = transferToIcon;
                        setForeground(transferToColor);
                    }
                }
                setIcon(icon);
            }
        } else {
            this.setText(value.toString());
        }
        return this;
    }
