        public void handle(Server server, INonBlockingConnection client, byte[] data) throws IOException {
            ClientSession cs = server.getClient(client);
            System.out.println("cs email: " + cs.getEmail());
            EnterChannelMessage message = new EnterChannelMessage(data, Protocol.TCP_BODY_BEGIN);
            EnterChannelResponse response;
            GetClassroomTx tx = new GetClassroomTx(message.getChannelName());
            try {
                Classroom classroom = (Classroom) Persister.getInstance().execute(tx);
                if (classroom != null) {
                    GetMemberTx tx2 = new GetMemberTx(message.getChannelName(), cs.getEmail());
                    Member member = (Member) Persister.getInstance().execute(tx2);
                    if (member != null) {
                        member.setOnline(true);
                        response = new EnterChannelResponse(member.hashCode(), StatusCode.OK);
                    } else {
                        response = new EnterChannelResponse(-1, StatusCode.EntryDenied);
                    }
                } else {
                    response = new EnterChannelResponse(-1, StatusCode.NotFound);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                response = new EnterChannelResponse(-1, StatusCode.Fail);
            }
            server.send(client, response);
        }
