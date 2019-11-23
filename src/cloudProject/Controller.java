package cloudProject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
   @FXML private Spinner<Integer> inputNumberOfHosts;
   @FXML private Spinner<Integer> inputHostMips;
   @FXML private ChoiceBox<String> inputHostNumberOfCores;
   @FXML private Spinner<Integer> inputHostRAM;
   @FXML private Spinner<Double> inputHostStorage;
   @FXML private Spinner<Integer> inputHostBW;
   @FXML private ChoiceBox<String> inputHostSchedulingPolicy;
   @FXML private Button btnCreateHosts;
   @FXML private Text textShowNumberOfHosts;


   //Datacenter Characteristics
   @FXML private ChoiceBox<String> inputDcArch;
   @FXML private TextField inputDcOS;
   @FXML private ChoiceBox<String> inputDcVmm;
   @FXML private TextField inputTimeZone;
   @FXML private Spinner<Double> inputDcCost;
   @FXML private Spinner<Double> inputDcCostPerRAM;
   @FXML private Spinner<Double> inputDcCostPerBandwidth;
   @FXML private Spinner<Double> inputDcCostPerStorage;

   //create datacenter button
   @FXML private Button btnCreateDc;






   /**************************************************************************
    ********************** GRAPHICAL CONTROLS ********************************
    **************************************************************************/





   /***************************************************************************
    ********************************* VARIABLES ********************************
    ***************************************************************************/


   private int numberOfUsers=0, userID=0,dcID=0, hostID=0;

   final ObservableList<String> listItems= FXCollections.observableArrayList();
   final ObservableList<String> CPUCoresList= FXCollections.observableArrayList("Dual-core", "Quad-core", "Octa-core");
   final ObservableList<String> schedulingPoliciesList= FXCollections.observableArrayList("Time shared", "Space shared");
   final ObservableList<String> archList= FXCollections.observableArrayList("x86", "x64");
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

   Host createHost(){
      int hMips= inputHostMips.getValue();
      int hRam= inputHostRAM.getValue();
      double hStorage= inputHostStorage.getValue();
      int hBW= inputHostBW.getValue();

      String hpesNumber= inputHostNumberOfCores.getValue();
      String hschedulingPolicy= inputHostSchedulingPolicy.getValue();

      peList.clear();
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
      Host host=null;
      switch (hschedulingPolicy){
         case "Time shared":

               host =new Host(
                     hostID,
                     new RamProvisionerSimple(hRam),
                     new BwProvisionerSimple(hBW),
                     (long)hStorage,
                     peList,
                     new VmSchedulerTimeShared(peList) );
               System.out.println("Host created successfully with time shared policy");

            break;
         case "Space shared":

               host=new Host(
                     hostID,
                     new RamProvisionerSimple(hRam),
                     new BwProvisionerSimple(hBW),
                     (long)hStorage,
                     peList,
                     new VmSchedulerSpaceShared(peList)
               );
               System.out.println("Host  created successfully with storage shared policy");

            break;
      }


      return host;
   }


   public Datacenter createDatacenter(){

      //create datacenter
      String dcName= "Datacenter"+dcID;
      String sysArch= inputDcArch.getValue();
      String OS= inputDcOS.getText();
      String vmm= inputDcVmm.getValue();
      Double timeZone=Double.parseDouble(inputTimeZone.getText());
     // Double timeZone=10.0;
      Double cost=inputDcCost.getValue();
      Double costPerRAM=inputDcCostPerRAM.getValue();
      Double costPerStorage=inputDcCostPerStorage.getValue();
      Double costPerBW=inputDcCostPerBandwidth.getValue();
      LinkedList<Storage> storageList = new LinkedList<Storage>();


      DatacenterCharacteristics datacenterCharacteristics= new DatacenterCharacteristics(sysArch,OS,vmm,hostList,timeZone,cost,costPerRAM,costPerStorage, costPerBW);
      Datacenter datacenter=null;
      System.out.println("characteristics created");
      System.out.println(dcName);
      System.out.println(hostList.size());
      System.out.println(storageList.size());
      System.out.println(datacenterCharacteristics.getVmm());
      try {
         datacenter= new Datacenter(dcName,datacenterCharacteristics,new VmAllocationPolicySimple(hostList), storageList,0);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      dcID++;
      return datacenter;
   }





   @Override
   public void initialize(URL location, ResourceBundle resources) {

      listOfUsers.setItems(listItems);
      deleteUser.setDisable(true);
      if (hostList.size()==0){
         btnCreateDc.setDisable(true);
         inputDcArch.setDisable(true);
         inputDcOS.setDisable(true);
         inputDcVmm.setDisable(true);
         inputTimeZone.setDisable(true);
         inputDcCost.setDisable(true);
         inputDcCostPerRAM.setDisable(true);
         inputDcCostPerStorage.setDisable(true);
         inputDcCostPerBandwidth.setDisable(true);
      }

      //disable other section if there is no user
      listOfUsers.focusedProperty().addListener(new ChangeListener<Boolean>() {
         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (listOfUsers.isFocused() && numberOfUsers!=0) {
               deleteUser.setDisable(false);
            }else deleteUser.setDisable(true);
         }
      });



      inputHostNumberOfCores.setItems(CPUCoresList);
      inputHostNumberOfCores.setValue(CPUCoresList.get(0));
      inputHostSchedulingPolicy.setItems(schedulingPoliciesList);
      inputHostSchedulingPolicy.setValue(schedulingPoliciesList.get(0));
      inputDcArch.setItems(archList);
      inputDcArch.setValue(archList.get(0));
      inputDcVmm.setItems(dcVmmList);
      inputDcVmm.setValue(dcVmmList.get(0));


      inputNumberOfHosts.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,1));
      inputNumberOfHosts.setEditable(true);

      inputHostMips.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000,100000,1000,1000));
      inputHostMips.setEditable(true);

      inputHostRAM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(256,10240,1024,256));
      inputHostRAM.setEditable(true);

      inputHostBW.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10000,1000000,10000,1000));
      inputHostBW.setEditable(true);

      inputHostStorage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(100000.00, 1000000000.00, 1000000.00,100000));
      inputHostStorage.setEditable(true);

      inputDcCost.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 100, 3, 0.1));
      inputDcCost.setEditable(true);

      inputDcCostPerRAM.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 2.0, 0.05, 0.01 ));
      inputDcCostPerRAM.setEditable(true);

      inputDcCostPerStorage.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10, 0.1, 0.1));
      inputDcCostPerStorage.setEditable(true);

      inputDcCostPerBandwidth.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10, 0.1, 0.1));
      inputDcCostPerBandwidth.setEditable(true);

      inputDcOS.setText("Linux");
      inputTimeZone.setText(String.valueOf(10.0));


      btnCreateHosts.setOnAction(e->{
         //hostID=0;

         int numberOfHosts= inputNumberOfHosts.getValue();
         for (int i = 0; i <numberOfHosts ; i++) {
            hostList.add(createHost());
            System.out.println("Host "+hostID+" created successfully");
            hostID++;

         }
         System.out.println(hostList.size()+" hosts created");
         textShowNumberOfHosts.setText(String.valueOf(hostList.size())+" hosts created");
         textShowNumberOfHosts.setFill(Color.GREEN);

         btnCreateDc.setDisable(false);
         inputDcArch.setDisable(false);
         inputDcOS.setDisable(false);
         inputDcVmm.setDisable(false);
         inputTimeZone.setDisable(false);
         inputDcCost.setDisable(false);
         inputDcCostPerRAM.setDisable(false);
         inputDcCostPerStorage.setDisable(false);
         inputDcCostPerBandwidth.setDisable(false);
      });


      //creating datacenter
      btnCreateDc.setOnAction(e -> {
         System.out.println("******************CREATING DATACENTER*******************");


         Datacenter datacenter=createDatacenter();

         textShowNumberOfHosts.setText("0 host created");
         textShowNumberOfHosts.setFill(Color.RED);
         hostList.clear();
         if (hostList.size()==0){
            btnCreateDc.setDisable(true);
            inputDcArch.setDisable(true);
            inputDcOS.setDisable(true);
            inputDcVmm.setDisable(true);
            inputTimeZone.setDisable(true);
            inputDcCost.setDisable(true);
            inputDcCostPerRAM.setDisable(true);
            inputDcCostPerStorage.setDisable(true);
            inputDcCostPerBandwidth.setDisable(true);
         }
      });





   }
}
