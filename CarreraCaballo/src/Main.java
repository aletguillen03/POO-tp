import javax.swing.SwingUtilities;
import vista.VistaCarreras;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaCarreras vista = new VistaCarreras();
            vista.setVisible(true);
        });
    }
}
