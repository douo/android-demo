package info.dourok.demo.algo;

public class LevesteinDistance {
    public int cal(String x, String y) {
        if (x == null || x.length() == 0) {
            if (y == null || y.length() == 0) {
                return 0;
            } else {
                return y.length();
            }
        }
        return -1;
    }
}
