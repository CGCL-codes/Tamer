    @Override
    public void scratch() throws Exception {
        getLogger().info("bufferSize=" + bufferSize);
        byte[] temp = new byte[bufferSize];
        URL url = new URL(destUrl);
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        RandomAccessFile file = null;
        for (int i = 0; i < retryTime; i++) {
            getLogger().info("���Ե�" + i + "��");
            try {
                httpUrl = (HttpURLConnection) url.openConnection();
                file = new RandomAccessFile(new File(destFileName), "rw");
                MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, startPosition, length);
                if (currPosition == 0) httpUrl.setRequestProperty("Range", "bytes=" + startPosition + "-" + (startPosition + length - 1)); else {
                    getLogger().info(String.valueOf("currposition=" + currPosition));
                    httpUrl.setRequestProperty("Range", "bytes=" + currPosition + "-" + (length - currPosition - 1));
                    buffer.position(currPosition);
                }
                if (headers != null) {
                    Iterator<String> itor = headers.keySet().iterator();
                    while (itor.hasNext()) {
                        String key = itor.next();
                        httpUrl.setRequestProperty(key, headers.get(key));
                    }
                }
                bis = new BufferedInputStream(httpUrl.getInputStream());
                String fileSizeStr = httpUrl.getHeaderField("Content-Length");
                getLogger().info("�ļ�����=" + fileSizeStr);
                int count = 0;
                while (!stopFlag) {
                    if (pauseFlag) {
                        continue;
                    }
                    if ((count = bis.read(temp)) != -1) {
                        buffer.put(temp, 0, count);
                        currPosition = currPosition + count;
                        setChanged();
                        notifyObservers(SpiderEvent.DOWNLOADPROGRESS);
                    } else break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                wait(idleTime);
                continue;
            } finally {
                if (httpUrl != null) httpUrl.disconnect();
                if (bis != null) bis.close();
                if (file != null) file.close();
            }
            break;
        }
        getLogger().info("Ƭ���������");
        setChanged();
        notifyObservers(SpiderEvent.DOWNLOADCOMPLETE);
    }
