package com.code.fictif.controller;

import com.code.fictif.dao.EmployeDAO;
import com.code.fictif.dao.LieuDAO;
import com.code.fictif.model.Employe;
import com.code.fictif.model.Lieu;
import com.code.fictif.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MainController extends Database {

    // EMPLOYE

    @FXML
    private TableView<Employe> tableEmploye;

    private TableColumn<Employe, String> numEmplCol = new TableColumn<>("Numéro");

    private TableColumn<Employe, String> civiliteCol = new TableColumn<>("Civilité");
    private TableColumn<Employe, String> nomCol = new TableColumn<>("Nom");
    private TableColumn<Employe, String> prenomsCol = new TableColumn<>("Prénoms");
    private TableColumn<Employe, String> mailCol = new TableColumn<>("Mail");
    private TableColumn<Employe, String> posteCol = new TableColumn<>("Poste");
    private TableColumn<Employe, String> lieuCol = new TableColumn<>("Lieu");

    private String search = null;

    @FXML
    private ComboBox<String> lieuCombo;

    @FXML
    private ComboBox<String> provinceComboEmploye;

    @FXML
    private TextField addMail;

    @FXML
    private TextField addNom;

    @FXML
    private TextField addPost;

    @FXML
    private TextField addPrenoms;

    @FXML
    private ComboBox<String> civiliteCombo;

    @FXML
    private TextField searchField;

    @FXML
    public void setSearch(KeyEvent event) {

    }

    private String tmpNumEmploye;

    //FIN EMPLOYE

    // DEBUT LIEU

    @FXML
    private TableView<Lieu> tableLieu;

    private TableColumn<Lieu, String> idLieuCol = new TableColumn<>("Identifiant");

    private TableColumn<Lieu, String> designCol = new TableColumn<>("Désignation");

    private TableColumn<Lieu, String> provinceCol = new TableColumn<>("Province");

    @FXML
    private TextField designField;

    @FXML
    private ComboBox<String> provinceCombo;

    private String tmpIdLieu;

    // FIN LIEU


    // DEBUT METHODE EMPLOYE
    @FXML
    public void handleAddEmploye(MouseEvent event) {
        String nom = addNom.getText();
        String prenoms = addPrenoms.getText();
        String civilite = civiliteCombo.getSelectionModel().getSelectedItem();
        String poste = addPost.getText();
        String lieu = LieuDAO.getOneLieu(lieuCombo.getSelectionModel().getSelectedItem(), provinceComboEmploye.getSelectionModel().getSelectedItem());
        String mail = addMail.getText();
        if (nom.isEmpty() || prenoms.isEmpty() || poste.isEmpty() || lieu.isEmpty() || mail.isEmpty() || civilite.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs vides", "Veuillez remplir tous les champs !");
        }
         else{
             EmployeDAO createDAO = new EmployeDAO();
             createDAO.createEmploye(nom, prenoms, poste, civilite, lieu, mail);
             setTableEmploye();

            addNom.setText("");
            addPrenoms.setText("");
            addPost.setText("");
            lieuCombo.getSelectionModel().select(null);
            addMail.setText("");
            civiliteCombo.getSelectionModel().select(null);
        }
    }

    @FXML
    public void updateEmploye(MouseEvent event) {
        Employe selectedEmploye = tableEmploye.getSelectionModel().getSelectedItem();
        if (selectedEmploye == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à éditer.");
            return;
        }

        addNom.setText(selectedEmploye.getNom_empl());
        addPrenoms.setText(selectedEmploye.getPrenoms_empl());
        addPost.setText(selectedEmploye.getPoste());
        addMail.setText(selectedEmploye.getMail());
        civiliteCombo.getSelectionModel().select(selectedEmploye.getCivilite());
        lieuCombo.getSelectionModel().select(LieuDAO.findLieu(selectedEmploye.getLieu()));
        tmpNumEmploye = selectedEmploye.getNum_empl();
    }

    @FXML
    public void confirmUpdateEmploye(MouseEvent event) {
        String nom = addNom.getText();
        String prenoms = addPrenoms.getText();
        String civilite = civiliteCombo.getSelectionModel().getSelectedItem();
        String poste = addPost.getText();
        String lieu = LieuDAO.getOneLieu(lieuCombo.getSelectionModel().getSelectedItem(), provinceComboEmploye.getSelectionModel().getSelectedItem());
        String mail = addMail.getText();
        if (nom.isEmpty() || prenoms.isEmpty() || poste.isEmpty() || lieu.isEmpty() || mail.isEmpty() || civilite.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs vides", "Veuillez remplir tous les champs !");
        }
        else{
            EmployeDAO createDAO = new EmployeDAO();
            createDAO.updateUser(tmpNumEmploye, nom, prenoms, poste, civilite, lieu, mail);
            setTableEmploye();

            addNom.setText("");
            addPrenoms.setText("");
            addPost.setText("");
            lieuCombo.getSelectionModel().select(null);
            addMail.setText("");
            civiliteCombo.getSelectionModel().select(null);
        }
    }

    @FXML
    public void deleteEmploye(MouseEvent event) {
        Employe selectedEmploye = tableEmploye.getSelectionModel().getSelectedItem();
        if (selectedEmploye == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à supprimer.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Suppression de l'employé");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'employé sélectionné ?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            EmployeDAO employeDAO = new EmployeDAO();
            employeDAO.deleteUser(selectedEmploye.getNum_empl());
            setTableEmploye();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Employé supprimé", "L'employé a été supprimé avec succès.");
        }
    }

    public void setTableEmploye() {
        numEmplCol.setCellValueFactory(new PropertyValueFactory<>("num_empl"));
        civiliteCol.setCellValueFactory(new PropertyValueFactory<>("civilite"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom_empl"));
        prenomsCol.setCellValueFactory(new PropertyValueFactory<>("prenoms_empl"));
        mailCol.setCellValueFactory(new PropertyValueFactory<>("mail"));
        posteCol.setCellValueFactory(new PropertyValueFactory<>("poste"));
        lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        EmployeDAO tmpDAO = new EmployeDAO();
        List<Employe> employes = tmpDAO.all();


        if (tableEmploye.getColumns().isEmpty()) {
            tableEmploye.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableEmploye.getColumns().addAll(numEmplCol, civiliteCol, nomCol, prenomsCol, mailCol, posteCol, lieuCol);
        }

        tableEmploye.getItems().setAll(employes);
    }

    public void loadLieu(MouseEvent event) {
        List<String> designations = LieuDAO.getLieuDesignations(provinceComboEmploye.getSelectionModel().getSelectedItem());
        System.out.println(designations);
        lieuCombo.getItems().clear();
        lieuCombo.getItems().addAll(designations);
    }

    // FIN METHODE EMPLOYE


    // DEBUT METHODE LIEU

    public void setTableLieu() {
        idLieuCol.setCellValueFactory(new PropertyValueFactory<>("id_lieu"));
        designCol.setCellValueFactory(new PropertyValueFactory<>("design"));
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        LieuDAO tmpDAO = new LieuDAO();
        List<Lieu> lieux = tmpDAO.all();


        if (tableLieu.getColumns().isEmpty()) {
            tableLieu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableLieu.getColumns().addAll(idLieuCol, designCol, provinceCol);
        }

        tableLieu.getItems().setAll(lieux);
    }

    @FXML
    public void handleAddLieu(MouseEvent event) {
        String designFieldText = designField.getText();
        String province = provinceCombo.getSelectionModel().getSelectedItem();
        if (designFieldText.isEmpty() || province.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs vides", "Veuillez remplir tous les champs !");
        } else {
            LieuDAO createDAO = new LieuDAO();
            createDAO.createLieu(designFieldText, province);
            setTableLieu();

            designField.setText("");
            provinceCombo.getSelectionModel().select(null);
        }
    }

    @FXML
    public void updateLieu(MouseEvent event) {
        Lieu selectedLieu = tableLieu.getSelectionModel().getSelectedItem();
        if (selectedLieu == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à éditer.");
            return;
        }

        designField.setText(selectedLieu.getDesign());
        provinceCombo.getSelectionModel().select(selectedLieu.getProvince());
        tmpIdLieu = selectedLieu.getId_lieu();
    }

    @FXML
    public void confirmUpdateLieu(MouseEvent event) {
        String designFieldText = designField.getText();
        String province = provinceCombo.getSelectionModel().getSelectedItem();
        if (designFieldText.isEmpty() || province.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs vides", "Veuillez remplir tous les champs !");
        }
        else{
            LieuDAO updateDAO = new LieuDAO();
            updateDAO.updateLieu(tmpIdLieu, designFieldText, province);
            setTableLieu();

            designField.setText("");
            provinceCombo.getSelectionModel().select(null);
        }
    }

    @FXML
    public void deleteLieu(MouseEvent event) {
        Lieu selectedLieu = tableLieu.getSelectionModel().getSelectedItem();
        if (selectedLieu == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à supprimer.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Suppression de l'employé");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'employé sélectionné ?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LieuDAO lieuDAO = new LieuDAO();
            lieuDAO.deleteLieu(selectedLieu.getId_lieu());
            setTableLieu();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Employé supprimé", "L'employé a été supprimé avec succès.");
        }
    }


    // FIN METHODE LIEU

    public void initialize(){

        ObservableList<String> options = FXCollections.observableArrayList("Mlle", "Mme", "Mr");
        civiliteCombo.getItems().addAll(options);

        options = FXCollections.observableArrayList("Fianarantsoa", "Antananarivo", "Antsiranana", "Mahajanga", "Toliara", "Toamasina");
        provinceComboEmploye.getItems().addAll(options);
        provinceCombo.getItems().addAll(options);

        provinceComboEmploye.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadLieu(null);
        });

        setTableEmploye();
        setTableLieu();
    }

    public static void showAlert(Alert.AlertType alertType, String erreur, String champsVides, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(erreur);
        alert.setHeaderText(champsVides);
        alert.setContentText(s);
        alert.showAndWait();
    }
}



