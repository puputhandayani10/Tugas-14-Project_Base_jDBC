package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main_menu.fxml"));
        Scene scene = new Scene(root); // Otomatis sesuai konten FXML

        primaryStage.setTitle("Toko Kelontong Uhuy");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.centerOnScreen();    // ✅ Tampil di tengah layar
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // 🔥 Ini akan manggil start()
    }
}
