package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.cloudbus.cloudsim.Vm;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
   /**************************************************************************
    ********************** GRAPHICAL COMPONENTS ******************************
    **************************************************************************/
   //users
   @FXML private Button deleteUser;
   @FXML private Button addUser;
   @FXML private ListView<String> listOfUsers;

   //create hosts
   @FXML private Spinner numberOfHosts;
   @FXML private Spinner hostMips;
   @FXML private ChoiceBox hostNumberOfCores;
   @FXML private Spinner hostRAM;
   @FXML private Spinner hostStorage;
   @FXML private Spinner hostBW;
   @FXML private ChoiceBox hostSchedulingPolicy;


   //Datacenter Characteristics
   @FXML private ChoiceBox DcArch;
   @FXML private TextField DcOS;
   @FXML private ChoiceBox DcVmm;
   @FXML private TextField timeZone;
   @FXML private TextField DcCost;
   @FXML private TextField DcCostPerRAM;
   @FXML private TextField DcCostPerStorage;
   @FXML private TextField DcCostPerBandwidth;
   @FXML private Button btnCreateDc;



   /**************************************************************************
    ********************** GRAPHICAL CONTROLS ********************************
    **************************************************************************/






   /***************************************************************************
    ********************************* VARIABLES ********************************
    ***************************************************************************/




   int numberOfUsers=0, userID=0;
   final ObservableList<String> listItems= FXCollections.observableArrayList();



   /***************************************************************************
    ************************ CLOUDSIM VARIABLES *******************************
    ***************************************************************************/

   int hostID=0;
   int hMips= (int) hostMips.getValue();
   int hRAM= (int) hostRAM.getValue();
   long hStorage= (long) hostStorage.getValue();
   int hBW= (int) hostBW.getValue();











   /***************************************************************************
    ************************ CLOUDSIM METHODS *********************************
    ***************************************************************************/

   /*************** create new user ***************/
   public void addUser() {
      numberOfUsers++;
      userID++;
      listItems.add("user"+userID);
   }

   /*************** delete user ***************/
   public void deleteUser() {
      int selectedItem = listOfUsers.getSelectionModel().getSelectedIndex();
      listItems.remove(selectedItem);
      numberOfUsers--;
      System.out.println(numberOfUsers);
   }



   @Override
   public void initialize(URL location, ResourceBundle resources) {

      listOfUsers.setItems(listItems);
      deleteUser.setDisable(true);
     // createDatacenterArea.setDisable(false);

      //disable other section if there is no user
      listOfUsers.focusedProperty().addListener(new ChangeListener<Boolean>() {
         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (listOfUsers.isFocused() && numberOfUsers!=0) {
               deleteUser.setDisable(false);
            }else deleteUser.setDisable(true);
         }
      });

   }
}
