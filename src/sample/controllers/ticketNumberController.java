package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sample.Main;
import sample.QueryExecutor;

import java.sql.ResultSet;

public class ticketNumberController {

    @FXML public Button confirmButton;
    int manyTicket = 0;
    int filmPrice=0;
    @FXML
    public AnchorPane myPane;
    //valueText
    @FXML
    Text actualTicektValue;
    @FXML
    Text studentValue;
    @FXML
    Text normalValue;
    @FXML
    Text retiredValue;
    @FXML
    Text veteranValue;
    @FXML
    Text workerValue;
    //procentText
    @FXML
    Text normalProcent;
    @FXML
    Text studentProcent;
    @FXML
    Text retiredProcent;
    @FXML
    Text veteranProcent;
    @FXML
    Text workerProcent;
    @FXML
    Text normalPrice;
    @FXML
    Text studentPrice;
    @FXML
    Text retiredPrice;
    @FXML
    Text veteranPrice;
    @FXML
    Text workerPrice;
    @FXML
    Text totalPrice;
    int student=0;
    int normal=0;
    int veteran=0;
    int worker=0;
    int retired=0;
    int a = 0,b=0,c=0,d=0,es=0;
    public void initialize(){
        manyTicket=reservationController.amountOfTickets;
        actualTicektValue.setText(String.valueOf(manyTicket));
        String query = "SELECT cena FROM seans WHERE id="+repertoireController.seans+";";
        try{
            ResultSet resultSet=QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                filmPrice=resultSet.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Text discount = new Text();
        Text disc_val = new Text();
        discount.setX(400);
        discount.setY(270);
        discount.setText("Discount type");
        discount.setFont(Font.font("Arial", 25));
        myPane.getChildren().add(discount);
        disc_val.setX(585);
        disc_val.setY(270);
        disc_val.setText("Amount");
        disc_val.setFont(Font.font("Arial", 25));
        myPane.getChildren().add(disc_val);

        query = "SELECT * FROM znizka;";
        try {
            ResultSet result = QueryExecutor.executeSelect(query);
            while (result.next()) {
                int tmp = result.getInt(1);
                if(tmp == 1){
                    a = result.getInt(3);
                } else if (tmp == 2){
                    b = result.getInt(3);
                } else if (tmp == 3){
                    c = result.getInt(3);
                } else if(tmp == 4){
                    d = result.getInt(3);
                } else {
                    es = result.getInt(3);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        normalProcent.setText(String.valueOf(d)+"%");
        studentProcent.setText(String.valueOf(a)+"%");
        retiredProcent.setText(String.valueOf(b)+"%");
        veteranProcent.setText(String.valueOf(c)+"%");
        workerProcent.setText(String.valueOf(es)+"%");
    }
    @FXML public void addNormal(){
        if(manyTicket>0){
            manyTicket--;
            normal++;
            normalValue.setText(String.valueOf(normal));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void subNormal(){
        if(normal>0){
            manyTicket++;
            normal--;
            normalValue.setText(String.valueOf(normal));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void addStudent(){
        if(manyTicket>0){
            manyTicket--;
            student++;
            studentValue.setText(String.valueOf(student));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void subStudent(){
        if(student>0){
            manyTicket++;
            student--;
            studentValue.setText(String.valueOf(student));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void addRetired(){
        if(manyTicket>0){
            manyTicket--;
            retired++;
            retiredValue.setText(String.valueOf(retired));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void subRetired(){
        if(retired>0){
            manyTicket++;
            retired--;
            retiredValue.setText(String.valueOf(retired));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void addVeteran(){
        if(manyTicket>0){
            manyTicket--;
            veteran++;
            veteranValue.setText(String.valueOf(veteran));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void subVeteran(){
        if(veteran>0){
            manyTicket++;
            veteran--;
            veteranValue.setText(String.valueOf(veteran));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void addWorker(){
        if(manyTicket>0){
            manyTicket--;
            worker++;
            workerValue.setText(String.valueOf(worker));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    @FXML public void subWorker(){
        if(worker>0){
            manyTicket++;
            worker--;
            workerValue.setText(String.valueOf(worker));
            actualTicektValue.setText(String.valueOf(manyTicket));
            refreashStats();
        }
    }
    public int sumPrice(){
        double normalTotalPrice=normal*filmPrice;
        double studentTotalPrice=student*filmPrice*((100f-a)/100f);
        double workerTotalPrice=worker*filmPrice*(100f-es)/100f;
        double veteranTotalPrice=veteran*filmPrice*(100f-c)/100f;
        double retiredTotalPrice=retired*filmPrice*(100f-b)/100f;
        double sum=normalTotalPrice+studentTotalPrice+workerTotalPrice+veteranTotalPrice+retiredTotalPrice;
        return (int)sum;
    }
    public void refreashStats(){
        double normalTotalPrice=normal*filmPrice;
        double studentTotalPrice=student*filmPrice*((100f-a)/100f);
        double workerTotalPrice=worker*filmPrice*(100f-es)/100f;
        double veteranTotalPrice=veteran*filmPrice*(100f-c)/100f;
        double retiredTotalPrice=retired*filmPrice*(100f-b)/100f;
        normalPrice.setText(String.valueOf((int)normalTotalPrice));
        studentPrice.setText(String.valueOf((int)studentTotalPrice));
        workerPrice.setText(String.valueOf((int)workerTotalPrice));
        veteranPrice.setText(String.valueOf((int)veteranTotalPrice));
        retiredPrice.setText(String.valueOf((int)retiredTotalPrice));
        totalPrice.setText(String.valueOf(sumPrice()));
    }
    @FXML public void addTickets(){
        if(manyTicket > 0){
            //TODO information about bad input
            return;
        }
        int seanceId = repertoireController.seans;
        int id_order = 0;
        String query = "INSERT INTO zamowienie VALUES(null, "+seanceId+", current_date, "+reservationController.isBuying+");";
        try {
            QueryExecutor.executeQuery(query);
        } catch (Exception e){
            e.printStackTrace();
        }
        query = "SELECT id from zamowienie ORDER BY id DESC LIMIT 1;";
        try {
            ResultSet resultSet = QueryExecutor.executeSelect(query);
            while(resultSet.next()){
                id_order = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<reservationController.amountOfTickets; i++){
            query = "INSERT INTO bilet VALUES("+id_order+", 4, " + reservationController.rows.get(i) +", "
                    + reservationController.columns.get(i) + ");";
            try {
                QueryExecutor.executeQuery(query);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Main.setScene("/sample/fxml/base.fxml", "/sample/style/style.css");
    }
}
