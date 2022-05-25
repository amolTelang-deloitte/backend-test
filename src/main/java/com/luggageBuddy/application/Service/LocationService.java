package com.luggageBuddy.application.Service;

import com.luggageBuddy.application.Entity.City;
import com.luggageBuddy.application.Entity.Location;
import com.luggageBuddy.application.Exception.ResourceAlreadyPresentException;
import com.luggageBuddy.application.Exception.ResourceNotFoundException;
import com.luggageBuddy.application.Repository.CityRepository;
import com.luggageBuddy.application.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CityRepository cityRepository;


    //get all cities
    public List<City> getAllCities(){
        return cityRepository.findAll();
    }

    //get all the locations from the database
    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    //get city by cityId
    public City getCityByName(String cityName){
        if(validCity(cityName)){
            throw new ResourceNotFoundException("invalid city name");
        }
        return cityRepository.findById(cityName).get();
    }

    //get location by loc phno
    public Location getLocationByPhno(String phno){
        if(validLocationId(phno)){
            throw new ResourceNotFoundException("invalid location id");
        }
        return locationRepository.findById(phno).get();
    }

    //get all locations by citname
    public List<Location> getLocationsByCityname(String cityname){
        if(validCity(cityname)){
            throw new ResourceNotFoundException("invalid city name");
        }
        ArrayList<Location> tempList=new ArrayList<>();
        tempList.addAll(locationRepository.findAll());
        ArrayList<Location> updatedList=new ArrayList<>();
        for(int i=0;i<tempList.size();i++){
            if(tempList.get(i).getCity().getCityName().equals(cityname)){
                updatedList.add(tempList.get(i));
            }
        }
        return updatedList;
    }

    //add a city into the database
    public ResponseEntity addCity(City newCity){
        if(cityPresent(newCity.getCityName())){
            throw new ResourceAlreadyPresentException("city already present");
        }
        cityRepository.save(newCity);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //adds location into the database
    public ResponseEntity addLocation(Location newLocation){
//        if(locationPresent(newLocation.getLocationId())){
//            throw new ResourceAlreadyPresentException("location already present");
//        }
        Location tempLocation= new Location();
        tempLocation.setLocationPhno(newLocation.getLocationPhno());
        tempLocation.setLocationEmail(newLocation.getLocationEmail());
        tempLocation.setLocationPassword(newLocation.getLocationOwnersLastName()+newLocation.getLocationPhno());
        tempLocation.setLocationOwnersFirstName(newLocation.getLocationOwnersFirstName());
        tempLocation.setLocationOwnersLastName(newLocation.getLocationOwnersLastName());
        tempLocation.setLocationName(newLocation.getLocationName());
        tempLocation.setCity(newLocation.getCity());

        locationRepository.save(tempLocation);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //update city pricing
    public ResponseEntity updateCityInfo(City updatedCity,String cityName){
        //exception to check if valid city or not
        if(validCity(cityName)){
            throw new ResourceNotFoundException("invalid city name");
        }
        City tempCity= cityRepository.findById(cityName).get();
        tempCity.setCityName(cityName);
        if(!(updatedCity.getPricePerHour()==null))
        {
            tempCity.setPricePerHour(updatedCity.getPricePerHour());
        }
        if(!(updatedCity.getPricePerDay()==null)){
            tempCity.setPricePerDay(updatedCity.getPricePerDay());
        }
        cityRepository.save(tempCity);
        return new ResponseEntity(HttpStatus.OK);
    }

    //update location info
    public ResponseEntity updateLocationInfo(Location updatedLocation,String id){
        if(  validLocationId(id)){
            throw new ResourceNotFoundException("invlaid location id");
        }
        //update if only not null
        Location tempLocation=locationRepository.findById(id).get();
        if(!(updatedLocation.getLocationOwnersLastName()==null))
            tempLocation.setLocationOwnersLastName(updatedLocation.getLocationOwnersLastName());
        if(!(updatedLocation.getLocationOwnersFirstName()==null))
            tempLocation.setLocationOwnersFirstName(updatedLocation.getLocationOwnersFirstName());
        if(!(updatedLocation.getAddress()==null))
            tempLocation.setAddress(updatedLocation.getAddress());
        if(!(updatedLocation.getImageBase64()==null))
            tempLocation.setImageBase64(updatedLocation.getImageBase64());
        if(!(updatedLocation.getTotalInventory()==null))
            tempLocation.setTotalInventory(updatedLocation.getTotalInventory());
        if(tempLocation.getUsedInventory()==0||tempLocation.getUsedInventory()==null)
            tempLocation.setAvailableInventory(tempLocation.getTotalInventory());
        else{
            tempLocation.setAvailableInventory(tempLocation.getTotalInventory() - tempLocation.getUsedInventory());
        }
        if(!(updatedLocation.getWorkingHours()==null))
            tempLocation.setWorkingHours(updatedLocation.getWorkingHours());
        locationRepository.save(tempLocation);
        return new ResponseEntity(HttpStatus.OK);
    }

    //update availability by id
    public ResponseEntity updateAvailability(String id,Boolean state){
        if(validLocationId(id)){
            throw new ResourceNotFoundException("invalid location id ");
        }
        Location tempLocation=locationRepository.findById(id).get();
        tempLocation.setAvailability(state);
        locationRepository.save(tempLocation);

        return new ResponseEntity(HttpStatus.OK);
    }

    //delete location from the database
    public ResponseEntity deleteLocation(String locationId){
        if(validLocationId(locationId)){
            throw new ResourceNotFoundException("invalid location id");
        }
        locationRepository.deleteById(locationId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    //delete city from the database
    public ResponseEntity deleteCity(String cityName){
        if(validCity(cityName)){
            throw new ResourceNotFoundException("invalid city name");
        }
        cityRepository.deleteById(cityName);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    //update city prices in db






    //-----------------------------------util functions------------------------------------------

    //check valid city name
    public Boolean validCity(String cityName){
        //return true if invalid cityname
        if(cityRepository.findById(cityName).isEmpty()) return true;
        //return false if valid city name
        return false;
    }
    //check if city already present\
    public Boolean cityPresent(String cityName){
        //return true if city is already present
        if(!(cityRepository.findById(cityName).isEmpty())){
            return true;
        }
        //return flase if not present
        return false;
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
