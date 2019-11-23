package cloudProject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

   //create datacenter button
   @FXML private Button btnCreateDc;






   /**************************************************************************
    ********************** GRAPHICAL CONTROLS ********************************
    **************************************************************************/





   /***************************************************************************
    ********************************* VARIABLES ********************************
    ***************************************************************************/


   private int numberOfUsers=0, userID=0;
   final ObservableList<String> listItems= FXCollections.observableArrayList();
   final ObservableList<String> CPUCoresList= FXCollections.observableArrayList("Dual-core", "Quad-core", "Octa-core");
   final ObservableList<String> schedulingPoliciesList= FXCollections.observableArrayList("Time shared", "Space shared");
   final ObservableList<String> archList= FXCollections.observableArrayList("X86", "x64");
   final ObservableList<String> dcVmmList= FXCollections.observableArrayList("Xen");





   /***************************************************************************
    ************************ CLOUDSIM VARIABLES *******************************
    ***************************************************************************/


   List<Pe> peList= new ArrayList<Pe>();
   List<Host> hostList = new ArrayList<Host>();











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
      listOfUsers.focusedProperty().addListener(new ChangeListener<Boolean>() {
         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (listOfUsers.isFocused() && numberOfUsers!=0) {
               deleteUser.setDisable(false);
            }else deleteUser.setDisable(true);
         }
      });



      hostNumberOfCores.setItems(CPUCoresList);
      hostNumberOfCores.setValue(CPUCoresList.get(0));
      hostSchedulingPolicy.setItems(schedulingPoliciesList);
      hostSchedulingPolicy.setValue(schedulingPoliciesList.get(0));
      DcArch.setItems(archList);
      DcArch.setValue(archList.get(0));
      DcVmm.setItems(dcVmmList);
      DcVmm.setValue(dcVmmList.get(0));


      numberOfHosts.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,1));
      numberOfHosts.setEditable(true);

      hostMips.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000,100000,1000,1000));
      hostMips.setEditable(true);

      hostRAM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(256,10240,1024,256));
      hostRAM.setEditable(true);

      hostBW.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10000,1000000,10000,1000));
      hostBW.setEditable(true);

      hostStorage.setEditable(true);

      btnCreateDc.setDisable(false);

      //creating datacenter
      btnCreateDc.setOnAction(e -> {

         int nbrOfHosts= numberOfHosts.getValue();
         int hMips= hostMips.getValue();
         int hRam=hostRAM.getValue();
         long hStorage=hostStorage.getValue();
         int hBW=hostBW.getValue();

         String hpesNumber=hostNumberOfCores.getValue();
         String hschedulingPolicy=hostSchedulingPolicy.getValue();

         switch (hpesNumber){
            case "Dual-core":
               for (int i = 0; i <2 ; i++) {
                  peList.add(new Pe(i,new PeProvisionerSimple(hMips)));
               }
               break;
            case "Quad-core":
               for (int i = 0; i <4 ; i++) {
                  peList.add(new Pe(i,new PeProvisionerSimple(hMips)));
               }
               break;
            case "Octa-core" :
               for (int i = 0; i <8 ; i++) {
                  peList.add(new Pe(i,new PeProvisionerSimple(hMips)));
               }
               break;
         }

         switch (hschedulingPolicy){
            case "Time shared":
               for (int i = 0; i <nbrOfHosts ; i++) {
                  hostList.add(new Host(
                        i,
                        new RamProvisionerSimple(hRam),
                        new BwProvisionerSimple(hBW),
                        hStorage,
                        peList,
                        new VmSchedulerTimeShared(peList)
                  ));
               }
               break;
            case "Space shared":
               for (int i = 0; i <nbrOfHosts ; i++) {
                  hostList.add(new Host(
                        i,
                        new RamProvisionerSimple(hRam),
                        new BwProvisionerSimple(hBW),
                        hStorage,
                        peList,
                        new VmSchedulerSpaceShared(peList)
                  ));
               }
               break;
         }

         System.out.println("Hosts created successfully");



      });





   }
}
