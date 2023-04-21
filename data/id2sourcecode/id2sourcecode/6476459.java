    @Override
    public void updateUI() {
        super.updateUI();
        allocationColor = UIManager.getColor(GuiPreferences.budgetAllocationKey);
        transferToColor = UIManager.getColor(GuiPreferences.budgetTransferToKey);
        transferFromColor = UIManager.getColor(GuiPreferences.budgetTransferFromKey);
        foregroundColor = UIManager.getColor(GuiPreferences.tableFgKey);
        backgroundColor = UIManager.getColor(GuiPreferences.tableBgKey);
        altBackgroundColor = UIManager.getColor(GuiPreferences.tableAltBgKey);
        categoryIcon = UIManager.getIcon(GuiPreferences.iconExpenseCategoryKey);
        accountIcon = UIManager.getIcon(GuiPreferences.iconAccountKey);
        incomeIcon = UIManager.getIcon(GuiPreferences.iconIncomeSourceKey);
        allocationIcon = UIManager.getIcon(GuiPreferences.iconBudgetAllocKey);
        transferToIcon = UIManager.getIcon(GuiPreferences.iconBudgetToKey);
        transferFromIcon = UIManager.getIcon(GuiPreferences.iconBudgetFromKey);
    }
