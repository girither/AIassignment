import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

/**
 * Created by admin on 9/23/16.
 */
class graphNode{
    String endvertex;
    int distance;
    public graphNode(String endvertex,int distance){
        this.endvertex = endvertex;
        this.distance = distance;
    }
}

class Path{
    String name;
    int cost;
    public Path(String name,int cost){
        this.name = name;
        this.cost = cost;
    }
}

class queueNode{
    int costdistance;
    String nodeval;
    ArrayList<Path> arrlist;
    public queueNode(String nodeval,int costdistance,ArrayList<Path> arrlist){
        this.costdistance = costdistance;
        this.nodeval = nodeval;
        this.arrlist = arrlist;
    }
}

class queueNodeUCS implements Comparable<queueNodeUCS> {
    int costdistance;
    String nodeval;
    ArrayList<Path> arrlist;
    int uniquecounter;
    public queueNodeUCS(String nodeval,int costdistance,ArrayList<Path> arrlist,int uniquecounter){
        this.costdistance = costdistance;
        this.nodeval = nodeval;
        this.arrlist = arrlist;
        this.uniquecounter = uniquecounter;
    }
    public int compareTo(queueNodeUCS t){
        if (this.costdistance == t.costdistance){
            return this.uniquecounter - t.uniquecounter;
        }
        else {
            return this.costdistance - t.costdistance;
        }
    }
}

class queueNodeAstar implements Comparable<queueNodeAstar> {
    int costdistance;
    String nodeval;
    ArrayList<Path> arrlist;
    int uniquecounter;
    int hueristic;
    public queueNodeAstar(String nodeval,int costdistance,ArrayList<Path> arrlist,int uniquecounter,int heuristic){
        this.costdistance = costdistance;
        this.nodeval = nodeval;
        this.arrlist = arrlist;
        this.uniquecounter = uniquecounter;
        this.hueristic = heuristic;
    }
    public int compareTo(queueNodeAstar t){
        if (this.hueristic == t.hueristic){
            return this.uniquecounter - t.uniquecounter;
        }
        else {
            return this.hueristic - t.hueristic;
        }
    }
}


class inputtestcase{
    String algorithm;
    String source;
    String destination;
    HashMap<String,Integer> sundaylivetraffic;
    HashMap<String,ArrayList<graphNode>> arrlist;
    public inputtestcase(String algorithm,String source,String destination,HashMap<String,Integer> hsmap,HashMap<String,ArrayList<graphNode>> arrlist) {
        this.source = source;
        this.algorithm = algorithm;
        this.destination = destination;
        this.sundaylivetraffic = hsmap;
        this.arrlist = arrlist;
    }
}


public class homework {

