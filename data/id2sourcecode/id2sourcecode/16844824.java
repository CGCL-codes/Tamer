        protected boolean doRead() throws IOException {
            return outputPipe.transferFrom(socketChannel) == -1;
        }
