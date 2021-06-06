package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sample.QueryExecutor;

import java.sql.ResultSet;

public class ticketNumberController {

    @FXML
    public AnchorPane myPane;
    Text student;
    Text old;
    Text soldier;
    Text normal;
    Text worker;
    public void initialize(){
        student = new Text();
        old = new Text();
        soldier = new Text();
        normal = new Text();
        worker = new Text();
        int a = 0,b=0,c=0,d=0,es=0;
        Text discount = new Text();
        Text disc_val = new Text();
        discount.setX(380);
        discount.setY(270);
        discount.setText("Discount type");
        discount.setFont(Font.font("Arial", 25));
        myPane.getChildren().add(discount);
        disc_val.setX(575);
        disc_val.setY(270);
        disc_val.setText("Amount");
        disc_val.setFont(Font.font("Arial", 25));
        myPane.getChildren().add(disc_val);

        String query = "SELECT * FROM znizka;";
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
        addText(normal, "Normal", d, 0);
        addText(soldier, "Veteran", c, 3);
        addText(old, "Retired", b, 2);
        addText(student, "Student", a, 1);
        addText(worker, "Worker", es, 4);
    }
    public void addText(Text text,String name, int value, int pos){
        text.setText(name);
        text.setX(400);
        text.setY(300+30*pos);
        text.setFont(Font.font("Arial", 20));
        Text tmp = new Text();
        tmp.setX(600);
        tmp.setY(300+30*pos);
        tmp.setFont(Font.font("Arial", 20));
        tmp.setText(value + "%");
        myPane.getChildren().add(text);
        myPane.getChildren().add(tmp);
    }
}
