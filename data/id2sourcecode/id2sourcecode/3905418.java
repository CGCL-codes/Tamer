    void dialogBean_propertyChange(PropertyChangeEvent evt) {
        Enumeration e = dialogBean.getChannelIndexes();
        while (e.hasMoreElements()) {
            Integer index = (Integer) e.nextElement();
            String status = dialogBean.getChannelStatusString(index);
            System.out.println("Channel " + index + " has status " + status);
        }
        System.out.println("\t--");
    }
