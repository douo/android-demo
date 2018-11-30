package info.dourok.android.demo.chunyun;

import java.util.ArrayList;

/**
 * Created by larry on 12/26/16.
 */

public class Node {
  public String name;
  public ArrayList<Edge> edges;

  public Node(String name) {
    this.name = name;
    edges = new ArrayList<>();
  }

  public void addEdge(Node to, double cost){
    edges.add(new Edge(this,to,cost,0));
  }
}
