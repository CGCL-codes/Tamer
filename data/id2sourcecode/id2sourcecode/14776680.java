        @Override
        public void run() {
            try {
                Init(null);
                Context c = ctx;
                StringBuffer result = new StringBuffer();
                if (mList != null && mList.length > 0) {
                    long sum = getSizes(mList);
                    if (sum < 0) {
                        sendProgress("Interrupted", Commander.OPERATION_FAILED);
                        return;
                    }
                    if ((mode & MODE_SORTING) == SORT_SIZE) synchronized (items) {
                        reSort(items);
                    }
                    if (mList.length == 1) {
                        File f = mList[0].f;
                        if (f.isDirectory()) {
                            result.append(c.getString(R.string.sz_folder, f.getName(), num));
                            if (dirs > 0) result.append(c.getString(R.string.sz_dirnum, dirs, (dirs > 1 ? c.getString(R.string.sz_dirsfx_p) : c.getString(R.string.sz_dirsfx_s))));
                        } else result.append(c.getString(R.string.sz_file, f.getName()));
                    } else result.append(c.getString(R.string.sz_files, num));
                    if (sum > 0) result.append(c.getString(R.string.sz_Nbytes, Formatter.formatFileSize(ctx, sum).trim()));
                    if (sum > 1024) result.append(c.getString(R.string.sz_bytes, sum));
                    if (mList.length == 1) {
                        result.append(c.getString(R.string.sz_lastmod));
                        result.append(" ");
                        String date_s;
                        Date date = new Date(mList[0].f.lastModified());
                        if (Locale.getDefault().getLanguage().compareTo("en") != 0) {
                            java.text.DateFormat locale_date_format = DateFormat.getDateFormat(ctx);
                            java.text.DateFormat locale_time_format = DateFormat.getTimeFormat(ctx);
                            date_s = locale_date_format.format(date) + " " + locale_time_format.format(date);
                        } else date_s = (String) DateFormat.format("MMM dd yyyy hh:mm:ss", date);
                        result.append(date_s);
                        if (mList[0].f.isFile()) {
                            FileInputStream in = new FileInputStream(mList[0].f);
                            MessageDigest digester = MessageDigest.getInstance("MD5");
                            byte[] bytes = new byte[8192];
                            int byteCount;
                            while ((byteCount = in.read(bytes)) > 0) {
                                digester.update(bytes, 0, byteCount);
                            }
                            byte[] digest = digester.digest();
                            in.close();
                            result.append("\n\nMD5:\n");
                            result.append(Utils.toHexString(digest));
                        }
                    }
                    result.append("\n\n");
                }
                StatFs stat = new StatFs(dirName);
                long block_size = stat.getBlockSize();
                result.append(c.getString(R.string.sz_total, Formatter.formatFileSize(ctx, stat.getBlockCount() * block_size), Formatter.formatFileSize(ctx, stat.getAvailableBlocks() * block_size)));
                sendReport(result.toString());
            } catch (Exception e) {
                sendProgress(e.getMessage(), Commander.OPERATION_FAILED);
            }
        }
