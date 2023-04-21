    protected void startBeforeBridge(VirtualMachine vm) throws VMException {
        if (!running) {
            logging.debug(LOG_NAME, "starting bridge....");
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
                for (DHCPEntry e : entries) {
                    if (!found && e.status.equals("unused")) {
                        e.status = "1";
                        this.addr = InetAddress.getByName(e.addr);
                        mac = e.mac;
                        ProcessExecutor.exec(true, bridgeCmd, ((XenVirtualMachine) vm).image.getPath(), mac);
                        found = true;
                    }
                    mapperFile.writeBytes(e.mac + "\t" + e.addr + "\t\t" + e.status + "\n");
                }
                running = true;
            } catch (FileNotFoundException e) {
                throw new VMException(e);
            } catch (IOException e) {
                throw new VMException(e);
            } catch (ProcessExecutorException e) {
                throw new VMException(e);
            }
        }
    }
