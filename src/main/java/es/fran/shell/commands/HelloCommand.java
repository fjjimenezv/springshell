package es.fran.shell.commands;
    
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import es.fran.shell.services.GreetingService;
import es.fran.shell.services.QueryService;

@ShellComponent
public class HelloCommand {

    @Autowired
    private GreetingService greetingService;

    @Autowired
    private QueryService queryService;
    
    @ShellMethod(key = "hello", value = "Says hello")
    public String hello(@ShellOption(defaultValue = "World") String name, 
            @ShellOption(defaultValue = ShellOption.NULL) String name2) {
        if (name2 != null) {
            return greetingService.getGreeting(name + " " + name2);
        } else {
            return greetingService.getGreeting(name);
        }
    }

    @ShellMethod(key = "q", value = "Query")
    public String query(@ShellOption String value) {
        return queryService.getQuery(value);
    }    
}
