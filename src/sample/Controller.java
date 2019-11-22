package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.cloudbus.cloudsim.Vm;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
   /**************************************************************************
    ********************** GRAPHICAL COMPONENTS ******************************
    **************************************************************************/

   @FXML private Button deleteUser;
   @FXML private Button addUser;
   @FXML private ListView<String> listOfUsers;



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
