        public void run() {
            try {
                byte[] cache = new byte[1024];
                DataInputStream reader = new DataInputStream(new FileInputStream(this.file));
                DataOutputStream writer = new DataOutputStream(this.client.getOutputStream());
                int tam;
                while ((tam = reader.read(cache)) != -1) writer.write(cache, 0, tam);
                writer.flush();
                writer.close();
                reader.close();
                this.client.close();
            } catch (IOException e) {
                ArCoLIVELogRepository.getInstance().log("The file tranfer to host " + client.getRemoteSocketAddress() + " has failed");
                return;
            }
            ArCoLIVELogRepository.getInstance().log("The file transfer to host " + client.getRemoteSocketAddress() + " has finished sucessfully");
        }
