        public HandleConnection(RPService handler, String request, Socket socket, BufferedReader reader, BufferedWriter writer) {
            this.request = request;
            this.socket = socket;
            this.handler = handler;
            this.reader = reader;
            this.writer = writer;
        }
