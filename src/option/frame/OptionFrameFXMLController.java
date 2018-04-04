package option.frame;

import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.operations.UnaryOperation;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.xml.transform.TransformerException;
import option.loader.ConnectionPara;
import option.loader.OptionLoader;

public class OptionFrameFXMLController implements Initializable{

    @FXML
    private ListView<ConnectionPara> listConnection;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtIPOne;
    @FXML
    private TextField txtIPTwo;
    @FXML
    private TextField txtIPThree;
    @FXML
    private TextField txtIPFour;
    @FXML
    private TextField txtRackNo;
    @FXML
    private TextField txtSlotNo;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel; 
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAddConnectionList;

    
    OptionLoader loader;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loader = OptionLoader.getInstance();
        initConnectionList();
        initTextfields();
        
        
    }


    @FXML
    void clickAddConnection(MouseEvent event) {
        listConnection.getItems().add(new ConnectionPara("Verbindung " + (listConnection.getItems().size() + 1)));
    }

    @FXML
    void clickDelete(MouseEvent event) {
       listConnection.getItems().remove(listConnection.getSelectionModel().getSelectedItem());
    }

    @FXML
    void clickCancel(MouseEvent event) {
        closeStage();
    }

    @FXML
    void clickSave(MouseEvent event) {
        loader.setConnectionList(listConnection.getItems());
        loader.saveProperty();
        closeStage();
    }
    


    private void closeStage(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void initConnectionList() {
        listConnection.setItems(FXCollections.observableList(loader.getConnectionList()));
        listConnection.getSelectionModel().select(0);
        
        listConnection.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ConnectionPara>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends ConnectionPara> c) {
                ConnectionPara oldPara = c.getRemoved().get(0);
                oldPara = getTextFields();
                ConnectionPara newPara = c.getAddedSubList().get(0);
                setTextFields(newPara);
            }
        });
    }

    private void initTextfields() {
        ConnectionPara para = listConnection.selectionModelProperty().get().getSelectedItem();
        if(para != null){
            setTextFields(para);
        }
        txtIPOne.setTextFormatter   (new TextFormatter<>(ipAddressFilter));
        txtIPTwo.setTextFormatter   (new TextFormatter<>(ipAddressFilter));
        txtIPThree.setTextFormatter (new TextFormatter<>(ipAddressFilter));
        txtIPFour.setTextFormatter  (new TextFormatter<>(ipAddressFilter));
        txtRackNo.setTextFormatter  (new TextFormatter<>(ipAddressFilter));
        txtSlotNo.setTextFormatter  (new TextFormatter<>(ipAddressFilter));
    }
    

    private ConnectionPara getTextFields(){
        ConnectionPara para = new ConnectionPara();
        para.name = txtName.getText();
        
        String tmp = "";
        tmp = tmp + txtIPOne     + ":";
        tmp = tmp + txtIPTwo     + ":";
        tmp = tmp + txtIPThree   + ":";
        para.ip = tmp;
        
        para.rack = Integer.valueOf(txtRackNo.getText());
        
        para.sockel = Integer.valueOf(txtSlotNo.getText());
        
        return para;
    }

    private void setTextFields(ConnectionPara para) {
        String ip = para.ip;
        String[]ipParts = ip.split(":");
        
        txtIPOne.setText(ipParts[0]);
        txtIPTwo.setText(ipParts[1]);
        txtIPThree.setText(ipParts[2]);
        txtIPFour.setText(ipParts[3]); 
        
        txtName.setText(para.name);
        
        txtRackNo.setText(String.valueOf(para.rack));
        
        txtSlotNo.setText(String.valueOf(para.sockel));
        
    }
    
    String regex = makePartialIPRegex();
    final UnaryOperator<TextFormatter.Change> ipAddressFilter = c -> {
        String text = c.getControlNewText();
        if (text.matches(regex)) {
            return c;
        } else {
            return null;
        }
    };
    
    private String makePartialIPRegex() {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))";
        String subsequentPartialBlock = "(\\." + partialBlock + ")";
        String ipAddress = partialBlock + "?" + subsequentPartialBlock + "{0,3}";
        return "^" + ipAddress;
    }
}

