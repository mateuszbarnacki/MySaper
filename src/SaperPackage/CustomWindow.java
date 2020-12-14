package SaperPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class CustomWindow {
    private int minMines = 10;
    private int maxMines;
    @FXML
    private TextField columnsTextField;
    @FXML
    private TextField rowsTextField;
    @FXML
    private TextField minesTextField;
    @FXML
    private Label minesInfoLabel;

    @FXML
    public void initialize(){
        maxMines = 668;
        columnsTextField.setText("");
        rowsTextField.setText("");
        minesTextField.setText("");
    }

    @FXML
    private void editMinesInfoLabel(MouseEvent mouseEvent){
        if(checkValidation(columnsTextField.getText()) && checkValidation(rowsTextField.getText())){
            int columns, rows;
            columns = Integer.parseInt(columnsTextField.getText());
            rows = Integer.parseInt(rowsTextField.getText());
            maxMines = (columns-1) * (rows-1);
            minesInfoLabel.setText("Enter the number of mines in the range from 10 to " + maxMines + ":");
        }
    }

    private boolean checkValidation(String string){
        boolean isValid = true;
        for(int i = 0; i < string.length(); ++i){
            if(!Character.isDigit(string.charAt(i))){
                isValid = false;
            }
        }
        if(string.length() == 0){
            isValid = false;
        }
        return isValid;
    }

    public SaperBoard processResult(){
        int firstSize, secondSize, mines;
        SaperBoard board = null;
        if(columnsTextField.getText().isEmpty() || rowsTextField.getText().isEmpty() || minesTextField.getText().isEmpty()){
            throw new UnsupportedOperationException("Enter correct data");
        }
        if(checkValidation(columnsTextField.getText()) && checkValidation(rowsTextField.getText()) && checkValidation(minesTextField.getText())){
            firstSize = Integer.parseInt(columnsTextField.getText());
            secondSize = Integer.parseInt(rowsTextField.getText());
            mines = Integer.parseInt(minesTextField.getText());

            if( ((firstSize >= 8) && (firstSize <= 30)) && ((secondSize >= 8) && (secondSize <= 24)) && ((mines >= minMines) && (mines <= maxMines)) ) {
                        board = new SaperBoard(firstSize, secondSize, mines);
            }else{
                throw new UnsupportedOperationException("Can't create this game board");
            }
        } else {
            throw new UnsupportedOperationException("Can't create this game board");
        }
        return board;
    }

    public void editMinesInfoLabelSec(KeyEvent keyEvent) {
        if(checkValidation(columnsTextField.getText()) && checkValidation(rowsTextField.getText())){
            int columns, rows;
            columns = Integer.parseInt(columnsTextField.getText());
            rows = Integer.parseInt(rowsTextField.getText());
            maxMines = (columns-1) * (rows-1);
            minesInfoLabel.setText("Enter the number of mines in the range from 10 to " + maxMines + ":");
        }
    }
}
