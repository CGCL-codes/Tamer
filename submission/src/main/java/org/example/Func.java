package org.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.YamlPrinter;

import java.awt.geom.QuadCurve2D;
import java.io.*;
import java.net.ProxySelector;
import java.util.*;
import java.util.stream.DoubleStream;

public class Func
{
    public int funcId;
    public int funcorder;
    public String fileName;

    public int funcLen;

    public List<Integer> ngramHash = new ArrayList<>();
    public static int N = 16;
    public List<List<String>> Subtree_list = new ArrayList<>();
    public List<List<Integer>> Subtreelen_list = new ArrayList<>();
    public Func(String fileName, int n, HashMap<String, Integer> string2char, HashMap<String, Integer> name_list, CompilationUnit cu1) {
        N = n;
        Initial_Func();
        this.fileName = fileName;
        int index = fileName.indexOf('/');
        String temp_file = fileName;

        while (index != -1) {
            temp_file = temp_file.substring(index + 1);
            index = temp_file.indexOf('/');
        }
        int temp_length = temp_file.length();
        String a = temp_file.substring(0, temp_length - 5);
        int id_temp = Integer.parseInt(a);
        this.funcId = id_temp;
        Get_AST_DFS(string2char, name_list, cu1);

    }

    private void Initial_Func()
    {
        for (int i = 1; i <= 10; i++)
        {
            List<String> newlist = new ArrayList<>();
            this.Subtree_list.add(newlist);
            List<Integer> aaa = new ArrayList<>();
            this.Subtreelen_list.add(aaa);
        }
    }

    public double Caculate_similarity_of_Func(Func another)
    {
        double final_result = 0.000;
        int totallcs = 0;
        int minlen = Math.max(this.funcLen, another.funcLen);
        for (int i = 0; i <= 9; i++)
        {
            int A_onetypeSubtree_number = this.Subtree_list.get(i).size();
            int B_onetypeSubtree_number = another.Subtree_list.get(i).size();
            double onetypescre = 0.000;

            for (int j = 0; j < A_onetypeSubtree_number; j++)
            {
                int totallength = 0;
                String a = this.Subtree_list.get(i).get(j);
                totallength += a.length();
                int temp_result = 0;
                for (int k = 0; k < B_onetypeSubtree_number; k++)
                {
                    String b = another.Subtree_list.get(i).get(k);
                    totallength += b.length();
                    temp_result += longestCommonSubsequence(a, b);

                }
                final_result += (double) temp_result / (double) (totallength - temp_result);
                //double temp_score = (double) temp_result / (double) minlen;
                //this.EverySubtreescore_list.get(i).add(temp_score);
                //onetypescre += temp_score;
            }
            //final_result += onetypescre;
            //this.Subtreescore_list.add(onetypescre);
        }
        return final_result;
    }


    public int huntLCS(char[] s1, char[] s2) {
        if (s1.length > s2.length) {
            char[] tmp = s1;
            s1 = s2;
            s2 = tmp;
        }
        int s1_len = s1.length, s2_len = s2.length;
        // first preprocessing step: computation of the equality points
        ArrayList[] equal = new ArrayList[s2_len];
        Map<Character, ArrayList<Integer>> m1 = new HashMap<>(s1_len);
        for (int i = s1_len-1; i >= 0; i--) {
            if (m1.containsKey(s1[i])){
                m1.get(s1[i]).add(i);
            } else {
                ArrayList<Integer> tmp = new ArrayList<>();
                tmp.add(i);
                m1.put(s1[i], tmp);
            }
        }

        for (int i = 0; i < s2_len; i++) {
            equal[i] = m1.getOrDefault(s2[i], new ArrayList<>());
        }

        // second preprocessing step: similarity threshold table
        int[] threshold = new int[s2_len+1];
        for (int i = 1; i < s2_len + 1; i++) {
            threshold[i] = s1_len+1;
        }

        //processing step: algorithm proper
        for (int i = 0; i < s2_len; i++) {
            int t_size = equal[i].size();
            for (int j = 0; j < t_size; j++) {
                int j_value = (int)equal[i].get(j);
                int k = look_for_threshold_index(j_value, threshold, 0, s2_len);
                if (j_value < threshold[k]) {
                    threshold[k] = j_value;
                }
            }
        }

        // postprocessing step: looking for the result, i.e., the similarity between the two strings
        // it is the first index in threshold with a value different from len(s1) + 1, starting from the right
        int res = 0;
        for (int i = s2_len; i > 0; i--) {
            if (s1_len+1 != threshold[i]) {
                res = i;
                break;
            }
        }
        return res;
    }

    public int look_for_threshold_index(int j, int[] threshold, int left, int right) {
        if (left > right) {
            return -1;
        }
        else if(left+1 == right || left == right) {
            return right;
        }
        else {
            int mid = (left+right)/2;
            if (j <= threshold[mid]) {
                right = mid;
            } else {
                left = mid;
            }
            return look_for_threshold_index(j, threshold, left, right);
        }
    }

    public int longestCommonSubsequence(String text1, String text2) {
        char[] cs1 = text1.toCharArray();
        char[] cs2 = text2.toCharArray();
        int[] dp = new int[cs2.length+1];
        for(int i = 1;i<=cs1.length;i++){
            int pre=0;
            for(int j =1;j<=cs2.length;j++){

                int cur = dp[j];
                if(cs1[i-1]==cs2[j-1]){
                    dp[j]=pre+1;
                }else{
                    dp[j]=Math.max(dp[j],dp[j-1]);
                }

                pre =cur;
            }
        }
        return dp[cs2.length];
    }

