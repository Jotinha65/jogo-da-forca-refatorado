package br.com.zup.jogoRefatorado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class JogoDaForca {

	public static void menuDeEscolhaDoTema() {
		System.out.println("\t========================================================== ");
		System.out.println("\t|                 JOGO DA FORCA REFATORADO                |");
		System.out.println("\t==========================================================");
		System.out.println("\t ANTES DE COMEÇAR ESCOLHA UM TEMA:\n");
		System.out.println("\t(1) - Animais\n\t(2) - Times de futebol\n\t(3) - Paises\n\t(4) - Carros");
		System.out.print("\n\tPARA ESCOLHER UMA OPÇÃO DIGITE O NUMERO REFERENTE AO TEMA \n\tQUE VOCÊ DESEJA:");

	}

	public static void menuDeRegras() {

		Scanner input = new Scanner(System.in);

		System.out.println("\tALGUMAS REGRAS PARA PODER JOGAR A FORCA!");
		System.out.println("\n\t(1) - Você inicia o jogo com 6 vidas.");
		System.out.println("\t(2) - Se digitar uma letra que não se encontra na palavra " + "\n\tperde uma vida.");
		System.out.println("\t(3) - Se digitar uma letra que já digitou antes, nada"
				+ "\n\tacontece, o jogo vai dar que a letra já foi"
				+ "\n\tdigitada e vai pedir para digitar uma nova letra.");
		System.out.println("\t(4) - Cada dica custa duas vidas.");
		System.out.println("\t(5) - Letras com acentos são consideradas.");

		System.out.print("\n\tPARA CONTINUAR APERTE (1) E DEPOIS ENTER:");
		String confirmarRegras = input.nextLine();
	}

	public static String sorteioPalavra(String nomeDoArquivo) throws IOException {

		FileReader fReader = new FileReader(nomeDoArquivo);
		BufferedReader bReader = new BufferedReader(fReader);

		String linha;

		int contaLinhas = 0;
		while ((linha = bReader.readLine()) != null) {
			contaLinhas++;
		}

		Random rand = new Random();
		int rangeDeLinhas = contaLinhas;
		int linhaEscolhida = rand.nextInt(rangeDeLinhas);

		String palavraEscolhida = Files.readAllLines(Paths.get(nomeDoArquivo)).get(linhaEscolhida);

		bReader.close();
		fReader.close();

		return palavraEscolhida;
	}

	public static String padronizarSemAcentos(String str) {
		
	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    
	    return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
	public static String adicionarNovaPalavra(String novaPalavra,String nomeDoArquivo) throws IOException {
		FileWriter fwriter = new FileWriter(nomeDoArquivo, true);
		fwriter.append(padronizarSemAcentos(novaPalavra));
		fwriter.append(String.format("\n"));

		fwriter.close();

		return novaPalavra;
	}
	
	public static void executandoJogo(String nomeDoArquivo) throws IOException {

		Scanner input = new Scanner(System.in);
		String palavraSorteada = sorteioPalavra(nomeDoArquivo);
		char[] letraDigitadasUtilizadas = new char[26];
		int posicao = 0, vidas = 0;
		boolean acertou = false;

		char[] palavraVetor = palavraSorteada.toUpperCase().toCharArray();
		char[] mostrarPalavra = new char[palavraVetor.length];

		char espacoEntrePalavras = ' ';

		System.out.print("\n\tA Palavra é:  ");
		for (int i = 0; i < mostrarPalavra.length; i++) {
			if (espacoEntrePalavras == palavraVetor[i]) {
				System.out.print(" ");
			} else {
				mostrarPalavra[i] = '_';
				System.out.print(mostrarPalavra[i] + " ");
			}
		}

		while (true) {
			System.out.print("\n\t Digite uma letra: ");
			char letraDigitada = input.next().toUpperCase().charAt(0);

			if (verificaletraDigitada(letraDigitada, letraDigitadasUtilizadas)) {
				letraDigitadasUtilizadas[posicao] = letraDigitada;
				posicao++;
				acertou = false;
				for (int i = 0; i < palavraVetor.length; i++) {
					if (letraDigitada == palavraVetor[i]) {
						mostrarPalavra[i] = letraDigitada;
						acertou = true;
					}
				}
				if (acertou) {
					System.out.println("\nFormação atual da palavra:");
					for (int i = 0; i < mostrarPalavra.length; i++)
						System.out.print(mostrarPalavra[i] + " ");
					if (ganhou(mostrarPalavra)) {
						System.out.println("\nParabéns, você GANHOU! Agora você tem direito de "
								+ "adicionar uma nova palavra na nossa lista!");
						System.out.print("\nPor favor, digite uma palavra: ");
						String novaPalavra = input.next();
						adicionarNovaPalavra(novaPalavra, nomeDoArquivo);
						System.out.print("\nNova palavra incluída com sucesso!\n\n");
						break;
					}
				} else {
					System.out.println("Errou!");
					vidas++;
					if (vidas == 6) {
						System.out.println("\nGAME OVER!!!!");
						break;
					} else {
						System.out.println("VOCÊ POSSUI " + (6 - vidas) + " vidas.");
					}
				}

			} else {
				System.out.println(" >>> letraDigitada JÁ UTILIZADA <<< ");
			}
		}

	}

	public static boolean verificaletraDigitada(char a, char v[]) {
		for (int i = 0; i < v.length; i++) {
			if (a == v[i])
				return false;
		}
		return true;
	}

	public static boolean ganhou(char[] v) {
		for (int i = 0; i < v.length; i++) {
			if (v[i] == '_')
				return false;
		}

		return true;
	}

	public static void main(String[] args) throws IOException {

		menuDeEscolhaDoTema();
		Scanner input = new Scanner(System.in);
		String temaEscolhido = input.nextLine();

		switch (temaEscolhido) {
		case "1":
			String nomeDoArquivo = "animais.txt";
			System.out.println("\t========================================================== ");
			System.out.println("\t|             O TEMA ESCOLHIDO FOI: ANIMAIS              |");
			System.out.println("\t==========================================================");
			menuDeRegras();
			executandoJogo(nomeDoArquivo);
			break;

		case "2":
			nomeDoArquivo = "times_de_futebol.txt";
			System.out.println("\t========================================================== ");
			System.out.println("\t|       O TEMA ESCOLHIDO FOI: TIMES DE FUTEBOL           |");
			System.out.println("\t==========================================================");
			menuDeRegras();
			executandoJogo(nomeDoArquivo);
			break;

		case "3":
			nomeDoArquivo = "paises.txt";
			System.out.println("\t========================================================== ");
			System.out.println("\t|              O TEMA ESCOLHIDO FOI: PAISES              |");
			System.out.println("\t==========================================================");
			menuDeRegras();
			executandoJogo(nomeDoArquivo);
			break;

		case "4":
			nomeDoArquivo = "carros.txt";
			System.out.println("\t========================================================== ");
			System.out.println("\t|              O TEMA ESCOLHIDO FOI: CARROS              |");
			System.out.println("\t==========================================================");
			menuDeRegras();
			executandoJogo(nomeDoArquivo);
			break;

		default:
			break;
		}
	}
}
