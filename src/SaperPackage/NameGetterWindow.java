package SaperPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NameGetterWindow {
    @FXML
    private TextField nameGetter;
    @FXML
    private Label label;

    public String getName(){
        return nameGetter.getText();
    }

    public void validateField(MouseEvent mouseEvent) {
        if(nameGetter.getText().isEmpty()){
            label.setVisible(true);
        }else{
            label.setVisible(false);
        }
    }
}
