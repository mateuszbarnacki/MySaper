package SaperPackage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class MainWindowController {
    @FXML
    private BorderPane borderPane;

    public void handleFirstButton(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("secondWindow.fxml"));
        try {
            borderPane.setCenter(fxmlLoader.load());
        }catch (IOException e){
            e.printStackTrace();
        }

        SecondWindowController controller = fxmlLoader.getController();
        controller.createGameScene(8, 8, 10);
    }

    public void handleSecondButton(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("secondWindow.fxml"));
        try {
            borderPane.setCenter(fxmlLoader.load());
        }catch (IOException e){
            e.printStackTrace();
        }

        SecondWindowController controller = fxmlLoader.getController();
        controller.createGameScene(16, 16, 40);
    }

    public void handleThirdButton(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("secondWindow.fxml"));
        try {
            borderPane.setCenter(fxmlLoader.load());
        }catch (IOException e){
            e.printStackTrace();
        }

        SecondWindowController controller = fxmlLoader.getController();
        controller.createGameScene(30, 16, 99);
    }

    public void handleFourthButton(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("customWindow.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Board creator");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.initOwner(borderPane.getScene().getWindow());

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("Can't load the scene");
            e.printStackTrace();
        }

        Optional<ButtonType> result = dialog.showAndWait();
        if((result.isPresent()) && (result.get() == ButtonType.OK)){
            try{
                CustomWindow controller = fxmlLoader.getController();
                SaperBoard saperBoard = controller.processResult();
                try{
                    FXMLLoader secFXMLLoader = new FXMLLoader();
                    secFXMLLoader.setLocation(getClass().getResource("secondWindow.fxml"));
                    borderPane.setCenter(secFXMLLoader.load());

                    SecondWindowController secondWindowController = secFXMLLoader.getController();
                    secondWindowController.createGameScene(saperBoard.getFirstSize(), saperBoard.getSecondSize(), saperBoard.getMines());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (UnsupportedOperationException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Board creation error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                Optional<ButtonType> res = alert.showAndWait();
                if(res.isPresent()){
                    alert.close();
                }
            }catch (NullPointerException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Board creation error");
                alert.setHeaderText(null);
                alert.setContentText("Enter the correct data");
                Optional<ButtonType> res = alert.showAndWait();
                if(res.isPresent()){
                    alert.close();
                }
            }

        }else{
            dialog.close();
        }
    }

}