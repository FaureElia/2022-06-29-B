/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
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

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	Album a1=this.cmbA1.getValue();
    	if(a1==null) {
    		this.txtResult.setText("inserire un album");
    		return;
    	}
    	List<Album> successori=this.model.getAdiacenze(a1);
    	this.txtResult.setText("SUCCESSORI:\n ");
    	for(Album a :successori) {
    		this.txtResult.appendText(a.getTitle()+", bilancio : "+a.getBilancio()+"\n");
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Album a1=this.cmbA1.getValue();
    	Album a2=this.cmbA2.getValue();
    	String xS=this.txtX.getText();
    	if(a1==null ||a2==null ||xS==null) {
    		this.txtResult.setText("inserire valori corretti");
    		return;
    	}
    	try {
    		Double x=Double.parseDouble(xS);
    		List<Album> percorsoMigliore=this.model.calcolaPercorso(a1,a2,x);
    		this.txtResult.setText("trovato percorso migliore: \n");
    		for(Album a: percorsoMigliore) {
    			this.txtResult.appendText(a+", "+a.getBilancio()+"\n");
    		}
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("inserire valori numerici");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.cmbA1.getItems().clear();
    	this.cmbA2.getItems().clear();
    	String nS=this.txtN.getText();
    	if(nS=="") {
    		this.txtResult.setText("inserire un valore");
    		return;
    	}
    	try {
    		double n=Double.parseDouble(nS);
    		List<Album> lista=this.model.creaGrafo(n);
    		this.cmbA1.getItems().addAll(lista);
    		this.cmbA2.getItems().addAll(lista);
    		
    		
    	}catch(NumberFormatException e){
    		this.txtResult.setText("inserire un numero");	
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
