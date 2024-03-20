package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private ConverteDados conversor = new ConverteDados();
    private ConsumoAPI consumo = new ConsumoAPI();
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=729d1535";
    public void exibeMenu() {
        System.out.println("Digite o nome da série");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalSeasons(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);

//        for(int i = 0; i < dados.totalSeasons(); i++) {
//            List<DadosEpisodio> episodiosTemp = temporadas.get(i).episodes();
//            for(int j = 0; j < episodiosTemp.size(); j++) {
//                System.out.println(episodiosTemp.get(j).title());
//            }
//        }

        temporadas.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<DadosEpisodio> dadosEpi = temporadas.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());


        dadosEpi.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::rating).reversed())
                .limit(10)
                .forEach(System.out::println);
    }
}
