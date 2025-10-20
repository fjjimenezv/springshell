package es.fran.shell.services;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String getGreeting(String value) {
        // This is where your real business logic would go,
        // like calculating data or fetching from a database.
        return "What's up, " + value + "!";
    }    
}
