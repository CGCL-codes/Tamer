    public void run() {
        BufferedInputStream in;
        BufferedOutputStream out;
        File base = new File(Constant.IO.imageBaseDir);
        if (!base.exists()) {
            base.mkdir();
        }
        Proxy p = null;
        if (type == 0) p = Proxy.NO_PROXY; else try {
            p = new Proxy(types[type], new InetSocketAddress(addr.getText(), Integer.parseInt(port.getText())));
        } catch (Exception ex) {
            throw new RuntimeException("Gui_DownloadPictures : error 1 - " + ex);
        }
        if (p != null) {
            byte[] buf = new byte[1024];
            int len;
            HashSet<String> ignoreUrls = SettingsManager.getManager().getIgnoreUrls();
            for (update(0); (checkBox.isSelected() ? cardIndex < cardsInGame.size() : cardIndex < cards.size()) && !cancel; update(cardIndex + 1)) {
                try {
                    CardUrl card = checkBox.isSelected() ? cardsInGame.get(cardIndex) : cards.get(cardIndex);
                    log.info("Downloading card: " + card.name + " (" + card.set + ")");
                    URL url = new URL(CardImageUtils.generateURL(card.collector, card.set));
                    if (ignoreUrls.contains(card.set) || card.token) {
                        if (card.collector != 0) {
                            continue;
                        }
                        url = new URL(card.url);
                    }
                    in = new BufferedInputStream(url.openConnection(p).getInputStream());
                    createDirForCard(card);
                    boolean withCollectorId = false;
                    if (card.name.equals("Forest") || card.name.equals("Mountain") || card.name.equals("Swamp") || card.name.equals("Island") || card.name.equals("Plains")) {
                        withCollectorId = true;
                    }
                    File fileOut = new File(CardImageUtils.getImagePath(card, withCollectorId));
                    out = new BufferedOutputStream(new FileOutputStream(fileOut));
                    while ((len = in.read(buf)) != -1) {
                        if (cancel) {
                            in.close();
                            out.flush();
                            out.close();
                            fileOut.delete();
                            return;
                        }
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    log.error(ex, ex);
                }
            }
        }
        close.setText("Close");
    }
