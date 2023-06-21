package com.code.fictif.controller;

import com.code.fictif.dao.AffectationDAO;
import com.code.fictif.dao.LieuDAO;
import com.code.fictif.model.Employe;
import com.code.fictif.model.Lieu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

public class AffectationController {
    private Employe affect_empl;

    @FXML
    private AnchorPane modalAffectLabel;

    @FXML
    private Button addAffectButton;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker date_affect;

    @FXML
    private DatePicker date_service;

    @FXML
    private TextField lieu_empl;

    @FXML
    private ComboBox<String> new_lieu;

    @FXML
    private ComboBox<String> new_prov;

    @FXML
    private TextField nom_empl;

    @FXML
    private TextField num_empl;

    @FXML
    private TextField prenoms_empl;

    @FXML
    private TextField posteField;

    @FXML
    void addAffect(ActionEvent event) throws SQLException {
        String num_emp = num_empl.getText();
        String ancien_lieu = lieu_empl.getText();
        String nouveau_lieu = LieuDAO.getOneLieu(new_lieu.getSelectionModel().getSelectedItem(), new_prov.getSelectionModel().getSelectedItem());
        Date date_aff = Date.valueOf(date_affect.getValue());
        Date date_p = Date.valueOf(date_service.getValue());

        AffectationDAO.createAffectation(num_emp, ancien_lieu, nouveau_lieu, date_aff, date_p);
        this.close();
    }

    @FXML
    void cancelAffect(ActionEvent event) {
        this.close();
    }

    public void getEmploye(Employe empl) {
        this.affect_empl = empl;
        num_empl.setText(empl.getNum_empl());
        nom_empl.setText(empl.getNom_empl());
        prenoms_empl.setText(empl.getPrenoms_empl());
        lieu_empl.setText(empl.getLieu());
        posteField.setText(empl.getPoste());
        ObservableList<String> options = FXCollections.observableArrayList("Fianarantsoa", "Antananarivo", "Antsiranana", "Mahajanga", "Toliara", "Toamasina");
        new_prov.getItems().addAll(options);

        new_prov.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadLieu(null);
        });
    }

    public void loadLieu(MouseEvent event) {
        List<String> designations = LieuDAO.getLieuDesignations(new_prov.getSelectionModel().getSelectedItem());
        System.out.println(designations);
        new_lieu.getItems().clear();
        new_lieu.getItems().addAll(designations);
    }

    private void close() {
        Stage stage = (Stage) modalAffectLabel.getScene().getWindow();
        stage.close();
    }
}
