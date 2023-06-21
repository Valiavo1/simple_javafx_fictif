package com.code.fictif.controller;

import com.code.fictif.dao.AffectationDAO;
import com.code.fictif.dao.EmployeDAO;
import com.code.fictif.dao.LieuDAO;
import com.code.fictif.model.Affectation;
import com.code.fictif.model.Employe;
import com.code.fictif.model.Lieu;
import com.code.fictif.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
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

    private String tmpNumEmploye;

    private Boolean boolAffect = false;

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

    // DEBUT AFFECTATION
    @FXML
    private Button updateAffButtonConfirm;

    @FXML
    private TextField ancien_lieu;

    @FXML
    private TextField searchAffEmploye;

    @FXML
    private DatePicker date_affectAff;

    @FXML
    private DatePicker date_serviceAff;

    @FXML
    private TextField num_emplAff;

    @FXML
    private ComboBox<String> new_lieu;

    @FXML
    private ComboBox<String> new_prov;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;


    @FXML
    private TableView<Affectation> tableAffectation;

    private TableColumn<Affectation, String> numAffectCol = new TableColumn<>("N° Affectation");
    private TableColumn<Affectation, String> numEmployeCol = new TableColumn<>("N° Employé");
    private TableColumn<Affectation, String> nomEmployeCol = new TableColumn<>("Nom Employé");
    private TableColumn<Affectation, String> prenomsEmployeCol = new TableColumn<>("Prénoms Employé");
    private TableColumn<Affectation, String> ancienLieuCol = new TableColumn<>("Ancien Lieu");
    private TableColumn<Affectation, String> nouveauLieuCol = new TableColumn<>("Nouveau Lieu");
    private TableColumn<Affectation, Date> dateAffectCol = new TableColumn<>("Date d'affectation");
    private TableColumn<Affectation, Date> datePriseCol = new TableColumn<>("Date de prise de service");

    private String tmpNumAffect;

    // FIN AFFECTATION


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

    public void affectEmploye(ActionEvent actionEvent) throws IOException {
        Employe selectedEmploye = tableEmploye.getSelectionModel().getSelectedItem();
        if (selectedEmploye == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à éditer.");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/affectModal.fxml"));
            Parent parent = fxmlLoader.load();
            AffectationController tmpController = fxmlLoader.getController();

            tmpController.getEmploye(selectedEmploye);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Affecter un employé");
            modalStage.setScene(new Scene(parent));
            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
        List<Employe> employes = new ArrayList<>();;
        if (this.boolAffect) {
            employes = tmpDAO.getAllAffectationNotAffected(search);
        } else {
            employes = tmpDAO.all(search);
        }

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

    public void nonAffectEmploye() {
        this.boolAffect = ! this.boolAffect;
        setTableEmploye();
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
        confirmationAlert.setHeaderText("Suppression d'un lieu");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer le lieu sélectionné ?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LieuDAO lieuDAO = new LieuDAO();
            lieuDAO.deleteLieu(selectedLieu.getId_lieu());
            setTableLieu();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Lieu supprimé", "Le lieu a été supprimé avec succès.");
        }
    }


    // FIN METHODE LIEU

    // DEBUT METHODE AFFECTATION

    public void setTableAffectation() {
        numAffectCol.setCellValueFactory(new PropertyValueFactory<>("num_affect"));
        numEmployeCol.setCellValueFactory(new PropertyValueFactory<>("num_empl"));
        nomEmployeCol.setCellValueFactory(new PropertyValueFactory<>("nom_empl"));
        prenomsEmployeCol.setCellValueFactory(new PropertyValueFactory<>("prenoms_empl"));
        ancienLieuCol.setCellValueFactory(new PropertyValueFactory<>("ancien_lieu"));
        nouveauLieuCol.setCellValueFactory(new PropertyValueFactory<>("nouveau_lieu"));
        dateAffectCol.setCellValueFactory(new PropertyValueFactory<>("date_affect"));
        datePriseCol.setCellValueFactory(new PropertyValueFactory<>("date_priseservice"));

        Date deb = null;
        Date fin = null;

        String searchD = "";

        if (searchField.getText() != null) {
            searchD = searchField.getText();
        }

        if (dateDebut.getValue() != null && dateFin.getValue() != null) {
            deb = Date.valueOf(dateDebut.getValue());
            fin = Date.valueOf(dateFin.getValue());
        } else if (dateDebut.getValue() != null && dateFin.getValue() == null) {
            deb = Date.valueOf(dateDebut.getValue());
        } else if (dateDebut.getValue() == null && dateFin.getValue() != null) {
            fin = Date.valueOf(dateFin.getValue());
        }

        List<Affectation> affects = AffectationDAO.all(searchD, deb, fin);

        if (tableAffectation != null) {
            if (tableAffectation.getColumns().isEmpty()) {
                tableAffectation.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableAffectation.getColumns().addAll(numAffectCol, numEmployeCol, nomEmployeCol, prenomsEmployeCol, ancienLieuCol, nouveauLieuCol,dateAffectCol, datePriseCol);
            }

            tableAffectation.getItems().setAll(affects);
        }
    }

    @FXML
    void changeDebDate(ActionEvent event) {
        setTableAffectation();
    }

    @FXML
    void changeFinDate(ActionEvent event) {
        setTableAffectation();
    }

    @FXML
    void updateAffect(ActionEvent event) {
        Affectation selectedAffect = tableAffectation.getSelectionModel().getSelectedItem();
        if (selectedAffect == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à éditer.");
            return;
        }

        this.tmpNumAffect = selectedAffect.getNum_affect();
        num_emplAff.setText(selectedAffect.getNum_empl());
        ancien_lieu.setText(selectedAffect.getAncien_lieu());
        new_prov.getSelectionModel().select(LieuDAO.getProvince(selectedAffect.getNouveau_lieu()));
        new_lieu.getSelectionModel().select(LieuDAO.findLieu(selectedAffect.getNouveau_lieu()));
        date_affectAff.setValue(selectedAffect.getDate_affect().toLocalDate());
        date_serviceAff.setValue(selectedAffect.getDate_priseservice().toLocalDate());
    }

    public void loadLieuAff(MouseEvent event) {
        List<String> designations = LieuDAO.getLieuDesignations(new_prov.getSelectionModel().getSelectedItem());
        System.out.println(designations);
        new_lieu.getItems().clear();
        new_lieu.getItems().addAll(designations);
    }

    @FXML
    void confirmUpdateAff(ActionEvent event) {
        Date date_affect = Date.valueOf(date_affectAff.getValue());
        Date date_serv = Date.valueOf(date_serviceAff.getValue());

        AffectationDAO.updateAffectation(tmpNumAffect, num_emplAff.getText(), ancien_lieu.getText(), LieuDAO.getOneLieu(new_lieu.getSelectionModel().getSelectedItem(), new_prov.getSelectionModel().getSelectedItem()), date_affect, date_serv);
        setTableAffectation();
        System.out.println("confirmUpdateAff");
    }

    @FXML
    void deleteAff(ActionEvent event) {
        Affectation selectedAffect = tableAffectation.getSelectionModel().getSelectedItem();
        if (selectedAffect == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun employé sélectionné", "Veuillez sélectionner un employé à éditer.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Suppression d'une affectation");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'affectation sélectionnée ?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            AffectationDAO.deleteAffectation(selectedAffect.getNum_affect());
            setTableAffectation();
            showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Affectation supprimée", "L'affectation a été supprimé avec succès.");
        }
    }

    // FIN METHODE AFFECTATION

    public void initialize(){

        ObservableList<String> options = FXCollections.observableArrayList("Mlle", "Mme", "Mr");
        civiliteCombo.getItems().addAll(options);

        options = FXCollections.observableArrayList("Antananarivo", "Fianarantsoa", "Antsiranana", "Mahajanga", "Toliara", "Toamasina");
        provinceComboEmploye.getItems().addAll(options);
        provinceCombo.getItems().addAll(options);
        new_prov.getItems().addAll(options);

        provinceComboEmploye.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadLieu(null);
        });

        new_prov.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadLieuAff(null);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            search = searchField.getText();
            setTableEmploye();
            System.out.println(search);
        });

        searchAffEmploye.textProperty().addListener((observable, oldValue, newValue) -> {
            search = searchAffEmploye.getText();
            setTableAffectation();
            System.out.println(search);
        });

        setTableEmploye();
        setTableLieu();
        setTableAffectation();
    }

    public static void showAlert(Alert.AlertType alertType, String erreur, String champsVides, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(erreur);
        alert.setHeaderText(champsVides);
        alert.setContentText(s);
        alert.showAndWait();
    }
}



