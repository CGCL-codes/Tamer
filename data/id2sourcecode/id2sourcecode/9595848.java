    public String execute() throws Exception {
        log.debug("Edit Customer :" + id);
        Customers customer;
        try {
            if (oper.equalsIgnoreCase("add")) {
                log.debug("Add Customer");
                customer = new Customers();
                int nextid = customersDao.nextCustomerNumber();
                log.debug("Id for ne Customer is " + nextid);
                customer.setCustomernumber(nextid);
                customer.setCustomername(customername);
                customer.setCountry(country);
                customer.setCity(city);
                customer.setCreditlimit(creditlimit);
                customer.setContactfirstname(contactfirstname);
                customer.setContactlastname(contactlastname);
                if (salesemployee != null) {
                    customer.setSalesemployee(employeeDao.get(salesemployee.getEmployeenumber()));
                }
                customersDao.save(customer);
            } else if (oper.equalsIgnoreCase("edit")) {
                log.debug("Edit Customer");
                customer = customersDao.get(Integer.parseInt(id));
                customer.setCustomername(customername);
                customer.setCountry(country);
                customer.setCity(city);
                customer.setCreditlimit(creditlimit);
                customer.setContactfirstname(contactfirstname);
                customer.setContactlastname(contactlastname);
                if (salesemployee != null) {
                    customer.setSalesemployee(employeeDao.get(salesemployee.getEmployeenumber()));
                }
                customersDao.update(customer);
            } else if (oper.equalsIgnoreCase("del")) {
                StringTokenizer ids = new StringTokenizer(id, ",");
                while (ids.hasMoreTokens()) {
                    int removeId = Integer.parseInt(ids.nextToken());
                    log.debug("Delete Customer " + removeId);
                    customersDao.delete(removeId);
                }
            }
            hTransaction.commit();
        } catch (Exception e) {
            hTransaction.rollback();
            addActionError("ERROR : " + e.getLocalizedMessage());
            addActionError("Is Database in read/write modus?");
            return "error";
        }
        return NONE;
    }
