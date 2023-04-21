    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        try {
            JobDetail jobDetail = ctx.getJobDetail();
            JobDataMap dataMap = jobDetail.getJobDataMap();
            User user = (User) dataMap.get("user");
            String[] hosts = (String[]) dataMap.get("hosts");
            EncryptUserData enc = new EncryptUserData(user);
            String hash = enc.getAccountHash();
            String ipNew = getActualIp().trim();
            System.out.println("IP is " + ipNew);
            Hashtable<String, String> domains = user.getDomainsData();
            URL url = new URL(URL_DNS + hash);
            System.out.println(URL_DNS + hash);
            URLConnection urlConnection = url.openConnection();
            InputStream inStream = urlConnection.getInputStream();
            List items = FreeDNSXmlReader.domainListItems(IOUtils.toByteArray(inStream));
            boolean ipIsChanged = false;
            boolean messageSent = false;
            for (int i = 0; i < hosts.length; i++) {
                String h = hosts[i];
                String oldIpHost = domains.get(h).trim();
                System.out.println("For host " + h + " old ip is " + oldIpHost);
                if (!ipNew.equals(oldIpHost)) {
                    ipIsChanged = true;
                }
                for (Iterator<Element> iter = items.iterator(); iter.hasNext(); ) {
                    Element itemEl = iter.next();
                    String domain = itemEl.elementText("host");
                    if (domain.equals(h)) {
                        String ipMemo = itemEl.elementText("address");
                        if (ipIsChanged || !ipMemo.equals(ipNew)) {
                            if (!messageSent) {
                                MailSender mailSender = new MailSender(to, from, usrMail, pwdMail);
                                mailSender.setSmtpServer(smtpServer);
                                messageSent = true;
                                if (smtpPort != null) {
                                    mailSender.setSmtpPort(smtpPort);
                                }
                                mailSender.sendMail(ipNew);
                            }
                            String updateString = itemEl.elementText("url");
                            updateDNS(domain, updateString);
                            user.addIp(domain, ipNew);
                        }
                    }
                }
            }
            ObjectSerializer.serialize(user, user.getUsername() + ".ser");
            System.out.println("##########################################################");
            System.out.println("                          INFO                           ");
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG, Locale.ITALY);
            String date = dateFormat.format(ctx.getNextFireTime());
            System.out.println(" Next fire time: " + date);
            System.out.println("##########################################################");
            System.out.println();
            System.out.println();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
