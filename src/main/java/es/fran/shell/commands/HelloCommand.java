package es.fran.shell.commands;
    
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import es.fran.shell.services.GreetingService;

@ShellComponent
public class HelloCommand {

    @Autowired
    private GreetingService greetingService;
    ;
    @ShellMethod(key = "hello", value = "Says hello")
    public String hello(@ShellOption(defaultValue = "World") String name) {
        return greetingService.getGreeting(name);
    }
}
