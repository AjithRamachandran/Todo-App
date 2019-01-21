package Todo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static javafx.application.Platform.exit;

public class Controller {

    @FXML
    private ListView<String> listView = new ListView<>();

    @FXML
    private TextField textField, search;

    private boolean firstTime = true;

    private ArrayList<String> todoList = new ArrayList<>();
    private ArrayList<String> words;
    private ArrayList<String> searchResult = new ArrayList<>();

    private String fileName = "src/Todo/list.txt";

    public Controller() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String items = reader.readLine();
        reader.close();
        words = toArray(items + ',');
    }

    private void save() throws IOException {
        String joined = listView.getItems().stream().map(Object::toString).collect(Collectors.joining(","));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(joined);
        writer.close();
    }

    private ArrayList<String> toArray(@NotNull String string) {
        StringBuilder name = new StringBuilder();
        ArrayList<String> wordArray = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if(c !=  ',') {
                name.append(c);
            }
            else {
                wordArray.add(name.toString());
                name = new StringBuilder();
            }
        }
        return wordArray;
    }

    @FXML
    private void initialize(){
        for(String item: words)
            listView.getItems().add(item);
    }

    @FXML
    private void addBtn() {
        String item = textField.getText();
        if (item.equals("")) {
            if (firstTime) {
                firstTime = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Enter Something");
                alert.showAndWait();
            }
        } else {
            todoList.add(item);
            listView.getItems().add(item);
            textField.setText("");
        }
    }

    @FXML
    void removeItem() {
        String item = listView.getSelectionModel().getSelectedItem();
        listView.getItems().remove(item);
    }

    @FXML
    private void exitTodo() throws IOException {
        save();
        exit();
    }

    @FXML
    private void search() {
        String keyWord = search.getText().toLowerCase();
        for(String word: words){
            if(word.toLowerCase().contains(keyWord)){
                searchResult.add(word);
                System.out.println(word);
            }
        }
        listView.getItems().clear();
        for(String item: searchResult)
            listView.getItems().add(item);
        searchResult.clear();
    }
}