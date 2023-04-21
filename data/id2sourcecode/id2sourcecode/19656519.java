    public void updateByBatch(String groupId, List<Configuration> configs) {
        if (configs.isEmpty()) return;
        WriteTranSession tran = super.getTransactionManager().openRWTran(false);
        try {
            ObjectBatcher batcher = tran.createObjectBatcher();
            for (Configuration c : configs) {
                batcher.update(c);
            }
            batcher.executeUpdate();
            tran.commit();
        } catch (RuntimeException e) {
            tran.rollback();
            throw e;
        } finally {
            tran.close();
        }
        this.updateVersion(groupId);
    }
