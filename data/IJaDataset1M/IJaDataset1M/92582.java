package weibo4j.examples.friendships;

import weibo4j.Weibo;
import weibo4j.org.json.JSONObject;

/**
 * @author hezhou
 *
 */
public class ShowFriendships {

    /**
	 * 返回两个用户关系的详细情况 
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
            JSONObject object = getWeibo(true, args).showFriendships(args[2]);
            JSONObject source = object.getJSONObject("source");
            JSONObject target = object.getJSONObject("target");
            System.out.println(source.getString("screen_name") + "与" + target.getString("screen_name") + "互为关注");
            object = getWeibo(true, args).showFriendships(args[3], args[4]);
            source = object.getJSONObject("source");
            target = object.getJSONObject("target");
            System.out.println(source.getString("screen_name") + "与" + target.getString("screen_name") + "互为关注");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Weibo getWeibo(boolean isOauth, String... args) {
        Weibo weibo = new Weibo();
        if (isOauth) {
            weibo.setToken("ea9751fb23b109b59ff415df99f611a8", "b71b4df1dc85c336c15fc79330ecad0b");
        } else {
            weibo.setUserId(args[0]);
            weibo.setPassword(args[1]);
        }
        return weibo;
    }
}