    /*public int longestCommonSubsequence(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        s1 = " " + s1; s2 = " " + s2;
        char[] cs1 = s1.toCharArray(), cs2 = s2.toCharArray();
        int[][] f = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (cs1[i] == cs2[j])
                    f[i][j] = f[i -1][j - 1] + 1;
                else
                    f[i][j] = Math.max(f[i - 1][j], f[i][j - 1]);
            }
        }
        return f[n][m];
    }*/


    private void Get_Subtree(Node head, List<String> temporylist, HashMap<String, Integer> string2char, HashMap<String, Integer> name_list)
    {
        Stack<Node> st = new Stack<>();
        st.push(head);
        int index = name_list.get(head.getClass().getSimpleName());
        int count = 0;
        String onesequence = new String();
        while (!st.isEmpty())
        {
            this.funcLen++;
            count++;
            var top = st.pop();
            String temp = top.getClass().getSimpleName();
            temporylist.add(temp);
            char a = (char)(string2char.get(temp) + 48);
            onesequence += a;
            var children = top.getChildNodes();

            for (int i = children.size() - 1; i >= 0; i--) {
                var child = children.get(i);
                st.push(child);
            }
        }
        this.Subtreelen_list.get(index).add(count);
        this.Subtree_list.get(index).add(onesequence);
    }

    private void Get_AST_DFS( HashMap<String, Integer> string2char, HashMap<String, Integer> name_list, CompilationUnit cu1)
    {
        Stack<Node> st = new Stack<>();
        List<String> list_cu1 = new ArrayList<>();
        try {
            //var cu1 = StaticJavaParser.parse(code1);
            st.push(cu1);
            String root = new String();
            while (!st.isEmpty()) {
                this.funcLen++;
//            var head = stack.pop();
                var head = st.pop();
                String temp = head.getClass().getSimpleName();
                //System.out.println(temp);
                list_cu1.add(temp);
                char a = (char)(string2char.get(temp) + 48);
                root += a;
                if (temp.compareTo("TryStmt") != 0 && temp.compareTo("WhileStmt") != 0 && temp.compareTo("IfStmt") != 0 && temp.compareTo("SwitchStmt") != 0 && temp.compareTo("DoStmt") != 0 && temp.compareTo("ForEachStmt") != 0 && temp.compareTo("ForStmt") != 0 && temp.compareTo("SynchronizedStmt") != 0) {
                    var children = head.getChildNodes();

                    for (int i = children.size() - 1; i >= 0; i--) {
                        var child = children.get(i);
                        st.push(child);
                    }
                }
                else
                {
                    Get_Subtree(head, list_cu1, string2char, name_list);
                }
            }
            st.clear();
            this.Subtree_list.get(9).add(root);
            for (int i = N - 1; i < list_cu1.size(); i++)
            {
                String hash_temp = "";
                for (int k = i - N + 1; k <= i; k++)
                    hash_temp += list_cu1.get(k);
                //System.out.println(hash_temp);
                Integer hash_value = hash_temp.hashCode();
                //System.out.println(hash_value);
                this.ngramHash.add(hash_value);
            }
        }catch (Exception e)
        {
            //System.out.println(this.fileName);
        }
        list_cu1.clear();
        //YamlPrinter printer = new YamlPrinter(true);
        //System.out.println(printer.output(cu1));

    }

    private void Get_AST_BFS(String code1) {
        Queue<Node> queue = new LinkedList<>();
        List<String> list_cu1 = new ArrayList<>();
        var cu1 = StaticJavaParser.parse(code1);
        queue.add(cu1);
        int count = 0;
        while (!queue.isEmpty()) {
//            var head = stack.pop();
            var head = queue.poll();

            var children = head.getChildNodes();
            for (var child : children) {
                count++;
                queue.offer(child);
                String temp = child.getClass().getSimpleName();
                list_cu1.add(temp);
            }
        }
        this.funcLen = list_cu1.size();
        for (int i = N - 1; i < count; i++)
        {
            String hash_temp = "";
            for (int k = i - N + 1; k <= i; k++)
                hash_temp += list_cu1.get(k);
            //System.out.println(hash_temp);
            Integer hash_value = hash_temp.hashCode();
            //System.out.println(hash_value);
            this.ngramHash.add(hash_value);
        }
    }

    public static int commonNLine(Func funcA, Func funcB, Map<Integer, HashSet<Integer>> invertedIndex) {
        int res = 0;
        for (var lineHash : funcB.ngramHash) {
            //System.out.println(lineHash);
            if (invertedIndex.containsKey(lineHash) && invertedIndex.get(lineHash).contains(funcA.funcorder)) {
                res += 1;
            }
        }
        return res;
    }

    public static float nLineVerify(Func funcA, Func funcB, Map<Integer, HashSet<Integer>> invertedBox, int[] times) {
        //var res = commonNLine(funcA, funcB, invertedBox);
        var res = times[funcB.funcorder];
        int ngram_number_of_A = funcA.funcLen - N + 1;
        int ngram_number_of_B = funcB.funcLen - N + 1;
        int compare_number = Math.max(ngram_number_of_A, ngram_number_of_B);
        compare_number = compare_number > 0 ? compare_number : 10;
        return 1.0f * res / compare_number;
    }

}
