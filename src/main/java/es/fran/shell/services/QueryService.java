package es.fran.shell.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fran.shell.model.InventoryObject;

@Service
public class QueryService {
    @Autowired
    private CSVService csvService;

    public String getQuery(String type, String value) {
        switch (type) {
            case "ft":
                List<InventoryObject> inventoryList = csvService.findAllByFTS(value);
                inventoryList.forEach(x -> {
                    System.out.println(x.toString());
                });                
                break;

            case "pa":
                return csvService.calculateContainersString(Integer.valueOf(value));
        
            case "ta":
                List<InventoryObject> tagInventoryList = csvService.findAllContainingPropertyAndValue("tag", value);
                tagInventoryList.forEach(x -> {
                    System.out.println(x.toString());
                });
                break;

            default:
                break;
        }
        return "What's up, " + value + "!";
    }
}
