package info.dourok.demo.algo.thread;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class PipeDemo {
    public static void main(String[] args) throws IOException {
        PipedReader reader = new PipedReader();
        PipedWriter writer = new PipedWriter();
        writer.connect(reader);

        writer.write('a');

        char a = (char) reader.read();
        System.out.println(a);
    }
}
