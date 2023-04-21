    private void sortClasses() {
        final HashTable alreadySortedClasses = new HashTable("SortedClasses");
        for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
            double highestPriority = 100000;
            Class actualClassWithHighestPriority = null;
            for (int c2 = 0; c2 < this.inputParameter.getCustomerClasses().size(); c2++) {
                final Class customerClass2 = (Class) this.inputParameter.getCustomerClasses().get(c2);
                if (alreadySortedClasses.read(customerClass2.getClassID()) == 0) {
                    if (customerClass2.getPriority() < highestPriority) {
                        highestPriority = customerClass2.getPriority();
                        actualClassWithHighestPriority = customerClass2;
                    }
                }
            }
            this.customerClasses.add(actualClassWithHighestPriority);
            alreadySortedClasses.write(actualClassWithHighestPriority.getClassID(), highestPriority);
        }
    }
