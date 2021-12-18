import java.io.IOException;

public class StaticBlock {
    static {
        try {
            Runtime.getRuntime().exec("touch /tmp/touched_by_StaticBlock");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    StaticBlock() throws IOException {
        {
            Runtime.getRuntime().exec("touch /tmp/touched_by_StaticBlock_in_Constructor");
        }
    }
}
