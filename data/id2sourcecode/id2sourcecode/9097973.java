        public void onData(int loadedBytes) {
            try {
                String readLine = socket.readStringLine("cp1251");
                while (readLine != null) {
                    if (readLine.startsWith(PING)) {
                        socket.writeStringLine(PONG + " :" + readLine.substring(readLine.indexOf(":") + 1), "cp1251");
                        socket.flush();
                    } else {
                        List list = parser.parseMessages(readLine);
                        notifyCommandListeners(list.iterator());
                    }
                    readLine = socket.readStringLine("cp1251");
                }
            } catch (IOException e) {
                Window.alert("Error");
            }
        }
