    protected void stopBridge() throws VMException {
        if (running) {
            running = false;
            logging.debug(LOG_NAME, "stopping bridge....");
            try {
                RandomAccessFile file = new RandomAccessFile(dhcpMapper, "rw");
                file.getChannel().lock();
                LinkedList<DHCPEntry> entries = new LinkedList<DHCPEntry>();
                String line = null, mac = null, addr = null, status = null;
                while ((line = file.readLine()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    mac = tokenizer.nextToken();
                    addr = tokenizer.nextToken();
                    status = tokenizer.nextToken();
                    entries.add(new DHCPEntry(mac, addr, status));
                }
                file.seek(0);
                file.setLength(0);
                boolean found = false;
                String current = this.addr.getHostAddress();
                for (DHCPEntry e : entries) {
                    if (!found && current.equals(e.addr)) {
                        int ref = Integer.parseInt(e.status);
                        ref--;
                        if (ref <= 0) e.status = "unused"; else e.status = Integer.toString(ref);
                        found = true;
                    }
                    file.writeBytes(e.mac + "\t" + e.addr + "\t\t" + e.status + "\n");
                }
                file.close();
            } catch (FileNotFoundException e) {
                throw new VMException(e);
            } catch (IOException e) {
                throw new VMException(e);
            }
        }
    }
