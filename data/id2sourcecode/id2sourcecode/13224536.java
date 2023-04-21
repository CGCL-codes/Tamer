    protected void processIteration(final Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        final String tableName = (String) iterationData[0];
        final TupleListIterator tuples = (TupleListIterator) iterationData[1];
        final MetadataWrapper metadataWrapper = tuples.getMetadataWrapper();
        final TupleMetadata metadata = (TupleMetadata) metadataWrapper.getMetadata();
        final String sql = SQLUtilities.createInsertStatementSQL(tableName, metadata);
        try {
            mStatement = mConnection.prepareStatement(sql);
            int totalCount = 0;
            Tuple tuple = null;
            while ((tuple = (Tuple) tuples.nextValue()) != null) {
                SQLUtilities.setStatementParameters(mStatement, tuple, metadata);
                totalCount += mStatement.executeUpdate();
            }
            mOutput.write(new Integer(totalCount));
            mConnection.commit();
            mStatement.close();
        } catch (SQLException e) {
            rollback();
            throw new ActivitySQLUserException(e);
        } catch (PipeClosedException e) {
            rollback();
            iterativeStageComplete();
        } catch (PipeIOException e) {
            rollback();
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            rollback();
            throw new ActivityTerminatedException();
        } catch (ActivityUserException e) {
            rollback();
            throw e;
        } catch (ActivityProcessingException e) {
            rollback();
            throw e;
        } catch (ActivityTerminatedException e) {
            rollback();
            throw e;
        } catch (Throwable e) {
            rollback();
            throw new ActivityProcessingException(e);
        }
    }
