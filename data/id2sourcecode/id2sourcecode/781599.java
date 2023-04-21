        public final void run() {
            while (true) {
                try {
                    if (!sock.getLocalAddress().getHostAddress().equals(host)) throw new Exception("unauthorized connection from " + sock.getLocalAddress().getHostAddress());
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    String s = in.readLine();
                    if (!s.startsWith("6020 ")) throw new Exception("NMAP protocol error 2: " + s);
                    String id = s.split(" ")[1];
                    int size = new Integer(s.split(" ")[3]).intValue();
                    Vector env = new Vector();
                    for (int x = new Integer(s.split(" ")[2]).intValue() - (in.readLine().length() + 2); x > 0; x -= (s.length() + 2)) {
                        env.add(s = in.readLine());
                    }
                    s = in.readLine();
                    if (!s.startsWith("6021 ")) throw new Exception("NMAP protocol error 3: " + s);
                    boolean process = false;
                    Stack to = new Stack();
                    for (Enumeration e = env.elements(); e.hasMoreElements(); ) {
                        s = (String) e.nextElement();
                        switch(s.charAt(0)) {
                            case 'F':
                                for (Enumeration n = from.elements(); n.hasMoreElements(); ) {
                                    if (s.split(" ")[0].toLowerCase().endsWith((String) n.nextElement())) process = true;
                                }
                                break;
                            case 'R':
                                to.push(s.split(" ")[1]);
                        }
                    }
                    if (to.empty()) process = false;
                    PrintStream out = new PrintStream(sock.getOutputStream());
                    int header = 0;
                    if (process) {
                        out.println("QHEAD " + id);
                        s = in.readLine();
                        if (!s.startsWith("2023 ")) throw new Exception("NMAP protocol error 4: " + s);
                        header = new Integer(s.split(" ")[1]).intValue();
                        for (int x = header; x > 0; x -= (s.length() + 2)) {
                            s = in.readLine();
                            if (s.startsWith("X-Hashcash: ")) process = false;
                        }
                        s = in.readLine();
                        if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 5: " + s);
                    }
                    if (process) {
                        out.println("QCREA");
                        s = in.readLine();
                        if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 6: " + s);
                        out.println("QADDQ " + id + " 0 " + header);
                        s = in.readLine();
                        if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 7: " + s);
                        do {
                            s = "0:" + timeToYYMMDD(cal) + ":" + (String) to.pop() + ":";
                            byte[] c = new byte[s.length() + Math.max(11, 6 + bits * 100 / 595)];
                            System.arraycopy(s.getBytes(), 0, c, 0, s.length());
                            for (int x = s.length(); x < c.length; x++) c[x] = getRandomChar(r);
                            byte[] h = new byte[20];
                            h = md.digest(c);
                            while (!zeroBits(h, bits)) {
                                for (int x = c.length; x-- > s.length() && (c[x] = incChar(c[x])) == 48; ) ;
                                h = md.digest(c);
                            }
                            out.println("QSTOR MESSAGE " + (c.length + 14));
                            out.println("X-Hashcash: " + new String(c));
                            s = in.readLine();
                            if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 8: " + s);
                        } while (!to.empty());
                        out.println("QADDQ " + id + " " + header + " " + (size - header));
                        s = in.readLine();
                        if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 9: " + s);
                        for (Enumeration e = env.elements(); e.hasMoreElements(); ) {
                            out.println("QSTOR RAW " + e.nextElement());
                            s = in.readLine();
                            if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 10: " + s);
                        }
                        out.println("QRUN");
                        s = in.readLine();
                        if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 11: " + s);
                        out.println("QDELE " + id);
                        s = in.readLine();
                        if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 12: " + s);
                    }
                    out.println("QDONE");
                    s = in.readLine();
                    if (!s.startsWith("1000 ")) throw new Exception("NMAP protocol error 13: " + s);
                } catch (Throwable t) {
                    System.out.println(t.getMessage());
                } finally {
                    try {
                        sock.close();
                    } catch (Throwable t) {
                    }
                }
                sock = null;
                freeThreads.push(this);
                waitForJob();
            }
        }
