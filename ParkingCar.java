package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingCar {
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/parkinglot";
        String username="root";
        String pass="Viratverma@12";

        ParkingLot parkingLot =new ParkingLot(5, username, url ,pass);
        Car car1=new Car("ABCD12");
        Car car2=new Car("ABCD13");
        Car car3=new Car("ABCD14");
        System.out.println("entry inserted");
        System.out.println("-------------------------");
        parkingLot.parkCar(car1);
        parkingLot.parkCar(car2);
        parkingLot.parkCar(car3);

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
        String sql="insert into parking (v_no, sport_num) values(?, ?)";
        try(Connection connection=DriverManager.getConnection(url, username, pass);
            PreparedStatement statement=connection.prepareStatement(sql)) {
            statement.setString(1, licensePlate);
            statement.setInt(2, sportNum);
            statement.executeUpdate();
            System.out.println("Data inserted into DB");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}