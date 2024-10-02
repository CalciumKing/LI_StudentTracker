package com.example.li_studenttracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    // region Variables
    @FXML
    private TextField name_txt, id_txt, id_search;
    @FXML
    private MenuButton grade_dropdown;
    @FXML
    private Label error_lbl;
    @FXML
    private TableColumn<Student, String> name_col, grade_col;
    @FXML
    private TableColumn<Student, Integer> id_col;
    @FXML
    private TableView<Student> table;

    private final ObservableList<Student> students = FXCollections.observableArrayList(
            new Student("Landen", 12, 12345),
            new Student("Steven", 11, 54321),
            new Student("Mason", 9, 13579)
    );
    // endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        grade_col.setCellValueFactory(new PropertyValueFactory<>("grade"));
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        ClearForm();
    }

    // region Form Section
    @FXML
    private void Grade(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        try {
            grade_dropdown.setText(item.getText());
        } catch (Exception ignored) {
            error_lbl.setText("Error Entering Grade");
        }
    }

    @FXML
    private void AddUser() {
        if (ValidForm()) {
            students.add(new Student(name_txt.getText(), Integer.parseInt(grade_dropdown.getText()), Integer.parseInt(id_txt.getText())));
            ClearForm();
        } else
            error_lbl.setText("Please Enter All Necessary Fields,\nAnd Include A Unique ID");
    }

    // endregion
    // region Search, Edit, Delete Section
    @FXML
    private void Search() {
        if (id_search.getText().isEmpty()) {
            table.setItems(students);
            error_lbl.setText("");
            return;
        }

        ObservableList<Student> searchResults = Results();
        if(!ValidSearch(searchResults))
            return;

        table.setItems(searchResults);
        ValidSearch(searchResults);
        error_lbl.setText("Error Making Search Results");
    }

    @FXML
    private void Edit() {
        ObservableList<Student> searchResults = Results();
        if (!ValidSearch(searchResults))
            return;

        if (ValidForm() && searchResults != null) {
            students.removeIf(student -> student == searchResults.getFirst());
            students.add(new Student(name_txt.getText(), Integer.parseInt(grade_dropdown.getText()), Integer.parseInt(id_txt.getText())));

            ClearForm();
        } else
            error_lbl.setText("Please Enter All Necessary Fields,\nAnd Include A Unique ID");
    }

    @FXML
    private void Delete() {
        ObservableList<Student> searchResults = Results();
        if (!ValidSearch(searchResults))
            return;

        if (searchResults != null)
            students.removeIf(student -> student == searchResults.getFirst());
        ClearForm();
    }

    // endregion
    // region Window Settings
    @FXML
    private void Minimize(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void Close() {
        System.exit(0);
    }

    // endregion
    // region Utilities
    private boolean ValidForm() {
        try {
            if (!id_search.getText().isEmpty())
                for (Student student : students)
                    if (student.getId() == Integer.parseInt(id_txt.getText()))
                        return false;

            return !name_txt.getText().isEmpty() && !id_txt.getText().isEmpty() && !grade_dropdown.getText().equals("Grade");
        } catch (Exception ignored) {
            error_lbl.setText("Please Make Sure\nAll Information\nIs Valid");
            return false;
        }
    }

    @FXML
    private void ClearForm() {
        name_txt.clear();
        id_txt.clear();
        id_search.clear();
        grade_dropdown.setText("Grade");
        table.setItems(students);
        error_lbl.setText("");
    }

    private ObservableList<Student> Results() {
        try {
            ObservableList<Student> search = FXCollections.observableArrayList();
            for (Student student : students)
                if (id_search.getText().contains("1234567890") && student.getId() == Integer.parseInt(id_search.getText()))
                    search.add(student);
            return search;
        } catch (Exception ignored) {
            error_lbl.setText("Error Making Search Results");
            return null;
        }
    }

    private boolean ValidSearch(ObservableList<Student> searchResults) {
        if (id_search.getText().isEmpty()) {
            error_lbl.setText("Please Enter ID");
            return false;
        }
        if(searchResults == null) {
            error_lbl.setText("Error Making Search Results");
            return false;
        }

        if (searchResults.size() > 1) {
            error_lbl.setText("Multiple Students\nHave The Same ID");
            return false;
        } else if (searchResults.size() == 1) {
            error_lbl.setText("If Editing\nEnter Correct Info Above\nThen Press Edit");
            return true;
        } else {
            error_lbl.setText("No Student Found\nEnter Full ID\nID Can Only Be Numbers");
            return false;
        }
    }
    // endregion
}