    public void dumpServers(File out) {
        try {
            StringBuilder sb;
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"));
            int idl = 0;
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            for (String server : serversettings.keySet()) {
                sb = new StringBuilder();
                Set<Trigger> tgs = serversettings.get(server).triggers;
                idl++;
                pw.println(indent(idl).append("<server name=\"").append(server).append("\">").toString());
                Map<String, LinkedList<Trigger>> ctr = new HashMap<String, LinkedList<Trigger>>();
                for (Trigger t : tgs) {
                    LinkedList<Trigger> ct = ctr.get(t.getChannel());
                    if (ct == null) {
                        ct = new LinkedList<Trigger>();
                        ctr.put(t.getChannel(), ct);
                    }
                    ct.add(t);
                }
                for (String channel : ctr.keySet()) {
                    idl++;
                    pw.println(indent(idl).append("<channel name=\"").append(channel).append("\">").toString());
                    LinkedList<Trigger> triggers = ctr.get(channel);
                    for (Trigger t : triggers) {
                        idl++;
                        pw.println(indent(idl).append("<trigger text=\"").append(t.getTrigger()).append("\" user=\"").append(t.getUser()).append("\">").toString());
                        for (FFile f : t.getFServInformation().rootfiles) {
                            recPrint(pw, f, idl + 1);
                        }
                        pw.println(indent(idl).append("</trigger>").toString());
                        idl--;
                    }
                    pw.println(indent(idl).append("</channel>").toString());
                    idl--;
                }
                pw.println(indent(idl).append("</server>").toString());
                idl--;
            }
            pw.println("</xml>");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
