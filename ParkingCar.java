package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ParkingCar {
    public static void main(String[] args) throws Exception {
        String url="jdbc:mysql://localhost:3306/parkinglot";
        String username="root";
        String pass="Viratverma@12";

        ParkingLot parkingLot =new ParkingLot(5, username, url ,pass);

        Car car1=new Car("UP 12 A 2025");
        Car car2=new Car("UP 14 B 2324");
        Car car3=new Car("UP 16 VV 0001");
//        System.out.println("entry inserted");
        System.out.println("-------------------------");
        ParkingLot parkingLot1=new ParkingLot(3, username, url, pass);
        parkingLot.parkCar(car1);
        parkingLot.parkCar(car2);
        parkingLot.parkCar(car3);

        List<Integer>deletesport= Arrays.asList(2,4,6,8,9);

       parkingLot1.deleteData(deletesport);

    }
}

class Car{
    private String licencePlate;
    public  Car (String licencePlate){
        this.licencePlate =licencePlate;
    }

    public String getLicencePlate(){
        return licencePlate;
    }
    public String getSportNum(){
        return getSportNum();
    }
}

class ParkingSport{
    private int sportNum;
    private boolean available;
    private Car car;


    public ParkingSport(int sportNum){
        this.sportNum=sportNum;
        this.car=null;
        this.available=true;
    }
    public int getSportNum(){
        return sportNum;
    }
    public boolean isAvailable(){
        return available;
    }

    public Car getCar(){
        return car;
    }
    public void occupy(Car car){
        this.car=car;
        this.available=false;
    }
    public void vacant(){
        this.car=null;
        this.available=true;
    }

}

class ParkingLot{
    private List<ParkingSport> sport;
    private String username;
    private String url;
    private String pass;


   public ParkingLot(int capacity, String username, String url, String pass){

        this.sport=new ArrayList<>();
        for(int i=0; i<capacity; i++){
            sport.add(new ParkingSport(i));
        }
        this.url=url;
        this.username=username;
        this.pass=pass;
   }


    public boolean parkCar(Car car){
        for(ParkingSport sport: sport){
            if(sport.isAvailable()){
                sport.occupy(car);
                System.out.println("car exists:"+car.getLicencePlate()+" parked @ sport num:"+sport.getSportNum());
//                return true;
                insertIntoDB(car.getLicencePlate(), sport.getSportNum());
//                deleteData();
                return true;
            }
        }
        System.out.println("Sorry Parking is not available:");
        return false;
    }
    public boolean removeCar(String licensePlate){
        for(ParkingSport sport:sport){
            if(!sport.isAvailable() && sport.getCar().getLicencePlate().equalsIgnoreCase(licensePlate)){
                sport.vacant();
                System.out.println("car with num:"+licensePlate + "remove"+ sport.getSportNum());
            return true;
            }
        }
        System.out.println("car with num:"+ licensePlate +"not found");
        return false;
    }
    private void insertIntoDB(String licensePlate, int sportNum){
            String sql = "insert into parking (v_no, sport_num) values(?, ?)";

            try (Connection connection = DriverManager.getConnection(url, username, pass);

//
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, licensePlate);
                statement.setInt(2, sportNum);
                int rowInserte=statement.executeUpdate();
                if(rowInserte>0)
                System.out.println("Data inserted into DB");
                else {
                    System.out.println("data is deleted ...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
   }
   public void deleteData(List<Integer> sportNum){
       String delQ="Delete from parking where id=?";
       try(Connection connection=DriverManager.getConnection(url, username, pass) ;
           PreparedStatement delStat=connection.prepareStatement(delQ)){
           for(Integer sportNums:sportNum){
               delStat.setInt(1, sportNums);
               int rowDelete=delStat.executeUpdate();
               System.out.println("Deleted"+ rowDelete + "for sportNum"+ sportNums);
           }
       }
       catch (Exception e){
           e.printStackTrace();
       }
   }
}