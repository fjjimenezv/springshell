package es.fran.shell.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.text.similarity.FuzzyScore;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String getGreeting(String value) {
        // This is where your real business logic would go,
        // like calculating data or fetching from a database.
        similarities(value);
        return "What's up, " + value + "!";
    }    

    public List<String> similarities(String value) {
        List<String> resultList = new ArrayList<>();

        List<String> collectionList = new ArrayList<>();
        collectionList.add("Alimentación - Supermercado");
        collectionList.add("Alimentación - Panadería");
        collectionList.add("Ocio - Restaurante");
        collectionList.add("Supermercado. JR");
        collectionList.add("Supermercado. ALDI");

        List<Similarity> similarityList = new ArrayList<>();
        
        FuzzyScore score = new FuzzyScore(Locale.forLanguageTag("ES"));
        for (String collection : collectionList) {
            double similarity = score.fuzzyScore(collection, value);
            if (similarityList.size() < 3) {
                similarityList.add(new Similarity(collection, similarity));
                similarityList.sort(Comparator.comparingDouble(Similarity::valor).reversed());
            } else {
                if (similarity > similarityList.get(2).valor) {
                    similarityList.remove(2);
                    similarityList.add(new Similarity(collection, similarity));
                }
                similarityList.sort(Comparator.comparingDouble(Similarity::valor).reversed());
            }
            System.out.println(similarity + " " + collection);
        }

        System.out.println("-- Valores --");
        for (Similarity similarity : similarityList) {
            System.out.println(similarity.valor + " " + similarity.texto);
        }

        return resultList;
    }

    public record Similarity(String texto, double valor) {}
}
