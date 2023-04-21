    protected void referenceBridge() throws VMException {
        if (running) {
            logging.debug(LOG_NAME, "prepare bridge...");
            try {
                mapperFile = new RandomAccessFile(dhcpMapper, "rw");
                mapperFile.getChannel().lock();
                LinkedList<DHCPEntry> entries = new LinkedList<DHCPEntry>();
                String line = null, mac = null, addr = null, status = null;
                while ((line = mapperFile.readLine()) != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    mac = tokenizer.nextToken();
                    addr = tokenizer.nextToken();
                    status = tokenizer.nextToken();
                    entries.add(new DHCPEntry(mac, addr, status));
                }
                mapperFile.seek(0);
                mapperFile.setLength(0);
                boolean found = false;
                String current = this.addr.getHostAddress();
                for (DHCPEntry e : entries) {
                    if (!found && current.equals(e.addr)) {
                        int ref = Integer.parseInt(e.status);
                        e.status = Integer.toString(++ref);
                        found = true;
                    }
                    mapperFile.writeBytes(e.mac + "\t" + e.addr + "\t\t" + e.status + "\n");
                }
                mapperFile.close();
            } catch (FileNotFoundException e) {
                throw new VMException(e);
            } catch (IOException e) {
                throw new VMException(e);
            }
        }
    }
