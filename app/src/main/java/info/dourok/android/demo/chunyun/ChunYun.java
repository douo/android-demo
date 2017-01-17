package info.dourok.android.demo.chunyun;

/**
 * Created by larry on 12/26/16.
 */

public class ChunYun {
  public ChunYun(){
    Node beijin = new Node("北京");
    Node tianjin = new Node("天津");
    Node guagnzhou = new Node("广州");
    Node xiamen = new Node("厦门");
    Node chaoshan = new Node("潮汕");

    beijin.addEdge(tianjin,54.5);
    beijin.addEdge(xiamen,790);
    beijin.addEdge(xiamen,1600);
    beijin.addEdge(xiamen,1600);

    tianjin.addEdge(xiamen,1500);

    xiamen.addEdge(chaoshan,49.5);

  }
}
