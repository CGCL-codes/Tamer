    public static void main(String[] args) {
        xAutojoinChannels asd = new xAutojoinChannels("jdbc:mysql://localhost/ircbot?user=root&password=texrulez");
        while (asd.hasResults()) System.out.println(asd.getChannel());
    }
