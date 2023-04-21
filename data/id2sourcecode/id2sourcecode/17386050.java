    public static UserPageDTO GetSingleUserPage(DataDonkey datadonkey, UserDTO User, UserPageDTO userpage, int pagetocrawl) throws FileNotFoundException {
        UserPageDTO thisuserpage = userpage;
        Collection<String> DTOpage = new LinkedList<String>();
        int page = pagetocrawl;
        URL url;
        String line, finalstring = null;
        StringBuffer buffer = new StringBuffer();
        try {
            thread = Thread.currentThread().getName();
            int t = Integer.parseInt(thread);
            System.out.println("WebMoleThread " + t + " using proxy: " + proxy[t]);
            url = new URL(proxy[t] + HTMLuserpage + User.getUserName() + "?setcount=100&page=" + page);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.addRequestProperty("User-Agent", userAgent);
            System.out.println("moling: page " + page + " of " + User.getUserName());
            BufferedReader input = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            input.close();
            connect.disconnect();
            finalstring = buffer.toString();
            finalstring.length();
            DTOpage.add(finalstring);
            thisuserpage.setPages(DTOpage);
            thisuserpage.setLastcrawledpage(pagetocrawl);
            if (thisuserpage.getMaxpages() <= 0) {
                thisuserpage.setMaxpages(Parser.getNumberOfPagesToCrawl(finalstring));
            }
            if (thisuserpage.getMaxpages() == pagetocrawl) {
                thisuserpage.setFinished(true);
            }
            datadonkey.parseUsersPages(User, thisuserpage);
            return thisuserpage;
        } catch (FileNotFoundException fnf) {
            System.err.println("IOException: " + fnf);
            fnf.printStackTrace();
            if (thisuserpage.getMaxpages() > 0) {
                thisuserpage.setMaxpages(thisuserpage.getLastcrawledpage());
            } else thisuserpage.setMaxpages(0);
            thisuserpage.setUserpagesmoled(true);
            thisuserpage.setScrewedup(false);
            DTOpage.add("");
            thisuserpage.setPages(DTOpage);
            return thisuserpage;
        } catch (MalformedURLException e) {
            System.err.println("Bad URL: " + e);
            return null;
        } catch (IOException io) {
            System.err.println("IOException: " + io);
            return null;
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return null;
        }
    }
