/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxdb2;

import com.mysql.jdbc.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 *
 * @author Arif
 */
public class FXMLDocumentController implements Initializable {
    private ObservableList<ObservableList> data;


    
    @FXML
    private TableView tableview;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        tableview.getColumns().clear();
        tablodoldur();
    }
    
    private void tablodoldur()
    {
        tableview.getColumns().clear();
          Connection c ;
          data = FXCollections.observableArrayList();
          try{
            c = (Connection) DBConnect.connect();
            //sql string ifademiz. 
            String SQL = "SELECT * from bilgi";//tablomuzun adı bilgi. id ve adi alanları var. 
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            // TABLO SÜTUNLARINI DİNAMİK OLARAK EKLEYECEĞİMİZ YAPI 
             
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });
               
                tableview.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
            }

            //ObservableList e verileri ekleyen döngü
            while(rs.next()){
                //Satırları yinele
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //sütunları yinele
                    row.add(rs.getString(i));
                }
                System.out.println("Satır eklendi "+row );
                data.add(row);
            }

            //Sonucu tabloya ekleme
            tableview.setItems(data);
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Hata oluştu");             
          }
    }
    
    @FXML
    private TextField txtad; //txtadi tanımladık. Uyarı verdiği yere tıklayarak TextFieldi import edebiliriz. 
    
    @FXML
    private void handleEkle(ActionEvent event){
        Connection c;
        
        try{
        c = (Connection) DBConnect.connect();
        String query = "insert into bilgi(ad) values(?)"; //sqlimizi yazıyoruz. Değeri aşağıda tanımlayacağız. 

      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setString (1, txtad.getText().toString()); //? ile belirttiğimiz değer. 
      preparedStmt.execute();//komutu çalıştırıyoruz
      tablodoldur();//tablomuzu yeniliyoruz. 
      txtad.setText("");//txtadi temizliyoruz. 
      
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
