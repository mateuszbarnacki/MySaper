package SaperPackage;

import SaperPackage.DataModel.Datasource;
import SaperPackage.DataModel.TimeResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class WinnersWindow {
    @FXML
    private TableView<TimeResult> resultsTable;

    public void loadTableView(String nameOfTheType, String custom){
        if(nameOfTheType.equalsIgnoreCase("easy")){
            Task<ObservableList<TimeResult>> task = new GetEasyWinnersFromDatabaseTask();
            resultsTable.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
        }else if(nameOfTheType.equalsIgnoreCase("medium")){
            Task<ObservableList<TimeResult>> task = new GetMediumWinnersFromDatabaseTask();
            resultsTable.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
        }else if(nameOfTheType.equalsIgnoreCase("hard")){
            Task<ObservableList<TimeResult>> task = new GetHardWinnersFromDatabaseTask();
            resultsTable.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
        }else{
            Task<ObservableList<TimeResult>> task = new Task<ObservableList<TimeResult>>() {
                @Override
                protected ObservableList<TimeResult> call() throws Exception {
                    return FXCollections.observableArrayList(Datasource.getInstance().queryCustomWinners(custom));
                }
            };
            resultsTable.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
        }

    }
}

class GetEasyWinnersFromDatabaseTask extends Task {
    @Override
    public ObservableList<TimeResult>  call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().queryEasyWinners());
    }
}

class GetMediumWinnersFromDatabaseTask extends Task {
    @Override
    public ObservableList<TimeResult>  call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().queryMediumWinners());
    }
}

class GetHardWinnersFromDatabaseTask extends Task {
    @Override
    public ObservableList<TimeResult>  call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().queryHardWinners());
    }
}