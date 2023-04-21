    public boolean update(Stat[] stats) {
        if (_closed) {
            return false;
        }
        try {
            _lookup = new HashMap();
            long readCounter = 0;
            long writeCounter = 0;
            long takeCounter = 0;
            long instanceCounter = 0;
            double memoryUsed = 0;
            long txnCounter = 0;
            long blockingReads = 0;
            long blockingTakes = 0;
            StringBuffer myRaw = new StringBuffer();
            for (int i = 0; i < stats.length; i++) {
                myRaw.append(stats[i].toString() + "\n");
                if (stats[i] instanceof MemoryStat) {
                    MemoryStat ms = (MemoryStat) stats[i];
                    double max = ms.getMaxMemory();
                    double used = ms.getCurrentMemory();
                    memoryUsed = used;
                    double pc = used / max * 100;
                    _status.setText("Memory: " + (int) pc + "% used: " + (int) used + " max: " + (int) max);
                } else if (stats[i] instanceof OpStat) {
                    OpStat op = (OpStat) stats[i];
                    String type = op.getType();
                    int theOp = op.getOp();
                    Long count = new Long(op.getCount());
                    Object[] data = getData(type);
                    data[0] = type;
                    switch(theOp) {
                        case OpStat.READS:
                            data[2] = count;
                            readCounter += count.longValue();
                            break;
                        case OpStat.WRITES:
                            data[3] = count;
                            writeCounter += count.longValue();
                            break;
                        case OpStat.TAKES:
                            data[4] = count;
                            takeCounter += count.longValue();
                            break;
                    }
                } else if (stats[i] instanceof InstanceCount) {
                    InstanceCount myCount = (InstanceCount) stats[i];
                    String type = myCount.getType();
                    Object[] data = getData(type);
                    data[0] = type;
                    data[1] = new Integer(myCount.getCount());
                    instanceCounter += myCount.getCount();
                } else if (stats[i] instanceof TxnStat) {
                    TxnStat myTxns = (TxnStat) stats[i];
                    txnCounter = myTxns.getActiveTxnCount();
                } else if (stats[i] instanceof BlockingOpsStat) {
                    BlockingOpsStat myBlocks = (BlockingOpsStat) stats[i];
                    blockingReads = myBlocks.getReaders();
                    blockingTakes = myBlocks.getTakers();
                } else if (stats[i] instanceof FieldsStat) {
                    FieldsStat myFieldsStat = (FieldsStat) stats[i];
                    _treeTypeView.update(myFieldsStat.getType(), myFieldsStat.getFields());
                }
            }
            Collection col = _lookup.values();
            ArrayList list = new ArrayList();
            list.addAll(col);
            _allStats.update(list);
            if (_piechart != null) {
                _piechart.update((int) takeCounter, (int) writeCounter, (int) readCounter);
            }
            if (_mode == OPSTATS) {
                _chart.update(new String[] { "read", "write", "take" }, new long[] { readCounter, writeCounter, takeCounter });
            } else if (_mode == INSTANCES) {
                _chart.update(new String[] { "Instance count" }, new long[] { instanceCounter });
            } else if (_mode == MEMORY) {
                _chart.update(new String[] { "Memory usage KB" }, new long[] { (long) (memoryUsed / 1024) });
            } else if (_mode == TXNS) {
                _chart.update(new String[] { "Active Txns" }, new long[] { txnCounter });
            } else if (_mode == BLOCKERS) {
                _chart.update(new String[] { "read", "take" }, new long[] { blockingReads, blockingTakes });
            }
            _textArea.setText(myRaw.toString());
            _textArea.revalidate();
        } catch (Exception ex) {
            closeWin();
            DashBoardFrame.theLogger.log(Level.SEVERE, "Problem in update", ex);
        }
        return true;
    }
