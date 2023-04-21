            @Override
            public void invoke(List<UrlLog> args) {
                Channel chnl = null;
                try {
                    try {
                        chnl = queue.getChannel();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        queue.processException(ex);
                    }
                    if (null != chnl) {
                        for (UrlLog arg : args) {
                            String rowKey = arg.getURLRowKey();
                            UrlLink urlLink = linkMapper.get(rowKey);
                            String url = urlLink.getUrl();
                            Message msg = new Message(jobId, startURLId, scheId, wrapperId, siteId, jobname, url, "CLS=custom");
                            queue.offer(chnl, msg);
                        }
                    }
                } finally {
                    try {
                        if (null != chnl) {
                            chnl.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