    public inputtestcase readinputtestcase(){
        try{
            String dataset = System.getProperty("user.dir") + "/input.txt";
            File file = new File(dataset);
            Scanner input = new Scanner(file);
            String algorithm = input.nextLine();
            algorithm =algorithm.trim();
            String source = input.nextLine();
            source = source.trim();
            String destination = input.nextLine();
            destination = destination.trim();
            int countlivetrafficlines = Integer.parseInt(input.nextLine());
            int counter =0;
            HashMap<String,ArrayList<graphNode>> arrlist = new HashMap();
            while(input.hasNextLine() && counter<countlivetrafficlines){
                String line = input.nextLine();
                String[] arr = line.trim().split(" ");
                graphNode newentry = new graphNode(arr[1],Integer.parseInt(arr[2]));
                if(arrlist.containsKey(arr[0])) {
                    ArrayList<graphNode> result =arrlist.get(arr[0]);
                    result.add(newentry);
                }
                else{
                    ArrayList<graphNode> result = new ArrayList();
                    result.add(newentry);
                    arrlist.put(arr[0],result);
                }
                counter++;
            }
            int countsundaytraffic = Integer.parseInt(input.nextLine());
            int counter2 =0;
            HashMap<String,Integer> hsmap = new HashMap();
            while(input.hasNextLine() && counter2<countsundaytraffic){
                String line = input.nextLine();
                String[] arr = line.trim().split(" ");
                hsmap.put(arr[0],Integer.parseInt(arr[1]));
                counter2++;
            }
            inputtestcase itestcase = new inputtestcase(algorithm,source,destination,hsmap,arrlist);
            input.close();
            return itestcase;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return new inputtestcase("","","",new HashMap<String,Integer>(),new HashMap<String,ArrayList<graphNode>>());
    }
    public static void main(String[] args){
        homework hw = new homework();
        inputtestcase testcase = hw.readinputtestcase();
        if(testcase.algorithm.equals("BFS")){
          hw.dobreadthfirstsearch(testcase);
        }
        else if(testcase.algorithm.equals("DFS")){
          hw.dodepthfirstsearch(testcase);
        }
        else if(testcase.algorithm.equals("UCS")){
          hw.doUCS(testcase);
        }
        else if(testcase.algorithm.equals("A*")){
          hw.doAstar(testcase);
        }
    }
    public void printpath(ArrayList<Path> path){

        try {
            PrintWriter writer = new PrintWriter("output.txt");
            for (Path p : path) {
                writer.println(p.name + " " + p.cost);
            }
            writer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dobreadthfirstsearch(inputtestcase testcase){

        Queue<queueNode> queue = new LinkedList<queueNode>();
        if(testcase.source.equals(testcase.destination)){
            ArrayList<Path> pathlist = new ArrayList();
            pathlist.add(new Path(testcase.source,0));
            printpath(pathlist);
            return;
        }
        String destination = testcase.destination;
        ArrayList<Path> listofpath = new ArrayList();
        listofpath.add(new Path(testcase.source,0));
        queue.offer(new queueNode(testcase.source,0,listofpath));
        HashSet<String> isvisited = new HashSet();
        HashSet<String> isInQueue = new HashSet();
        isInQueue.add(testcase.source);
        while(!queue.isEmpty()) {
            queueNode popelem = queue.poll();
            if (popelem.nodeval.equals(destination)) {
                printpath(popelem.arrlist);
                return;
            }
            isvisited.add(popelem.nodeval);
            ArrayList<graphNode> children = testcase.arrlist.get(popelem.nodeval);
            for (graphNode child : children) {
                if (!(isvisited.contains(child.endvertex) || isInQueue.contains(child.endvertex))) {
                    ArrayList<Path> newlist = new ArrayList(popelem.arrlist);
                    int distance = popelem.costdistance + 1;
                    newlist.add(new Path(child.endvertex, distance));
                    queue.offer(new queueNode(child.endvertex,distance, newlist));
                    isInQueue.add(child.endvertex);
                } else {
                    continue;
                }
            }
        }
    }
    public void dodepthfirstsearch(inputtestcase testcase){

        Stack<queueNode> queue = new Stack<queueNode>();
        if(testcase.source.equals(testcase.destination)){
            ArrayList<Path> pathlist = new ArrayList();
            pathlist.add(new Path(testcase.source,0));
            printpath(pathlist);
            return;
        }
        String destination = testcase.destination;
        ArrayList<Path> listofpath = new ArrayList();
        listofpath.add(new Path(testcase.source,0));
        queue.push(new queueNode(testcase.source,0,listofpath));
        HashSet<String> isvisited = new HashSet();
        HashSet<String> isInQueue = new HashSet();
        isInQueue.add(testcase.source);
        while(!queue.isEmpty()) {
            queueNode popelem = queue.pop();
            if (popelem.nodeval.equals(destination)) {
                printpath(popelem.arrlist);
                return;
            }
            isvisited.add(popelem.nodeval);
            ArrayList<graphNode> children = testcase.arrlist.get(popelem.nodeval);
            ArrayList<graphNode> childreverse;
            if (children==null){
                childreverse  = new ArrayList<graphNode>();
            }
            else {
                childreverse = new ArrayList<graphNode>(children);
                Collections.reverse(childreverse);
            }
            for (graphNode child : childreverse) {
                if (!(isvisited.contains(child.endvertex) || isInQueue.contains(child.endvertex))) {
                    ArrayList<Path> newlist = new ArrayList(popelem.arrlist);
                    int distance = popelem.costdistance + 1;
                    newlist.add(new Path(child.endvertex, distance));
                    queue.push(new queueNode(child.endvertex,distance, newlist));
                    isInQueue.add(child.endvertex);
                } else {
                    continue;
                }
            }
        }
    }
    public void doUCS(inputtestcase testcase){

        PriorityQueue<queueNodeUCS> queue = new PriorityQueue<queueNodeUCS>();
        if(testcase.source.equals(testcase.destination)) {
            ArrayList<Path> pathlist = new ArrayList();
            pathlist.add(new Path(testcase.source, 0));
            printpath(pathlist);
            return;
        }
        String destination = testcase.destination;
        ArrayList<Path> listofpath = new ArrayList();
        listofpath.add(new Path(testcase.source,0));
        int globalcounter =0;
        queue.offer(new queueNodeUCS(testcase.source,0,listofpath,globalcounter));
        globalcounter = globalcounter+1;
        HashSet<String> isvisited = new HashSet();
        HashSet<String> isInQueue = new HashSet();
        isInQueue.add(testcase.source);
        while(!queue.isEmpty()) {
            queueNodeUCS popelem = queue.poll();
            if (popelem.nodeval.equals(destination)) {
                printpath(popelem.arrlist);
                return;
            }
            isvisited.add(popelem.nodeval);
            ArrayList<graphNode> children = testcase.arrlist.get(popelem.nodeval);
            for (graphNode child : children) {
                if (!(isvisited.contains(child.endvertex))) {
                    if(isInQueue.contains(child.endvertex)){
                        queueNodeUCS node=null;
                        Iterator<queueNodeUCS> itr = queue.iterator();
                        while(itr.hasNext()){
                             node = itr.next();
                             if(node.nodeval.equals(child.endvertex))
                                 break;
                        }
                        int countervar = node.uniquecounter;
                        if(node.costdistance>child.distance+popelem.costdistance){
                            queue.remove(node);
                            ArrayList<Path> newlist = new ArrayList(popelem.arrlist);
                            int distance = popelem.costdistance + child.distance;
                            newlist.add(new Path(child.endvertex, distance));
                            queue.offer(new queueNodeUCS(child.endvertex, distance, newlist,countervar));
                        }
                    }
                    else {
                        ArrayList<Path> newlist = new ArrayList(popelem.arrlist);
                        int distance = popelem.costdistance + child.distance;
                        newlist.add(new Path(child.endvertex, distance));
                        queue.offer(new queueNodeUCS(child.endvertex, distance, newlist,globalcounter));
                        globalcounter = globalcounter+1;
                        isInQueue.add(child.endvertex);
                    }
                } else {
                    continue;
                }
            }
        }
    }
    public void doAstar(inputtestcase testcase){

        PriorityQueue<queueNodeAstar> queue = new PriorityQueue<queueNodeAstar>();
        if(testcase.source.equals(testcase.destination)){
            ArrayList<Path> pathlist = new ArrayList();
            pathlist.add(new Path(testcase.source,0));
            printpath(pathlist);
            return;
        }

        String destination = testcase.destination;
        ArrayList<Path> listofpath = new ArrayList();
        listofpath.add(new Path(testcase.source,0));
        int globalcounter =0;
        HashMap<String,Integer> hsmap = testcase.sundaylivetraffic;
        queue.offer(new queueNodeAstar(testcase.source,0,listofpath,globalcounter,0));
        globalcounter = globalcounter+1;
        HashSet<String> isvisited = new HashSet();
        HashSet<String> isInQueue = new HashSet();
        isInQueue.add(testcase.source);
        while(!queue.isEmpty()) {
            queueNodeAstar popelem = queue.poll();
            if (popelem.nodeval.equals(destination)) {
                printpath(popelem.arrlist);
                return;
            }
            isvisited.add(popelem.nodeval);
            ArrayList<graphNode> children = testcase.arrlist.get(popelem.nodeval);
            for (graphNode child : children) {
                if (!(isvisited.contains(child.endvertex))) {
                    if(isInQueue.contains(child.endvertex)){
                        queueNodeAstar node=null;
                        Iterator<queueNodeAstar> itr = queue.iterator();
                        while(itr.hasNext()){
                            node = itr.next();
                            if(node.nodeval.equals(child.endvertex))
                                break;
                        }
                        int countervar = node.uniquecounter;
                        if(node.hueristic>child.distance+popelem.costdistance+hsmap.get(child.endvertex)){
                            queue.remove(node);
                            ArrayList<Path> newlist = new ArrayList(popelem.arrlist);
                            int distance = popelem.costdistance + child.distance;
                            newlist.add(new Path(child.endvertex, distance));
                            queue.offer(new queueNodeAstar(child.endvertex, distance, newlist,countervar,child.distance+popelem.costdistance+hsmap.get(child.endvertex)));
                        }
                    }
                    else {
                        ArrayList<Path> newlist = new ArrayList(popelem.arrlist);
                        int distance = popelem.costdistance + child.distance;
                        newlist.add(new Path(child.endvertex, distance));
                        queue.offer(new queueNodeAstar(child.endvertex, distance, newlist,globalcounter,distance+hsmap.get(child.endvertex)));
                        globalcounter = globalcounter+1;
                        isInQueue.add(child.endvertex);
                    }
                } else {
                    continue;
                }
            }
        }
    }
}
