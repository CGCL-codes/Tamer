        @Override
        public void run() {
            Transaction t = theDB.readwriteTran();
            DestTran dest = new DestTran(t, theDB.dest);
            try {
                switch(random(3)) {
                    case 0:
                        check(dest.node(node));
                        break;
                    case 1:
                        check(dest.nodeForWrite(node));
                        break;
                    case 2:
                        ByteBuf buf = dest.nodeForWrite(node);
                        buf.putInt(0, num.getAndIncrement());
                        t.create_act(123, 456);
                        break;
                }
            } catch (SuException e) {
                throwUnexpected(e);
            } finally {
                if (t.complete() != null) nfailed.incrementAndGet();
                nreps.incrementAndGet();
            }
        }
