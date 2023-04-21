    private void fillChangelogField() {
        {
            setChangelogName();
            try {
                URL url = new URL(changelogUrl);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                Scanner in = new Scanner(inStream, "UTF-8");
                String line = in.nextLine();
                String changelog = "";
                while (in.hasNext() && !line.contains(Application.getAppVersion())) {
                    changelog += line + "\n";
                    line = in.nextLine();
                }
                in.close();
                inStream.close();
                changelogText.setText(changelog);
            } catch (Exception e) {
            }
        }
    }
