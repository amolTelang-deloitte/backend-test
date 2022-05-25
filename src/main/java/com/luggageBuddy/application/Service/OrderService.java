package com.luggageBuddy.application.Service;

import com.luggageBuddy.application.Entity.Location;
import com.luggageBuddy.application.Entity.Orders;
import com.luggageBuddy.application.Exception.ResourceNotFoundException;
import com.luggageBuddy.application.Exception.SpaceException;
import com.luggageBuddy.application.Repository.CityRepository;
import com.luggageBuddy.application.Repository.LocationRepository;
import com.luggageBuddy.application.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class OrderService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private OrderRepository orderRepository;

    //get all orders
    public List<Orders> getAllOrders(){
        return orderRepository.findAll();
    }

    //get all orders by locationId
    public List<Orders> getAllOrdersByLocationId(String locId){
        if(  validLocationId(locId)){
            throw new ResourceNotFoundException("invlaid location id");
        }

        List<Orders> tempOrders=new ArrayList<>();
        tempOrders.addAll(orderRepository.findAllBylocationId(locId));
        List<Orders> updatedOrders=new ArrayList();
        for(int i=0;i<tempOrders.size();i++){
            Orders temp=tempOrders.get(i);
            if(temp.getStatus().equals("pending"))
            {
                Date newDate=getCurrentDate();
                float amt=getAmount(temp.getOrderStart(),newDate,temp.getCityName());
                temp.setCurrentAmount(amt);
                updatedOrders.add(temp);
            }
        }
        return updatedOrders;
    }

    //get orders by customerId
    public List<Orders> getAllOrdersByCustomerId(String custId){
        List<Orders> tempOrders=new ArrayList<>();
        tempOrders.addAll(orderRepository.findAllBycustomerId(custId));
        List<Orders> updatedOrders=new ArrayList();
        for(int i=0;i<tempOrders.size();i++){
            Orders temp=tempOrders.get(i);
            if(temp.getStatus().equals("pending"))
            {
                Date newDate=getCurrentDate();
                float amt=getAmount(temp.getOrderStart(),newDate,temp.getCityName());
                temp.setCurrentAmount(amt);
                updatedOrders.add(temp);
            }
        }
        return updatedOrders;
    }

    //get order by orderId
    public Orders getOrderByOrderId(Long orderId){
        if(  validOrderId(orderId)){
            throw new ResourceNotFoundException("invlaid order id");
        }
        Orders tempOrder=orderRepository.findById(orderId).get();
        return tempOrder;
    }

    //get pending orders by locationid
    public List<Orders> getPendingOrdersByLocationId(String locId){
        if(  validLocationId(locId)){
            throw new ResourceNotFoundException("invlaid location id");
        }
        List<Orders> tempOrder=orderRepository.findAllBylocationId(locId);
        List<Orders> updatedOrders=new ArrayList<>();
        for(int i=0;i<tempOrder.size();i++)
        {
            if(tempOrder.get(i).getStatus().equals("pending")){
                updatedOrders.add(tempOrder.get(i));
            }
        }
        return updatedOrders;

    }

    //add an order
    public ResponseEntity addOrder(Orders newOrder, String locId, String customerId, String cityName){
        if(  validLocationId(locId)){
            throw new ResourceNotFoundException("invlaid location id");
        }
        Location tempLoc=locationRepository.findById(locId).get();
        //check if space is available
        System.out.println(tempLoc.getTotalInventory());
        System.out.println(tempLoc.getUsedInventory());
        System.out.println(newOrder.getNoOfBags());

        if(tempLoc.getUsedInventory()+ newOrder.getNoOfBags()>tempLoc.getTotalInventory())
        {
            throw new SpaceException("capacity exceeded");
        }
        Orders tempOrder=new Orders();
        tempOrder.setCustomerId(customerId);
        tempOrder.setLocationId(locId);


        //get customer name from the customerRepo and add it into the obj
        tempOrder.setNoOfBags(newOrder.getNoOfBags());
        tempOrder.setCityName(cityName);
        tempOrder.setOrderStart(getCurrentDate());
        tempOrder.setStatus(newOrder.getStatus());



        //change the available space
        tempLoc.setUsedInventory(tempLoc.getUsedInventory()+ newOrder.getNoOfBags());
        tempLoc.setAvailableInventory(tempLoc.getTotalInventory()- tempLoc.getUsedInventory());
        locationRepository.save(tempLoc);
        orderRepository.save(tempOrder);


        return new ResponseEntity(HttpStatus.OK);
    }

    //cancel an order
    public ResponseEntity cancelOrder(Long orderId){
        if(  validOrderId(orderId)){
            throw new ResourceNotFoundException("invlaid order id");
        }
        Orders tempOrder=orderRepository.findById(orderId).get();
        Location tempLocation=locationRepository.findById(tempOrder.getLocationId()).get();
        tempLocation.setAvailableInventory(tempLocation.getAvailableInventory()+ tempOrder.getNoOfBags());
        tempLocation.setUsedInventory(tempLocation.getUsedInventory()- tempOrder.getNoOfBags());
        tempOrder.setStatus("cancelled");
        orderRepository.save(tempOrder);
        locationRepository.save(tempLocation);
        return new ResponseEntity(HttpStatus.OK);
    }

    //returns final amount
    public float getFinalPrice(Long orderId){
        if(  validOrderId(orderId)){
            throw new ResourceNotFoundException("invlaid order id");
        }
        Orders temp=orderRepository.findById(orderId).get();
        Date endDate=new Date();
        float amt=getAmount(temp.getOrderStart(),endDate,temp.getCityName());
        return amt;
    }

    //complete Order
    public ResponseEntity completeOrder(Long orderId,float paidAmt){

        if( validOrderId(orderId)){
            throw new ResourceNotFoundException("invlaid order id");
        }
        Orders tempOrder=orderRepository.findById(orderId).get();

        if(checkAmt(paidAmt,tempOrder.getCurrentAmount()))
        {
            //throw exception
        }

        Date endDate=new Date();
        tempOrder.setOrderEnd(endDate);
        tempOrder.setFinalAmount(paidAmt);
        tempOrder.setStatus("COMPLETED");
        orderRepository.save(tempOrder);
        Location tempLocation=locationRepository.findById(tempOrder.getLocationId()).get();
        tempLocation.setAvailableInventory(tempLocation.getAvailableInventory()+ tempOrder.getNoOfBags());
        tempLocation.setUsedInventory(tempLocation.getUsedInventory()- tempOrder.getNoOfBags());
        locationRepository.save(tempLocation);


        return new ResponseEntity(HttpStatus.OK);
    }

    //----------------
    //get current date and time
    public Date getCurrentDate(){
        Date date = new Date();
        return date;
    }

    //check final amount is correct or not
    public Boolean checkAmt(float finalAmt,float currentAmt){
        if(finalAmt==currentAmt)
        {
            return false;
        }
        else
            return true;
    }

    //invalid order id return true if invalid id or flase if valid id
    public Boolean validOrderId(Long id){
        if(orderRepository.findById(id).isEmpty()){
            return true;
        }
        return false;
    }


    //returns the amount between 2 dates
    public float getAmount(Date startDate,Date endDate,String cityName){
        float perHour=cityRepository.findById(cityName).get().getPricePerHour();
        float perDay=cityRepository.findById(cityName).get().getPricePerDay();
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println(perHour);
        int seconds=getSeconds(startDate, endDate);
        int arr[]=getDaysAndHours(seconds);
        float amt=arr[0]*perDay+arr[1]*perHour;
        return amt;
    }

    //return the no of seconds between 2 dates
    public int getSeconds(Date startDate,Date endDate){
        long duration=endDate.getTime()-startDate.getTime();
        long sec= TimeUnit.MILLISECONDS.toSeconds(duration);
        return (int)sec;
    }



    //get no of hours and days through no of seconds
    public int[] getDaysAndHours(int seconds){
        int ans[]=new int[2];
        int fsec,d,h,m,s,temp=0,i;
        fsec=seconds;
        //For Days
        if(fsec>=86400)
        {
            temp=fsec/86400;
            d=temp;
            for(i=1;i<=temp;i++)
            {
                fsec-=86400;
            }
        }
        else
        {
            d=0;
        }
        //For Hours
        if(fsec>=3600)
        {
            temp=fsec/3600;
            h=temp;
            for(i=1;i<=temp;i++)
            {
                fsec-=3600;
            }
        }
        else
        {
            h=0;
        }
        //For Minutes
        if(fsec>=60)
        {
            temp=fsec/60;
            m=temp;
            for(i=1;i<=temp;i++)
            {
                fsec-=60;
            }
        }
        else
        {
            m=0;
        }
        //For Seconds
        if(fsec>=1)
        {
            s=fsec;
        }
        else
        {
            s=0;
        }
        ans[0]=d;
        ans[1]=h;

        return ans;
    }



    public Boolean validLocationId(String locationId){
        //return true if invalid locaitonId
        if(locationRepository.findById(locationId).isEmpty())
        {
            return true;
        }
        //return false if valid location id
        return false;
    }
}
