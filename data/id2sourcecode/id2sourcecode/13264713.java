            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem ti = peerTable.getSelection()[0];
                PeerDecorator dec = (PeerDecorator) ti.getData();
                PeerBean pbean = dec.getBean();
                ChannelBean cbean = getStore().getChannel();
                updateMenu(cbean.getRole(), pbean.getRole());
            }
