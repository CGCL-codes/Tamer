    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            InetSocketAddress addr = new InetSocketAddress("server.eurobattle.net", 6112);
            String name = "WTSBot";
            String pass = "password";
            String channel = "dpltest";
            boolean pvpgn = true;
            BattleNetChatClientFactory fact = new BattleNetChatClientFactory();
            fact.setPlatform(Platform.X86);
            fact.setProduct(Product.W3TFT_1_23A);
            fact.setKeys("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            fact.setKeyOwner("Richard Milhous Nixon");
            BattleNetChatClient bncc = fact.createClient();
            my_listener = new Listener(bncc);
            bncc.addListener(my_listener);
            bncc.connect(addr).get();
            try {
                bncc.login(name, pass, pvpgn).get();
            } catch (InterruptedException ex) {
                Logger.getLogger(GuardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(GuardView.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Joining channel...");
            bncc.join(channel);
        } catch (SQLException ex) {
            Logger.getLogger(GuardView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GuardView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(GuardView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            my_listener.setlist();
        } catch (SQLException ex) {
            Logger.getLogger(GuardView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
