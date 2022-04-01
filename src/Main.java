import java.util.*;

public class Main {

    public static void main (String[] args) {

        String s1 = "lukerabbitte";
        String s2 = "bleaktribute";

        System.out.println(String_anagram(s1, s2));
    }

    private static Hashtable<Character, Double> hash = new Hashtable<>();

    static {
        hash.put('a', 2.0);hash.put('b', 3.0);hash.put('c', 5.0);hash.put('d', 7.0);
        hash.put('e', 11.0);hash.put('f', 13.0);hash.put('g', 17.0);hash.put('h', 19.0);
        hash.put('i', 23.0);hash.put('j', 29.0);hash.put('k', 31.0);hash.put('l', 37.0);
        hash.put('m', 41.0);hash.put('n', 43.0);hash.put('o', 47.0);hash.put('p', 53.0);
        hash.put('q', 59.0);hash.put('r', 61.0);hash.put('s', 67.0);hash.put('t', 71.0);
        hash.put('u', 73.0);hash.put('v', 79.0);hash.put('w', 83.0);hash.put('x', 89.0);
        hash.put('y', 91.0);hash.put('z', 97.0);
    }

    public static boolean String_anagram(String s1, String s2){
        double startingNum = 1.0;

        for(Character ch : s1.toCharArray()) {
            startingNum = startingNum * hash.get(ch);
            System.out.println(startingNum);
        }
        for(Character ch : s2.toCharArray()) {
            startingNum = startingNum / hash.get(ch);
            System.out.println(startingNum);
        }
        return (startingNum == 1.0);
    }
}