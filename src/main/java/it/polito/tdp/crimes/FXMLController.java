/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Simulazione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	txtResult.clear();
    	int anno=0;
    	anno=boxAnno.getValue();
    	
    	if (anno==0) {
    		txtResult.appendText("Inserisci anno");
    				 return;
    	}
    	model.creaGrafo(anno);
    	for(int i:model.getGrafo().vertexSet()) {
    		txtResult.appendText(String.format("\n\nVicini del distretto %d \n", i));
    		for(Integer e:Graphs.neighborListOf(this.model.getGrafo(), i)) {
    			txtResult.appendText(String.format("Distretto: #%d distanza in Km: %f\n",e,this.model.getGrafo().getEdgeWeight(this.model.getGrafo().getEdge(i, e)) ));
    		}
    	}
    	
    	
    	for(int i=1;i<13;i++) {
    		boxMese.getItems().add(i);
    	}
    	for(int i=1;i<30;i++) {
    		boxGiorno.getItems().add(i);
    	}

    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	int giorno=boxGiorno.getValue();
    	int mese=boxMese.getValue();
    	int NA=Integer.parseInt(txtN.getText());
    	this.model.setGiorno(giorno);
    	this.model.setMese(mese);
    	Simulazione s=new Simulazione();
    	s.setNA(NA);
    	s.init(this.model);
    	s.run();
    	txtResult.appendText("Numero casi mal gestiti: "+s.getCasiMalGestiti());
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().addAll(model.getAnni());
    }
}
