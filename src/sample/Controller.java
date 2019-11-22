package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
   @FXML private Spinner<Integer> numberOfHosts;
   @FXML private Spinner<Integer> hostMips;
   @FXML private ChoiceBox<String> hostNumberOfCores;
   @FXML private Spinner<Integer> hostRAM;
   @FXML private Spinner<Long> hostStorage;
   @FXML private Spinner<Integer> hostBW;
   @FXML private ChoiceBox<String> hostSchedulingPolicy;


   //Datacenter Characteristics
   @FXML private ChoiceBox<String> DcArch;
   @FXML private TextField DcOS;
   @FXML private ChoiceBox<String> DcVmm;
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
   final ObservableList<String> CPUCoresList= FXCollections.observableArrayList("Dual-core", "Quad-core", "Octa-core");
   final ObservableList<String> schedulingPoliciesList= FXCollections.observableArrayList("Time shared", "Space shared");
   final ObservableList<String> archList= FXCollections.observableArrayList("X86", "x64");
   final ObservableList<String> dcVmmList= FXCollections.observableArrayList("Xen");





   /***************************************************************************
    ************************ CLOUDSIM VARIABLES *******************************
    ***************************************************************************/

   /*
   int hostID=0;
   int hMips= hostMips.getValue();
   int hRAM= hostRAM.getValue();
   long hStorage= hostStorage.getValue();
   int hBW= hostBW.getValue();

    */











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

      //disable other section if there is no user
      /*
      listOfUsers.focusedProperty().addListener(new ChangeListener<Boolean>() {
         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (listOfUsers.isFocused() && numberOfUsers!=0) {
               deleteUser.setDisable(false);
            }else deleteUser.setDisable(true);
         }
      });

      */

      hostNumberOfCores.setItems(CPUCoresList);
      hostSchedulingPolicy.setItems(schedulingPoliciesList);
      DcArch.setItems(archList);
      DcVmm.setItems(dcVmmList);


      numberOfHosts.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,1));
      numberOfHosts.setEditable(true);

      hostMips.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000,100000,1000,1000));
      hostMips.setEditable(true);

      hostRAM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(256,10240,1024,256));
      hostRAM.setEditable(true);

      hostStorage.setValueFactory(new SpinnerValueFactory<Long>() {

         @Override
         public void decrement(int steps) {
            steps-=1000;
         }

         @Override
         public void increment(int steps) {
            steps+=1000;
         }
      });





   }
}
