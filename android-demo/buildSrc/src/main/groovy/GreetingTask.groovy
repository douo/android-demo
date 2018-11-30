import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

    public class GreetingTask extends DefaultTask {
    @TaskAction
    def greet() {
        println 'hello from GreetingTask'
    }
}
