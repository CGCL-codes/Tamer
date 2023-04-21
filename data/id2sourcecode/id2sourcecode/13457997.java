    public void buildDataTable() {
        if (hardware instanceof Sensor) {
            Sensor sensor = (Sensor) hardware;
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            List<AbstractHardwareData> dataList = DAOFactory.HIBERNATE.getHardwareDataDAO().getAllData(sensor.getHardwareId());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            for (AbstractHardwareData data : dataList) {
                tableModel.addRow(new Object[] { data.getHardwareId(), data.getDataString(), data.getTimestamp(), data.getChannel() });
            }
        }
    }
