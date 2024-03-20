package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

//Anti-Stream option
//        for(int i = 0; i < dados.totalSeasons(); i++) {
//            List<DadosEpisodio> episodiosTemp = temporadas.get(i).episodes();
//            for(int j = 0; j < episodiosTemp.size(); j++) {
//                System.out.println(episodiosTemp.get(j).title());
//            }
//        }

//        temporadas.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

//        List<DadosEpisodio> dadosEpi = temporadas.stream()
//                .flatMap(t -> t.episodes().stream())
//                .collect(Collectors.toList());


//        dadosEpi.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::rating).reversed())
//                .limit(10)
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream().
                flatMap(t -> t.episodes().stream()
                        .map(d-> new Episodio(t.number(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite o ano desejado");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate date = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e-> e.getReleased() != null && e.getReleased().isAfter(date))
//                .forEach(e-> System.out.println(
//                        "Temporada: " + e.getSeason() +
//                                " Episódio: " + e.getTitle() +
//                                " Data " +e.getReleased().format(formatter)
//                ));

//        System.out.println("Digite o título desejado");
//        var keyword = leitura.nextLine();
//        Optional<Episodio> respostaBuscaTitulo = episodios.stream()
//                .filter(e-> e.getTitle().toUpperCase().contains(keyword.toUpperCase()))
//                .findFirst();
//        if(respostaBuscaTitulo.isPresent()) {
//            System.out.println("Encontrado");
//            System.out.println("Temporada: " + respostaBuscaTitulo.get().getSeason());
//            System.out.println("Numero: " + respostaBuscaTitulo.get().getEpisodeN());
//            System.out.println("Nome Completo: " + respostaBuscaTitulo.get().getTitle());
//        } else {
//            System.out.println("Não encontrado");
//        }

        Map<Integer, Double> seasonRatings = episodios.stream()
                .filter(e-> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getSeason,
                        Collectors.averagingDouble(Episodio::getRating)));

        System.out.println(seasonRatings);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e-> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getRating));
        System.out.println(est);
    }
}
