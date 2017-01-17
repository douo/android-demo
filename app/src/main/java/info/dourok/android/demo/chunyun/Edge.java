package info.dourok.android.demo.chunyun;

/**
 * Created by larry on 12/26/16.
 */
public class Edge {
  public Node from;
  public Node to;
  public double cost;
  public double time;

  public Edge(Node from, Node to, double cost,double time) {
    this.from = from;
    this.to = to;
    this.cost = cost;
    this.time = time;
  }
}
