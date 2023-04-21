    @Override
    public byte[] backup(final UserDetails user) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (user != null) {
            Set<Account> accounts = user.getAccounts();
            if (!CollectionUtils.isEmpty(accounts)) {
                ZipOutputStream zos = new ZipOutputStream(bos);
                for (Account account : accounts) {
                    NameValuePair<String, String> qifs = qif(account);
                    ZipEntry cash = new ZipEntry(account.getName() + ".qif");
                    zos.putNextEntry(cash);
                    copy(StringUtils.asStream(qifs.getKey()), zos);
                    zos.closeEntry();
                    if (qifs.getValue() != null) {
                        ZipEntry investment = new ZipEntry(account.getName() + "-investment.qif");
                        zos.putNextEntry(investment);
                        copy(StringUtils.asStream(qifs.getValue()), zos);
                        zos.closeEntry();
                    }
                }
                zos.close();
            }
        }
        return bos.toByteArray();
    }
