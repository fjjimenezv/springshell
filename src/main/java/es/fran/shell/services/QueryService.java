package es.fran.shell.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fran.shell.model.InventoryObject;

@Service
public class QueryService {
    @Autowired
    private CSVService csvService;

    public String getQuery(String value) {
        List<InventoryObject> inventoryService = csvService.findAllByFTS(value);
        inventoryService.forEach(x -> {
            System.out.println(x.toString());
        });
        return "What's up, " + value + "!";
    }
}
