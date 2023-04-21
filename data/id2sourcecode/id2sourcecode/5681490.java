            public void run() {
                if (!generationNeeded) return;
                generationNeeded = false;
                try {
                    File f = new File("logs/userlist_" + source.getProfile().getName() + ".html");
                    DataOutputStream fos = new DataOutputStream(new FileOutputStream(f));
                    List<BNetUser> users = source.getSortedUsers();
                    fos.write("<table><tr><td colspan=\"4\"><b>".getBytes());
                    String channel = source.getChannel();
                    if (channel != null) fos.write(channel.getBytes());
                    fos.write("</b> (".getBytes());
                    fos.write(Integer.toString(users.size()).getBytes());
                    fos.write(")</td></tr>".getBytes());
                    for (BNetUser ui : users) {
                        StatString ss = ui.getStatString();
                        String product = getIcon(ss.getProduct().getDword(), ss.getIcon(), ui.getFlags());
                        String lag = getLagIcon(ui.getFlags(), ui.getPing());
                        fos.write("<tr>".getBytes());
                        fos.write(("<td><img src=\"images/" + product + ".jpg\"></td>").getBytes());
                        fos.write(("<td>" + ui.toString(GlobalSettings.bnUserToStringUserList) + "</td>").getBytes());
                        fos.write(("<td><img src=\"images/" + lag + ".jpg\"></td>").getBytes());
                        fos.write("</tr>".getBytes());
                    }
                    fos.write("</table>".getBytes());
                    fos.close();
                } catch (Exception e) {
                    Out.exception(e);
                }
            }
