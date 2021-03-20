package SaperPackage.Windows;

import SaperPackage.Boards.MinesweeperBoard;
import SaperPackage.Utilities.Converter;
import SaperPackage.DataModel.Datasource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class GameWindowController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button playAgainButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Label timeLabel;
    private GridPane boardGridPane;
    private MinesweeperBoard minesweeperBoard;
    private static boolean isPause;
    private static boolean isFirstClick = true;
    private static int playAgainFirstSize;
    private static int playAgainSecondSize;
    private static int playAgainMines;
    private Timeline time;
    private long sec = 0;
    private String mode = "easy";

    public void initialize(){
        time = new Timeline(new KeyFrame(Duration.seconds(1), (e) -> {
            timeLabel.setText(Converter.getInstance().convertLongToTimeString(++sec));
            if (mode.compareTo("easy") == 0) {
                if (sec == 60*10) {
                    endOfTheTime();
                }
            } else if (mode.compareTo("medium") == 0) {
                if (sec == 40*60) {
                    endOfTheTime();
                }
            } else if (mode.compareTo("hard") == 0) {
                if (sec == 99*60) {
                    endOfTheTime();
                }
            }
        }));
    }

    public void createGameScene(int firstSize, int secondSize, int mines) {
        playAgainFirstSize = firstSize;
        playAgainSecondSize = secondSize;
        playAgainMines = mines;
        isPause = true;

        if (firstSize == 8 && secondSize == 8) {
            mode = "easy";
        } else if (firstSize == 16 && secondSize == 16) {
            mode = "medium";
        } else if (firstSize == 30 && secondSize == 16) {
            mode = "hard";
        } else {
            mode = "custom";
        }

        minesweeperBoard = new MinesweeperBoard(firstSize, secondSize, mines);
        minesweeperBoard.fillGameBoard();
        borderPane.setCenter(drawBoard(minesweeperBoard));
        playAgainButton.setDisable(true);
        pauseButton.setDisable(true);
    }

    private GridPane drawBoard(MinesweeperBoard saperBoard) {
        setGameBoardProperties();
        double aSize;
        double bSize = 450.0 / (saperBoard.getSecondSize()-1);
        if (mode.compareTo("hard") != 0) aSize = 590.0 / saperBoard.getFirstSize();
        else aSize = 590.0 / (saperBoard.getFirstSize()-1);

        for (int i = 0; i < saperBoard.getSecondSize(); ++i) {
            for (int j = 0; j < saperBoard.getFirstSize(); ++j) {
                StackPane stackPane = new StackPane();
                stackPane = drawEmptyField(stackPane, aSize, bSize);
                if (saperBoard.checkField(j, i) == -200) {
                    stackPane = addMines(stackPane, bSize / 3);
                } else if ((saperBoard.checkField(j, i) >= -99) && (saperBoard.checkField(j, i) <= -92)) {
                    stackPane = addTips(stackPane, saperBoard.checkField(j, i));
                } else {
                    Label label = new Label();
                    stackPane.getChildren().add(label);
                }
                stackPane = hideField(stackPane, aSize, bSize);
                boardGridPane.add(stackPane, j, i);
            }
        }
        return boardGridPane;
    }

    private void setGameBoardProperties() {
        boardGridPane = new GridPane();
        boardGridPane.setVgap(1);
        boardGridPane.setHgap(1);
    }

    private StackPane drawEmptyField(StackPane stackPane, double firstSize, double secondSize) {
        Rectangle rectangle = new Rectangle(firstSize, secondSize);
        rectangle.setFill(Color.WHITE);
        stackPane.getChildren().add(rectangle);
        return stackPane;
    }

    private StackPane addMines(StackPane stackPane, double size) {
        Circle circle = new Circle();
        circle.setRadius(size);
        circle.setFill(Color.BLACK);
        stackPane.getChildren().add(circle);
        return stackPane;
    }

    private StackPane addTips(StackPane stackPane, int code) {
        Label label = new Label();
        if (code == -99) {
            label.setText("1");
            label.setTextFill(Color.RED);
        } else if (code == -98) {
            label.setText("2");
            label.setTextFill(Color.GREEN);
        } else if (code == -97) {
            label.setText("3");
            label.setTextFill(Color.BLUE);
        } else if (code == -96) {
            label.setText("4");
            label.setTextFill(Color.DARKORANGE);
        } else if (code == -95) {
            label.setText("5");
            label.setTextFill(Color.DARKGOLDENROD);
        } else if (code == -94) {
            label.setText("6");
            label.setTextFill(Color.DARKKHAKI);
        } else if (code == -93) {
            label.setText("7");
            label.setTextFill(Color.DARKRED);
        } else if (code == -92) {
            label.setText("8");
            label.setTextFill(Color.DARKBLUE);
        }
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: Times New Roman");
        stackPane.getChildren().add(label);
        return stackPane;
    }

    private StackPane hideField(StackPane stackPane, double firstSize, double secondSize) {
        Rectangle rectangle = new Rectangle(firstSize, secondSize);
        rectangle.setFill(Color.BLUE);
        rectangle.setOnMouseClicked(e -> {
                int xIndex, yIndex;
                rectangle.setFill(Color.TRANSPARENT);
                xIndex = boardGridPane.getColumnIndex(stackPane);
                yIndex = boardGridPane.getRowIndex(stackPane);
                processClick(xIndex, yIndex);
        });
        stackPane.getChildren().add(rectangle);
        return stackPane;
    }

    private void processClick(int firstPosition, int secondPosition) {
        if(isFirstClick){
            pauseButton.setDisable(false);
            time.setCycleCount(Timeline.INDEFINITE);
            time.play();
            isFirstClick = false;
            isPause = false;
        }
        if(isPause){
            isPause = false;
            time.play();
        }
        minesweeperBoard.changeFieldValue(firstPosition, secondPosition);
        refreshGameBoard();
        if (minesweeperBoard.isLoose()) {
            endOfTheGame();
            loseOfTheGame();
        }
        if(minesweeperBoard.isWin()){
            endOfTheGame();
            winOfTheGame();
        }
    }

    private void refreshGameBoard() {
//        minesweeperBoard.printValues();
        ObservableList<Node> nodes = boardGridPane.getChildren();
        Timeline timeline = new Timeline();
        int stepTime = 1;
        StackPane stackPane;
        for (int i = 0; i < minesweeperBoard.getSecondSize(); ++i) {
            for (int j = 0; j < minesweeperBoard.getFirstSize(); ++j) {
                if (minesweeperBoard.checkField(j, i) >= 0) {
                    stackPane = (StackPane) nodes.get(i * minesweeperBoard.getFirstSize() + j);
                    Rectangle rectangle = (Rectangle) stackPane.getChildren().get(2);
                    KeyFrame keyFrame = new KeyFrame(Duration.millis(stepTime * 20), e -> {
                        rectangle.setFill(Color.TRANSPARENT);
                    });
                    timeline.getKeyFrames().add(keyFrame);
                    stepTime++;
                }
            }
        }
        timeline.play();
    }

    private void endOfTheGame(){
        showMines();
        time.stop();
        playAgainButton.setDisable(false);
        isFirstClick = true;
        boardGridPane.setDisable(true);
    }

    private void showMines() {
        ObservableList<Node> nodes = boardGridPane.getChildren();
        StackPane stackPane;
        for (int i = 0; i < minesweeperBoard.getSecondSize(); ++i) {
            for (int j = 0; j < minesweeperBoard.getFirstSize(); ++j) {
                if (minesweeperBoard.checkField(j, i) == -200) {
                    stackPane = (StackPane) nodes.get(i * minesweeperBoard.getFirstSize() + j);
                    Rectangle rectangle = (Rectangle) stackPane.getChildren().get(2);
                    rectangle.setFill(Color.TRANSPARENT);
                }
            }
        }
    }

    private void loseOfTheGame(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of the game");
        alert.setHeaderText(null);
        alert.setContentText("This is the end of this game. If you want to play again, press OK");
        alert.showAndWait();
    }

    private void winOfTheGame(){
        String winnersName = getWinnersName();
        Dialog<ButtonType> dialog = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("winnersWindow.fxml"));

        try{
            DialogPane dialogPane = fxmlLoader.load();
            WinnersWindowController winnersWindow = fxmlLoader.getController();
            if((minesweeperBoard.getFirstSize() == 8) && (minesweeperBoard.getSecondSize() == 8)){
                Datasource.getInstance().insertEasyWinner(winnersName, timeLabel.getText());
                winnersWindow.loadTableView("easy", null);
            }else if((minesweeperBoard.getFirstSize() == 16) && (minesweeperBoard.getSecondSize() == 16)){
                Datasource.getInstance().insertMediumWinner(winnersName, timeLabel.getText());
                winnersWindow.loadTableView("medium", null);
            }else if((minesweeperBoard.getFirstSize() == 30) && (minesweeperBoard.getSecondSize() == 16)){
                Datasource.getInstance().insertHardWinner(winnersName, timeLabel.getText());
                winnersWindow.loadTableView("hard", null);
            }else{
                String type = minesweeperBoard.getFirstSize() + " x " + minesweeperBoard.getSecondSize();
                Datasource.getInstance().insertCustomWinner(winnersName, timeLabel.getText(), type);
                winnersWindow.loadTableView("custom", type);
            }
            dialog.setDialogPane(dialogPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Optional<ButtonType> result = dialog.showAndWait();
        }catch (IOException e){
            System.out.println("Couldn't load winners window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getWinnersName(){
        String name = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("nameGetterWindow.fxml"));
        DialogPane dialogPane = null;

        try{
            dialogPane = fxmlLoader.load();
        }catch (IOException e){
            System.out.println("Couldn't load name getter window: " + e.getMessage());
            e.printStackTrace();
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent()){
            NameGetterWindowController nameGetterWindow = fxmlLoader.getController();
            name = nameGetterWindow.getName();
        }
        return name;
    }

    private void endOfTheTime() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of the game");
        alert.setHeaderText(null);
        alert.setContentText("Time is up!");
        alert.show();
        alert.setOnShowing((event) -> {
            Optional<ButtonType> response = alert.showAndWait();
            if (response.isPresent()) alert.close();
        });
        time.stop();
        showMines();
        playAgainButton.setDisable(false);
        isFirstClick = true;
        boardGridPane.setDisable(true);
    }

    public void handlePlayAgainButton() {
        createGameScene(playAgainFirstSize, playAgainSecondSize, playAgainMines);
        sec = 0;
        timeLabel.setText("00:00");
    }

    public void handleChangeDifficultyButton() {
        time.stop();
        isFirstClick = true;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("mainWindow.fxml"));

        try {
            mainBorderPane.setRight(null);
            mainBorderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Can't load a new scene");
            e.printStackTrace();
        }
    }

    public void handlePauseButton(){
        isPause = true;
        time.stop();
    }
}