import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main {
    private static HashMap<Integer, HashMap<Character, Integer>> finiteAuto = new HashMap<>();
    private static Boolean[] visited = new Boolean[0];
    private static HashSet<Character> alphabet = new HashSet<>();
    private static HashSet<Integer> finalConditions = new HashSet<>();
    private static String w0;
    private static int c0 = 0;

    private static boolean checkAuto(String path){
        try(BufferedReader bf = new BufferedReader(new FileReader(new File(path)))){
            w0 = bf.readLine();
            int alphabetLength = Integer.parseInt(bf.readLine());
            for(int i = 0; i < alphabetLength; i++){
                alphabet.add((char)('a' + i));
            }
            int conditionsLength = Integer.parseInt(bf.readLine());
            c0 = Integer.parseInt(bf.readLine());
            String[] fC = bf.readLine().split(" ");
            for (String c : fC){
                finalConditions.add(Integer.parseInt(c));
            }
            int numberOfEdges = Integer.parseInt(bf.readLine());
            for (int i = 0; i < numberOfEdges; i++){
                String edge = bf.readLine();
                String[] condToCond = edge.split(" ");
                int cFrom = Integer.parseInt(condToCond[0]);
                int cTo = Integer.parseInt(condToCond[2]);
                char let = condToCond[1].toCharArray()[0];
                System.out.println(cFrom + " " + let + " " + cTo);
                try {
                    HashMap<Character, Integer> e = new HashMap<>();
                    if(finiteAuto.get(cFrom) != null){
                        e = finiteAuto.get(cFrom);
                    }
                    e.put(let, cTo);
                    finiteAuto.put(cFrom, e);
                } catch (IndexOutOfBoundsException e){
                    System.out.println(e);
                }
            }
            System.out.println("Automation: " + finiteAuto);
            System.out.println("Word w0 = " + w0);
            HashSet<Integer> start = new HashSet<>();
            visited = new Boolean[conditionsLength];
            Arrays.fill(visited, false);
            search(start, c0);
            System.out.println(start);
            HashSet<Integer> finals = new HashSet<>();
            for(int condition = 0; condition < conditionsLength; condition++){
                HashSet<Integer> temp = new HashSet<>();
                visited = new Boolean[conditionsLength];
                Arrays.fill(visited, false);
                search(temp, condition);
                for(Integer finalState : finalConditions){
                    if(temp.contains(finalState)){
                        finals.add(condition);
                        break;
                    }
                }
            }
            System.out.println(finals);
            for(Integer condition : start){
                visited = new Boolean[conditionsLength];
                Arrays.fill(visited, false);
                if(buildPath(condition, finals)){
                    return true;
                }
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args) {
        String fileString = "automate.txt";
        boolean result = checkAuto(fileString);
        if(result){
            System.out.println("w = w1w0w2");
        }
        else {
            System.out.println("w != w1w0w2");
        }
    }
    private static void search(HashSet<Integer> conditions, int condition){
        visited[condition] = true;
        conditions.add(condition);
        HashMap<Character, Integer> h = finiteAuto.get(condition);
        if(h == null){
            return;
        }
        for(Map.Entry<Character, Integer> e : h.entrySet()){
            if(alphabet.contains(e.getKey()) && !visited[e.getValue()]){
                search(conditions, e.getValue());
            }
        }
        return;
    }
    private static Boolean buildPath(int condition, HashSet<Integer> part){
        int cur = condition;
        for(char trigger : w0.toCharArray()) {
            int nextC = 0;
            if(finiteAuto.get(cur) != null){
                if(finiteAuto.get(cur).get(trigger) != null) {
                    nextC = finiteAuto.get(cur).get(trigger);
                    cur = nextC;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        if(part.contains(cur) && cur != condition){
            return  true;
        }
        return false;
    }
}
