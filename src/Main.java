import javax.swing.SwingUtilities;
import util.DBConnection;

public class Main {
    public static void main(String[] args) {
        DBConnection.initialize();
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}
